package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.mapper.StammdatenDomainMapper;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerPlanungsursaechlicherBedarfModel;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenBedarfModel;
import de.muenchen.isi.domain.model.stammdaten.SobonOrientierungswertSozialeInfrastrukturModel;
import de.muenchen.isi.infrastructure.entity.enums.Einrichtungstyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.repository.stammdaten.SobonOrientierungswertSozialeInfrastrukturRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LangfristigerPlanungsursaechlicherBedarfService {

    private final PlanungsursaechlicheBedarfService planungsursaechlicheBedarfService;

    private final SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository;

    private final StammdatenDomainMapper stammdatenDomainMapper;

    public LangfristigerPlanungsursaechlicherBedarfModel calculateLangfristigerPlanungsursaechlicherBedarfForKinderkrippe(
        final List<BauabschnittModel> bauabschnitte,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) {
        final var planungsursaechlicherBedarf = planungsursaechlicheBedarfService.calculatePlanungsursaechlicherBedarf(
            bauabschnitte,
            sobonJahr,
            gueltigAb
        );

        final var wohneinheitenBedarfForForderart = getWohneinheitenBedarfForForderart(planungsursaechlicherBedarf);

        final var sobonOrientierungswertForFoerderart =
            this.getSobonOrientierungswertForFoerderart(
                    planungsursaechlicherBedarf,
                    sobonJahr,
                    Einrichtungstyp.KINDERKRIPPE
                );

        return null;
    }

    protected Map<String, List<WohneinheitenBedarfModel>> getWohneinheitenBedarfForForderart(
        final PlanungsursaechlicherBedarfModel planungsursaechlicherBedarf
    ) {
        final var wohneinheitenBedarfForForderart = new HashMap<String, List<WohneinheitenBedarfModel>>();
        for (final var wohneinheitBedarf : planungsursaechlicherBedarf.getWohneinheitenBedarfe()) {
            if (wohneinheitenBedarfForForderart.containsKey(wohneinheitBedarf.getFoerderart())) {
                wohneinheitenBedarfForForderart.get(wohneinheitBedarf.getFoerderart()).add(wohneinheitBedarf);
            } else {
                final var wohneinheitenBedarf = new ArrayList<WohneinheitenBedarfModel>();
                wohneinheitenBedarf.add(wohneinheitBedarf);
                wohneinheitenBedarfForForderart.put(wohneinheitBedarf.getFoerderart(), wohneinheitenBedarf);
            }
        }
        wohneinheitenBedarfForForderart
            .values()
            .forEach(wohneinheitenBedarfe ->
                wohneinheitenBedarfe.sort(Comparator.comparing(WohneinheitenBedarfModel::getJahr))
            );
        return wohneinheitenBedarfForForderart;
    }

    protected Map<String, SobonOrientierungswertSozialeInfrastrukturModel> getSobonOrientierungswertForFoerderart(
        final PlanungsursaechlicherBedarfModel planungsursaechlicherBedarf,
        final SobonOrientierungswertJahr sobonJahr,
        final Einrichtungstyp einrichtungstyp
    ) {
        return planungsursaechlicherBedarf
            .getWohneinheitenBedarfe()
            .stream()
            .map(WohneinheitenBedarfModel::getFoerderart)
            .map(foerderart ->
                sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    einrichtungstyp,
                    foerderart,
                    sobonJahr.getGueltigAb()
                )
            )
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(stammdatenDomainMapper::entity2Model)
            .collect(
                Collectors.toMap(
                    SobonOrientierungswertSozialeInfrastrukturModel::getFoerderartBezeichnung,
                    Function.identity()
                )
            );
    }
}
