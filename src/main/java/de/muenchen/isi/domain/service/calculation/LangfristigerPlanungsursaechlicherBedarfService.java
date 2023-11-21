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
import java.math.BigDecimal;
import java.math.RoundingMode;
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

        final var wohneinheitenBedarfForFoerderart = getWohneinheitenBedarfForFoerderart(planungsursaechlicherBedarf);

        final var sobonOrientierungswertForFoerderart =
            this.getSobonOrientierungswertForFoerderart(
                    planungsursaechlicherBedarf,
                    sobonJahr,
                    Einrichtungstyp.KINDERKRIPPE
                );

        return null;
    }

    protected void calculatePlanungsursaechlicherBedarf(
        final List<WohneinheitenBedarfModel> wohneinheitenBedarfe,
        final SobonOrientierungswertSozialeInfrastrukturModel sobonOrientierungswertSozialeInfrastruktur
    ) {
        wohneinheitenBedarfe.sort(Comparator.comparing(WohneinheitenBedarfModel::getJahr));
        BigDecimal kinderJahr1NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr2NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr3NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr4NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr5NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr6NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr7NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr8NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr9NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr10NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr11NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr12NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr13NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr14NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr15NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr16NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr17NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr18NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr19NachErstellung = BigDecimal.ZERO;
        BigDecimal kinderJahr20NachErstellung = BigDecimal.ZERO;
        if (0 < wohneinheitenBedarfe.size()) {
            kinderJahr1NachErstellung =
                wohneinheitenBedarfe
                    .get(0)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr1NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (1 < wohneinheitenBedarfe.size()) {
            kinderJahr2NachErstellung =
                wohneinheitenBedarfe
                    .get(1)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr2NachErsterstellung()
                    )
                    .add(kinderJahr1NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (2 < wohneinheitenBedarfe.size()) {
            kinderJahr3NachErstellung =
                wohneinheitenBedarfe
                    .get(2)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr3NachErsterstellung()
                    )
                    .add(kinderJahr2NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (3 < wohneinheitenBedarfe.size()) {
            kinderJahr4NachErstellung =
                wohneinheitenBedarfe
                    .get(3)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr4NachErsterstellung()
                    )
                    .add(kinderJahr3NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (4 < wohneinheitenBedarfe.size()) {
            kinderJahr5NachErstellung =
                wohneinheitenBedarfe
                    .get(4)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr5NachErsterstellung()
                    )
                    .add(kinderJahr4NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (5 < wohneinheitenBedarfe.size()) {
            kinderJahr6NachErstellung =
                wohneinheitenBedarfe
                    .get(5)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr6NachErsterstellung()
                    )
                    .add(kinderJahr5NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (6 < wohneinheitenBedarfe.size()) {
            kinderJahr7NachErstellung =
                wohneinheitenBedarfe
                    .get(6)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr7NachErsterstellung()
                    )
                    .add(kinderJahr6NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (7 < wohneinheitenBedarfe.size()) {
            kinderJahr8NachErstellung =
                wohneinheitenBedarfe
                    .get(7)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr8NachErsterstellung()
                    )
                    .add(kinderJahr7NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (8 < wohneinheitenBedarfe.size()) {
            kinderJahr9NachErstellung =
                wohneinheitenBedarfe
                    .get(8)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr9NachErsterstellung()
                    )
                    .add(kinderJahr8NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (9 < wohneinheitenBedarfe.size()) {
            kinderJahr10NachErstellung =
                wohneinheitenBedarfe
                    .get(9)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr10NachErsterstellung()
                    )
                    .add(kinderJahr9NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (10 < wohneinheitenBedarfe.size()) {
            kinderJahr11NachErstellung =
                wohneinheitenBedarfe
                    .get(10)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr11NachErsterstellung()
                    )
                    .add(kinderJahr10NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (11 < wohneinheitenBedarfe.size()) {
            kinderJahr12NachErstellung =
                wohneinheitenBedarfe
                    .get(11)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr12NachErsterstellung()
                    )
                    .add(kinderJahr11NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (12 < wohneinheitenBedarfe.size()) {
            kinderJahr13NachErstellung =
                wohneinheitenBedarfe
                    .get(12)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr13NachErsterstellung()
                    )
                    .add(kinderJahr12NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (13 < wohneinheitenBedarfe.size()) {
            kinderJahr14NachErstellung =
                wohneinheitenBedarfe
                    .get(13)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr14NachErsterstellung()
                    )
                    .add(kinderJahr13NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (14 < wohneinheitenBedarfe.size()) {
            kinderJahr15NachErstellung =
                wohneinheitenBedarfe
                    .get(14)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr15NachErsterstellung()
                    )
                    .add(kinderJahr14NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (15 < wohneinheitenBedarfe.size()) {
            kinderJahr16NachErstellung =
                wohneinheitenBedarfe
                    .get(15)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr16NachErsterstellung()
                    )
                    .add(kinderJahr15NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (16 < wohneinheitenBedarfe.size()) {
            kinderJahr17NachErstellung =
                wohneinheitenBedarfe
                    .get(16)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr17NachErsterstellung()
                    )
                    .add(kinderJahr16NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (17 < wohneinheitenBedarfe.size()) {
            kinderJahr18NachErstellung =
                wohneinheitenBedarfe
                    .get(17)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr18NachErsterstellung()
                    )
                    .add(kinderJahr17NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (18 < wohneinheitenBedarfe.size()) {
            kinderJahr19NachErstellung =
                wohneinheitenBedarfe
                    .get(18)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr19NachErsterstellung()
                    )
                    .add(kinderJahr18NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
        if (19 < wohneinheitenBedarfe.size()) {
            kinderJahr20NachErstellung =
                wohneinheitenBedarfe
                    .get(19)
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr20NachErsterstellung()
                    )
                    .add(kinderJahr19NachErstellung)
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
        }
    }

    protected Map<String, List<WohneinheitenBedarfModel>> getWohneinheitenBedarfForFoerderart(
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
