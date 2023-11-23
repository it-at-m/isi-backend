package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapper;
import de.muenchen.isi.domain.model.calculation.PlanungsursachlicheWohneinheitenModel;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfModel;
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
public class PlanungsursaechlicherBedarfService {

    private final SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository;

    private final VersorgungsquoteGruppenstaerkeRepository versorgungsquoteGruppenstaerkeRepository;

    private final StammdatenDomainMapper stammdatenDomainMapper;

    public List<PlanungsursaechlicherBedarfModel> calculatePlanungsursaechlicherBedarfForKinderkrippe(
        final List<PlanungsursachlicheWohneinheitenModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) throws EntityNotFoundException {
        return this.calculatePlanungsursaechlicherBedarf(
                InfrastruktureinrichtungTyp.KINDERKRIPPE,
                wohneinheiten,
                sobonJahr,
                gueltigAb
            );
    }

    public List<PlanungsursaechlicherBedarfModel> calculatePlanungsursaechlicherBedarfForKindergarten(
        final List<PlanungsursachlicheWohneinheitenModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) throws EntityNotFoundException {
        return this.calculatePlanungsursaechlicherBedarf(
                InfrastruktureinrichtungTyp.KINDERGARTEN,
                wohneinheiten,
                sobonJahr,
                gueltigAb
            );
    }

    protected List<PlanungsursaechlicherBedarfModel> calculatePlanungsursaechlicherBedarf(
        final InfrastruktureinrichtungTyp einrichtung,
        final List<PlanungsursachlicheWohneinheitenModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) throws EntityNotFoundException {
        // Gruppieren der planungsursächlichen Wohneinheiten nach Förderart
        final var planungsursachlicheWohneinheitenForFoerderart =
            this.getPlanungsursachlicheWohneinheitenForFoerderart(wohneinheiten);

        // Ermittlung und Gruppierung der SoBon-Orientierungswerte nach Förderart
        final var sobonOrientierungswertForFoerderart =
            this.getSobonOrientierungswertForFoerderart(wohneinheiten, sobonJahr, einrichtung);

        // Ermittlung der Versorgungsquote und Gruppenstärke für die Einrichtung.
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
        return planungsursachlicheWohneinheitenForFoerderart
            .keySet()
            .stream()
            .flatMap(foerderart ->
                // Ermittlung der planungsursächlichen Bedarfe je Förderart für 20 Jahre
                this.calculatePlanungsursaechlicheBedarfe(
                        planungsursachlicheWohneinheitenForFoerderart.get(foerderart),
                        sobonOrientierungswertForFoerderart.get(foerderart)
                    )
                    .stream()
            )
            // Gruppieren der planungsursächlichen Bedarfe je Jahr
            .collect(
                Collectors.groupingBy(
                    PlanungsursaechlicherBedarfModel::getJahr,
                    Collectors.mapping(Function.identity(), Collectors.toList())
                )
            )
            .values()
            .stream()
            // Bilden der Jahressumme der vorher gruppierten planungsursächlichen Bedarfe.
            .map(planungsursaechlicheBedarfeForJahr ->
                planungsursaechlicheBedarfeForJahr.stream().reduce(new PlanungsursaechlicherBedarfModel(), this::add)
            )
            .sorted(Comparator.comparing(PlanungsursaechlicherBedarfModel::getJahr))
            // Ermitteln der Versorgungsquote und Gruppenstärke
            .map(planungsursaechlicherBedarfTest ->
                setVersorgungsquoteAndGruppenstaerke(planungsursaechlicherBedarfTest, versorgungsquoteGruppenstaerke)
            )
            .collect(Collectors.toList());
    }

    protected PlanungsursaechlicherBedarfModel setVersorgungsquoteAndGruppenstaerke(
        final PlanungsursaechlicherBedarfModel planungsursaechlicherBedarf,
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

    protected PlanungsursaechlicherBedarfModel add(
        final PlanungsursaechlicherBedarfModel o1,
        final PlanungsursaechlicherBedarfModel o2
    ) {
        final var planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        final var jahr = ObjectUtils.defaultIfNull(o1.getJahr(), o2.getJahr());
        final var sumAnzahlKinderGesamt = ObjectUtils
            .defaultIfNull(o1.getAnzahlKinderGesamt(), BigDecimal.ZERO)
            .add(ObjectUtils.defaultIfNull(o2.getAnzahlKinderGesamt(), BigDecimal.ZERO));
        planungsursaechlicherBedarf.setJahr(jahr);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(sumAnzahlKinderGesamt);
        return planungsursaechlicherBedarf;
    }

    protected Map<String, List<PlanungsursachlicheWohneinheitenModel>> getPlanungsursachlicheWohneinheitenForFoerderart(
        final List<PlanungsursachlicheWohneinheitenModel> wohneinheiten
    ) {
        return wohneinheiten
            .stream()
            .collect(
                Collectors.groupingBy(
                    PlanungsursachlicheWohneinheitenModel::getFoerderart,
                    Collectors.mapping(Function.identity(), Collectors.toList())
                )
            );
    }

    protected Map<String, SobonOrientierungswertSozialeInfrastrukturModel> getSobonOrientierungswertForFoerderart(
        final List<PlanungsursachlicheWohneinheitenModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final InfrastruktureinrichtungTyp einrichtungstyp
    ) {
        return wohneinheiten
            .stream()
            .map(PlanungsursachlicheWohneinheitenModel::getFoerderart)
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

    protected List<PlanungsursaechlicherBedarfModel> calculatePlanungsursaechlicheBedarfe(
        final List<PlanungsursachlicheWohneinheitenModel> planungsursachlicheWohneinheiten,
        final SobonOrientierungswertSozialeInfrastrukturModel sobonOrientierungswertSozialeInfrastruktur
    ) {
        planungsursachlicheWohneinheiten.sort(Comparator.comparing(PlanungsursachlicheWohneinheitenModel::getJahr));
        final var planungsursaechlicheBedarfe = new ArrayList<PlanungsursaechlicherBedarfModel>();
        BigDecimal anzahlKinderGesamt;
        PlanungsursachlicheWohneinheitenModel wohneinheiten;
        if (0 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(0);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr1NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (1 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(1);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr2NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(0).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (2 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(2);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr3NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(1).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (3 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(3);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr4NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(2).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (4 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(4);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr5NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(3).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (5 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(5);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr6NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(4).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (6 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(6);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr7NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(5).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (7 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(7);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr8NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(6).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (8 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(8);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr9NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(7).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (9 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(9);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr10NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(8).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (10 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(10);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr11NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(9).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (11 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(11);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr12NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(10).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (12 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(12);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr13NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(11).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (13 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(13);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr14NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(12).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (14 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(14);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr15NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(13).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (15 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(15);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr16NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(14).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (16 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(16);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr17NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(15).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (17 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(17);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr18NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(16).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (18 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(18);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr19NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(17).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        if (19 < planungsursachlicheWohneinheiten.size()) {
            wohneinheiten = planungsursachlicheWohneinheiten.get(19);
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr20NachErsterstellung()
                    )
                    .add(planungsursaechlicheBedarfe.get(18).getAnzahlKinderGesamt())
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(wohneinheiten.getJahr(), anzahlKinderGesamt)
            );
        }
        return planungsursaechlicheBedarfe;
    }

    protected PlanungsursaechlicherBedarfModel createPlanungsursaechlicherBedarf(
        final Integer jahr,
        final BigDecimal anzahlKinderGesamt
    ) {
        final var planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(jahr);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(anzahlKinderGesamt);
        return planungsursaechlicherBedarf;
    }
}
