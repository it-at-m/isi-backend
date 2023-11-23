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
import org.apache.commons.lang3.math.NumberUtils;
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
        final var wohneinheitenWithoutSum = wohneinheiten
            .stream()
            .filter(planungsursachlicheWohneinheiten ->
                NumberUtils.isParsable(planungsursachlicheWohneinheiten.getJahr())
            )
            .collect(Collectors.toList());

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

        final var firstPlanungsUrsaechlichesJahr = wohneinheitenWithoutSum
            .stream()
            .map(PlanungsursachlicheWohneinheitenModel::getJahr)
            .mapToInt(Integer::parseInt)
            .min()
            .getAsInt();

        // Berechnung Gesamtanzahl der Kinder je Jahr
        return wohneinheitenWithoutSum
            .stream()
            .flatMap(planungsursaechlicheWohneinheiten ->
                calculatePlanungsursaechlicheBedarfe(
                    firstPlanungsUrsaechlichesJahr,
                    planungsursaechlicheWohneinheiten,
                    sobonOrientierungswertForFoerderart.get(planungsursaechlicheWohneinheiten.getFoerderart())
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
            .distinct()
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
        final Integer firstPlanungsursaechlichesJahr,
        final PlanungsursachlicheWohneinheitenModel wohneinheiten,
        final SobonOrientierungswertSozialeInfrastrukturModel sobonOrientierungswertSozialeInfrastruktur
    ) {
        final var planungsursaechlicheBedarfe = new ArrayList<PlanungsursaechlicherBedarfModel>();
        final var numberOfYearsToCalculate =
            20 - (Integer.parseInt(wohneinheiten.getJahr()) - firstPlanungsursaechlichesJahr);
        var yearToCalculate = 0;
        BigDecimal anzahlKinderGesamt;
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr1NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr2NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr3NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr4NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr5NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr6NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr7NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr8NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr9NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr10NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr11NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr12NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr13NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr14NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr15NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr16NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr17NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr18NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr19NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlKinderGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(
                        sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr20NachErsterstellung()
                    )
                    .setScale(SobonOrientierungswertSozialeInfrastrukturModel.SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
        }
        return planungsursaechlicheBedarfe;
    }

    protected PlanungsursaechlicherBedarfModel createPlanungsursaechlicherBedarf(
        final Integer jahr,
        final BigDecimal anzahlKinderGesamt
    ) {
        final var planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(jahr));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(anzahlKinderGesamt);
        return planungsursaechlicherBedarf;
    }
}
