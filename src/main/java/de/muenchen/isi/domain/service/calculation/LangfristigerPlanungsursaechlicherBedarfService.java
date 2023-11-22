package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapper;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfModel;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfTestModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenBedarfModel;
import de.muenchen.isi.domain.model.stammdaten.SobonOrientierungswertSozialeInfrastrukturModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.stammdaten.VersorgungsquoteGruppenstaerke;
import de.muenchen.isi.infrastructure.repository.stammdaten.SobonOrientierungswertSozialeInfrastrukturRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.VersorgungsquoteGruppenstaerkeRepository;
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
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LangfristigerPlanungsursaechlicherBedarfService {

    private final SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository;

    private final VersorgungsquoteGruppenstaerkeRepository versorgungsquoteGruppenstaerkeRepository;

    private final StammdatenDomainMapper stammdatenDomainMapper;

    public List<PlanungsursaechlicherBedarfTestModel> calculateLangfristigerPlanungsursaechlicherBedarf(
        final InfrastruktureinrichtungTyp einrichtung,
        final PlanungsursaechlicherBedarfModel planungsursaechlicherBedarf,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) throws EntityNotFoundException {
        final var wohneinheitenBedarfForFoerderart = getWohneinheitenBedarfForFoerderart(planungsursaechlicherBedarf);

        final var sobonOrientierungswertForFoerderart =
            this.getSobonOrientierungswertForFoerderart(planungsursaechlicherBedarf, sobonJahr, einrichtung);

        final var versorgungsquoteGruppenstaerke = versorgungsquoteGruppenstaerkeRepository
            .findFirstByInfrastruktureinrichtungTypAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                einrichtung,
                gueltigAb
            )
            .orElseThrow(() -> {
                final var message =
                    "Das Stammdatum der Versorgungsquote und Gruppenstärke für den Einrichtungstyp " +
                    einrichtung +
                    "wurde nicht gefunden.";
                log.error(message);
                return new EntityNotFoundException(message);
            });

        // Berechnung Gesamtanzahl der Kinder je Jahr
        return wohneinheitenBedarfForFoerderart
            .keySet()
            .stream()
            .flatMap(foerderart ->
                // Ermittlung der planungsursächlichen Bedarfe je Förderart für 20 Jahre
                this.calculatePlanungsursaechlicherBedarfe(
                        wohneinheitenBedarfForFoerderart.get(foerderart),
                        sobonOrientierungswertForFoerderart.get(foerderart)
                    )
                    .stream()
            )
            // Gruppieren der planungsursächlichen Bedarfe je Jahr
            .collect(
                Collectors.groupingBy(
                    PlanungsursaechlicherBedarfTestModel::getJahr,
                    Collectors.mapping(Function.identity(), Collectors.toList())
                )
            )
            .values()
            .stream()
            // Bilden der Jahressumme der vorher gruppierten planungsursächlichen Bedarfe.
            .map(planungsursaechlicheBedarfeForJahr ->
                planungsursaechlicheBedarfeForJahr
                    .stream()
                    .reduce(new PlanungsursaechlicherBedarfTestModel(), this::add)
            )
            .sorted(Comparator.comparing(PlanungsursaechlicherBedarfTestModel::getJahr))
            .map(planungsursaechlicherBedarfTest ->
                setVersorgungsquoteAndGruppenstaerke(planungsursaechlicherBedarfTest, versorgungsquoteGruppenstaerke)
            )
            .collect(Collectors.toList());
    }

    private PlanungsursaechlicherBedarfTestModel setVersorgungsquoteAndGruppenstaerke(
        final PlanungsursaechlicherBedarfTestModel planungsursaechlicherBedarf,
        final VersorgungsquoteGruppenstaerke versorgungsquoteGruppenstaerke
    ) {
        final var anzahlKinderGesamt = planungsursaechlicherBedarf.getAnzahlKinderGesamt();
        final var anzahlKinderZuVersorgen = anzahlKinderGesamt.multiply(
            versorgungsquoteGruppenstaerke.getVersorgungsquotePlanungsursaechlich()
        );
        final var anzahlGruppen = anzahlKinderZuVersorgen.divide(
            BigDecimal.valueOf(versorgungsquoteGruppenstaerke.getGruppenstaerke()),
            2,
            RoundingMode.HALF_EVEN
        );
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(anzahlKinderZuVersorgen);
        planungsursaechlicherBedarf.setAnzahlGruppen(anzahlGruppen);
        return planungsursaechlicherBedarf;
    }

    protected PlanungsursaechlicherBedarfTestModel add(
        final PlanungsursaechlicherBedarfTestModel o1,
        final PlanungsursaechlicherBedarfTestModel o2
    ) {
        final var planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfTestModel();
        final var jahr = ObjectUtils.defaultIfNull(o1.getJahr(), o2.getJahr());
        final var sumAnzahlKinderGesamt = ObjectUtils
            .defaultIfNull(o1.getAnzahlKinderGesamt(), BigDecimal.ZERO)
            .add(ObjectUtils.defaultIfNull(o2.getAnzahlKinderGesamt(), BigDecimal.ZERO));
        planungsursaechlicherBedarf.setJahr(jahr);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(sumAnzahlKinderGesamt);
        return planungsursaechlicherBedarf;
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
        final InfrastruktureinrichtungTyp einrichtungstyp
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

    private PlanungsursaechlicherBedarfTestModel createPlanungsursaechlicherBedarfTest(
        final Integer jahr,
        final BigDecimal anzahlKinderGesamt
    ) {
        final var planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfTestModel();
        planungsursaechlicherBedarf.setJahr(jahr);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(anzahlKinderGesamt);
        return planungsursaechlicherBedarf;
    }

    protected List<PlanungsursaechlicherBedarfTestModel> calculatePlanungsursaechlicherBedarfe(
        final List<WohneinheitenBedarfModel> wohneinheitenBedarfe,
        final SobonOrientierungswertSozialeInfrastrukturModel sobonOrientierungswertSozialeInfrastruktur
    ) {
        wohneinheitenBedarfe.sort(Comparator.comparing(WohneinheitenBedarfModel::getJahr));
        final var planungsursaechlicheBedarfe = new ArrayList<PlanungsursaechlicherBedarfTestModel>();
        BigDecimal anzahlKinderGesamt;
        WohneinheitenBedarfModel wohneinheitenBedarf;
        if (0 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(0);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr1NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (1 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(1);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr2NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(0).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (2 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(2);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr3NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(1).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (3 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(3);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr4NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(2).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (4 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(4);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr5NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(3).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (5 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(5);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr6NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(4).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (6 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(6);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr7NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(5).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (7 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(7);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr8NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(6).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (8 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(8);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr9NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(7).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (9 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(9);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr10NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(8).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (10 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(10);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr11NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(9).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (11 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(11);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr12NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(10).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (12 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(12);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr13NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(11).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (13 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(13);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr14NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(12).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (14 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(14);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr15NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(13).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (15 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(15);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr16NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(14).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (16 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(16);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr17NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(15).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (17 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(17);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr18NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(16).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (18 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(18);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr19NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(17).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        if (19 < wohneinheitenBedarfe.size()) {
            wohneinheitenBedarf = wohneinheitenBedarfe.get(19);
            anzahlKinderGesamt =
                wohneinheitenBedarf
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr20NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(18).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarfTest(wohneinheitenBedarf.getJahr(), anzahlKinderGesamt)
            );
        }
        return planungsursaechlicheBedarfe;
    }
}
