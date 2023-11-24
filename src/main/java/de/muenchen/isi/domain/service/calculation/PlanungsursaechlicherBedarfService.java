package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.mapper.StammdatenDomainMapper;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfProJahrModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
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
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanungsursaechlicherBedarfService {

    public static final int SCALE_ROUNDING_RESULT_INTEGER = 0;

    public static final int SCALE_ROUNDING_RESULT_DECIMAL = 2;

    public static final String TITLE_MEAN_YEAR_10 = "Mittelwert 10 J.";

    public static final String TITLE_MEAN_YEAR_15 = "Mittelwert 15 J.";

    public static final String TITLE_MEAN_YEAR_20 = "Mittelwert 20 J.";

    private final SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository;

    private final VersorgungsquoteGruppenstaerkeRepository versorgungsquoteGruppenstaerkeRepository;

    private final StammdatenDomainMapper stammdatenDomainMapper;

    /**
     * Ermittlung die gerundeten planungsursächlichen Bedarfe sowie den 10-Jahres, 15-jahres und 20-Jahres-Mittelwert
     * für den Zeitraum von 20 Jahren auf Basis der gegebenen planungsursächlichen Wohneinheiten für Kinderkrippen.
     *
     * @param wohneinheiten zur Ermittlung der planungsursächlichen Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastuktur.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die planungsursächlichen Bedarfe für den Zeitraum von 20 Jahren
     */
    public List<
        PlanungsursaechlicherBedarfProJahrModel
    > calculatePlanungsursaechlicherBedarfForKinderkrippeRoundedAndWithMean(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) {
        final var roundedPlanungsursaechlicheBedarfe =
            this.calculatePlanungsursaechlicherBedarfForKinderkrippe(wohneinheiten, sobonJahr, gueltigAb)
                .map(this::roundValuesAndReturnModelWithRoundedValues)
                .collect(Collectors.toList());
        final var meansForRoundedPlanungsursaechlicheBedarfe =
            this.calculate10Year15YearAnd20YearMean(roundedPlanungsursaechlicheBedarfe);
        roundedPlanungsursaechlicheBedarfe.addAll(meansForRoundedPlanungsursaechlicheBedarfe);
        return roundedPlanungsursaechlicheBedarfe;
    }

    /**
     * Ermittlung die planungsursächlichen Bedarfe für den Zeitraum von 20 Jahren auf Basis der gegebenen
     * planungsursächlichen Wohneinheiten für Kinderkrippen.
     *
     * @param wohneinheiten zur Ermittlung der planungsursächlichen Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastuktur.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die planungsursächlichen Bedarfe für den Zeitraum von 20 Jahren
     */
    public Stream<PlanungsursaechlicherBedarfProJahrModel> calculatePlanungsursaechlicherBedarfForKinderkrippe(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) {
        return this.calculatePlanungsursaechlicherBedarf(
                InfrastruktureinrichtungTyp.KINDERKRIPPE,
                wohneinheiten,
                sobonJahr,
                gueltigAb
            );
    }

    /**
     * Ermittlung die gerundeten planungsursächlichen Bedarfe sowie den 10-Jahres, 15-jahres und 20-Jahres-Mittelwert
     * für den Zeitraum von 20 Jahren auf Basis der gegebenen planungsursächlichen Wohneinheiten für Kindergärten.
     *
     * @param wohneinheiten zur Ermittlung der planungsursächlichen Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastuktur.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die planungsursächlichen Bedarfe für den Zeitraum von 20 Jahren
     */
    public List<
        PlanungsursaechlicherBedarfProJahrModel
    > calculatePlanungsursaechlicherBedarfForKindergartenRoundedAndWithMean(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) {
        final var roundedPlanungsursaechlicheBedarfe =
            this.calculatePlanungsursaechlicherBedarfForKindergarten(wohneinheiten, sobonJahr, gueltigAb)
                .map(this::roundValuesAndReturnModelWithRoundedValues)
                .collect(Collectors.toList());
        final var meansForRoundedPlanungsursaechlicheBedarfe =
            this.calculate10Year15YearAnd20YearMean(roundedPlanungsursaechlicheBedarfe);
        roundedPlanungsursaechlicheBedarfe.addAll(meansForRoundedPlanungsursaechlicheBedarfe);
        return roundedPlanungsursaechlicheBedarfe;
    }

    /**
     * Ermittlung die planungsursächlichen Bedarfe für den Zeitraum von 20 Jahren auf Basis der gegebenen
     * planungsursächlichen Wohneinheiten für Kindergärten.
     *
     * @param wohneinheiten zur Ermittlung der planungsursächlichen Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastuktur.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die planungsursächlichen Bedarfe für den Zeitraum von 20 Jahren
     */
    public Stream<PlanungsursaechlicherBedarfProJahrModel> calculatePlanungsursaechlicherBedarfForKindergarten(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) {
        return this.calculatePlanungsursaechlicherBedarf(
                InfrastruktureinrichtungTyp.KINDERGARTEN,
                wohneinheiten,
                sobonJahr,
                gueltigAb
            );
    }

    /**
     * Ermittlung die planungsursächlichen Bedarfe für den Zeitraum von 20 Jahren für die im Parameter gegebene Einrichtung
     * auf Basis der gegebenen planungsursächlichen Wohneinheiten.
     *
     * @param einrichtung zur Ermittlung der planungsursächlichen Bedarfe.
     * @param wohneinheiten zur Ermittlung der planungsursächlichen Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastuktur.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die planungsursächlichen Bedarfe für den Zeitraum von 20 Jahren
     */
    protected Stream<PlanungsursaechlicherBedarfProJahrModel> calculatePlanungsursaechlicherBedarf(
        final InfrastruktureinrichtungTyp einrichtung,
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) {
        final var wohneinheitenWithoutSum = wohneinheiten
            .stream()
            .filter(planungsursachlicheWohneinheiten ->
                NumberUtils.isParsable(planungsursachlicheWohneinheiten.getJahr())
            )
            .collect(Collectors.toList());

        // Ermittlung und Gruppierung der SoBon-Orientierungswerte nach Förderart
        final var sobonOrientierungswertForFoerderart =
            this.getSobonOrientierungswertGroupedByFoerderart(wohneinheiten, sobonJahr, einrichtung);

        // Ermittlung der Versorgungsquote und Gruppenstärke für die Einrichtung.
        final var versorgungsquoteGruppenstaerke = versorgungsquoteGruppenstaerkeRepository
            .findFirstByInfrastruktureinrichtungTypAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                einrichtung,
                gueltigAb
            )
            .get();

        final var earliestPlanungsUrsaechlichesJahr = wohneinheitenWithoutSum
            .stream()
            .map(WohneinheitenProFoerderartProJahrModel::getJahr)
            .mapToInt(Integer::parseInt)
            .min()
            .getAsInt();

        // Berechnung Gesamtanzahl der Kinder je Jahr
        return wohneinheitenWithoutSum
            .stream()
            .flatMap(planungsursaechlicheWohneinheiten ->
                calculatePlanungsursaechlicheBedarfe(
                    earliestPlanungsUrsaechlichesJahr,
                    planungsursaechlicheWohneinheiten,
                    sobonOrientierungswertForFoerderart.get(planungsursaechlicheWohneinheiten.getFoerderart())
                )
            )
            // Gruppieren der planungsursächlichen Bedarfe je Jahr
            .collect(
                Collectors.groupingBy(
                    PlanungsursaechlicherBedarfProJahrModel::getJahr,
                    Collectors.mapping(Function.identity(), Collectors.toList())
                )
            )
            .values()
            .stream()
            // Bilden der Jahressumme der vorher gruppierten planungsursächlichen Bedarfe.
            .map(planungsursaechlicheBedarfeForJahr ->
                planungsursaechlicheBedarfeForJahr
                    .stream()
                    .reduce(new PlanungsursaechlicherBedarfProJahrModel(), this::add)
            )
            .sorted(Comparator.comparing(PlanungsursaechlicherBedarfProJahrModel::getJahr))
            // Ermitteln der Versorgungsquote und Gruppenstärke
            .map(planungsursaechlicherBedarfTest ->
                this.setVersorgungsquoteAndGruppenstaerkeInPlanungsursaechlichenBedarf(
                        planungsursaechlicherBedarfTest,
                        versorgungsquoteGruppenstaerke
                    )
            );
    }

    /**
     * Ermittelt auf Basis des planungsursächlichen Bedarfs und der Versorgungsquote und Gruppenstärke die Anzahl
     * an zu versorgenden Kinder sowie die Gruppenstärke für die zu versorgenden Kinder und setzt die ermittelten Werte
     * im gegebenen Bedarfsobjekt.
     *
     * @param planungsursaechlicherBedarf zur Ermittlung.
     * @param versorgungsquoteGruppenstaerke zur Ermittlung.
     * @return den um die zu versorgenden Kinder und die Anzahl der Gruppen angereicherten planungsursächlichen Bedarf.
     */
    protected PlanungsursaechlicherBedarfProJahrModel setVersorgungsquoteAndGruppenstaerkeInPlanungsursaechlichenBedarf(
        final PlanungsursaechlicherBedarfProJahrModel planungsursaechlicherBedarf,
        final VersorgungsquoteGruppenstaerke versorgungsquoteGruppenstaerke
    ) {
        final var anzahlKinderGesamt = planungsursaechlicherBedarf.getAnzahlKinderGesamt();
        final var anzahlKinderZuVersorgen = anzahlKinderGesamt.multiply(
            versorgungsquoteGruppenstaerke.getVersorgungsquotePlanungsursaechlich()
        );
        final var anzahlGruppen = anzahlKinderZuVersorgen.divide(
            BigDecimal.valueOf(versorgungsquoteGruppenstaerke.getGruppenstaerke()),
            SCALE_ROUNDING_RESULT_DECIMAL,
            RoundingMode.HALF_EVEN
        );
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(anzahlKinderZuVersorgen);
        planungsursaechlicherBedarf.setAnzahlGruppen(anzahlGruppen);
        return planungsursaechlicherBedarf;
    }

    /**
     * Rundet die Werte für den im Parameter gegebenen planungsursächlichen Bedarf und gibt den Bedarf mit den gerundeten Werten zurück.
     *
     * - {@link PlanungsursaechlicherBedarfProJahrModel#getAnzahlKinderGesamt()} runden auf {@link this#SCALE_ROUNDING_RESULT_INTEGER}.
     * - {@link PlanungsursaechlicherBedarfProJahrModel#getAnzahlKinderZuVersorgen()} ()} runden auf {@link this#SCALE_ROUNDING_RESULT_INTEGER}.
     * - {@link PlanungsursaechlicherBedarfProJahrModel#getAnzahlGruppen()} runden auf {@link this#SCALE_ROUNDING_RESULT_DECIMAL}.
     *
     * @param planungsursaechlicherBedarf zum Runden der Werte.
     * @return den planungsursächliechen Bedarf mit gerundeten Werten.
     */
    protected PlanungsursaechlicherBedarfProJahrModel roundValuesAndReturnModelWithRoundedValues(
        final PlanungsursaechlicherBedarfProJahrModel planungsursaechlicherBedarf
    ) {
        final var anzahlKinderGesamtRounded = planungsursaechlicherBedarf
            .getAnzahlKinderGesamt()
            .setScale(SCALE_ROUNDING_RESULT_INTEGER, RoundingMode.HALF_EVEN);
        final var anzahlKinderZuVersorgenRounded = planungsursaechlicherBedarf
            .getAnzahlKinderZuVersorgen()
            .setScale(SCALE_ROUNDING_RESULT_INTEGER, RoundingMode.HALF_EVEN);
        final var anzahlGruppenRounded = planungsursaechlicherBedarf
            .getAnzahlGruppen()
            .setScale(SCALE_ROUNDING_RESULT_DECIMAL, RoundingMode.HALF_EVEN);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(anzahlKinderGesamtRounded);
        planungsursaechlicherBedarf.setAnzahlKinderZuVersorgen(anzahlKinderZuVersorgenRounded);
        planungsursaechlicherBedarf.setAnzahlGruppen(anzahlGruppenRounded);
        return planungsursaechlicherBedarf;
    }

    /**
     * Summiert die Gesamtanzahl der Kinder der in den Parameter gegebenen planungsursächlichen Bedarfe.
     *
     * Als relevantes Jahr wird das Jahr der Bedarfe im Parameter o1 gesetzt, falls diese vorhanden ist.
     * Ansonsten wird das jahr des Parameter o2 verwendet.
     *
     * @param o1 zum Summieren.
     * @param o2 zum Summieren.
     * @return den planungsursächlichen Bedarf mit der summierten Gesamtanzahl der Kinder
     */
    protected PlanungsursaechlicherBedarfProJahrModel add(
        final PlanungsursaechlicherBedarfProJahrModel o1,
        final PlanungsursaechlicherBedarfProJahrModel o2
    ) {
        final var planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfProJahrModel();
        final var jahr = ObjectUtils.defaultIfNull(o1.getJahr(), o2.getJahr());
        final var sumAnzahlKinderGesamt = ObjectUtils
            .defaultIfNull(o1.getAnzahlKinderGesamt(), BigDecimal.ZERO)
            .add(ObjectUtils.defaultIfNull(o2.getAnzahlKinderGesamt(), BigDecimal.ZERO));
        planungsursaechlicherBedarf.setJahr(jahr);
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(sumAnzahlKinderGesamt);
        return planungsursaechlicherBedarf;
    }

    /**
     * Ermittelt je Förderart in den gegebenen planungsursächlichen Wohneinheiten die Sobon-Orientierungswerte zur
     * sozialen Infrastruktur.
     *
     * @param wohneinheiten mit Förderarten zur Ermittlung der Sobon-Orientierungswerte zur sozialen Infrastruktur je Förderart.
     * @param sobonJahr zur Extraktion der korrekten Sobon-Orientierungswerte zur sozialen Infrastruktur je Förderart.
     * @param einrichtungstyp zur Extraktion der korrekten Sobon-Orientierungswerte zur sozialen Infrastruktur je Förderart.
     * @return die Sobon-Orientierungswerte zur sozialen Infrastruktur gruppiert nach Förderart.
     */
    protected Map<String, SobonOrientierungswertSozialeInfrastrukturModel> getSobonOrientierungswertGroupedByFoerderart(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final InfrastruktureinrichtungTyp einrichtungstyp
    ) {
        return wohneinheiten
            .stream()
            .map(WohneinheitenProFoerderartProJahrModel::getFoerderart)
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

    /**
     * Ermittelt für die im Parameter gegebenen Wohneinheiten die planungsursächlichen Bedarfe
     * auf Basis der im Paramter Sobon-Orientierungswerte der sozialen Infrastruktur.
     *
     * Entspricht das jahr der planungsursächlichen Wohneinheiten dem ersten planungsursächlichen Jahr,
     * so werden planungsursächlichen Bedarfe für 20 Jahre ermittelt.
     * Jedes weitere Jahr der planungsursächlichen Wohneinheiten reduziert die Anzahl der planungsursächlichen Bedarfe
     * jeweils um ein Jahr.
     *
     * @param firstPlanungsursaechlichesJahr zur Ermittlung der notwendigen Anzahl an jährlichen planungsursächlichen Bedarfe.
     * @param wohneinheiten zur Ermittlung der planungsursächliechen Bedarfe.
     * @param sobonOrientierungswertSozialeInfrastruktur zur Ermittlung der planungsursächlichen Bedarfe.
     * @return die planungsursächlichen Bedarfe der gegebenen Wohneinheiten unter Berücksichtung des ersten planungsursächlichen Jahres.
     */
    protected Stream<PlanungsursaechlicherBedarfProJahrModel> calculatePlanungsursaechlicheBedarfe(
        final Integer firstPlanungsursaechlichesJahr,
        final WohneinheitenProFoerderartProJahrModel wohneinheiten,
        final SobonOrientierungswertSozialeInfrastrukturModel sobonOrientierungswertSozialeInfrastruktur
    ) {
        final var planungsursaechlicheBedarfe = new ArrayList<PlanungsursaechlicherBedarfProJahrModel>();
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
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
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
            planungsursaechlicheBedarfe.add(
                createPlanungsursaechlicherBedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
        }
        return planungsursaechlicheBedarfe.stream();
    }

    /**
     * Erstellt ein {@link PlanungsursaechlicherBedarfProJahrModel} mit den im Parameter gegebenen Werten.
     *
     * @param jahr des planungsursächlichen Bedarfs.
     * @param anzahlKinderGesamt des planungsursächlichen Bedarfs.
     * @return den planungsursächlichen Bedarf mit gesetzten Jahr und der Gesamtanzahl an Kinder.
     */
    protected PlanungsursaechlicherBedarfProJahrModel createPlanungsursaechlicherBedarf(
        final Integer jahr,
        final BigDecimal anzahlKinderGesamt
    ) {
        final var planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfProJahrModel();
        planungsursaechlicherBedarf.setJahr(Integer.toString(jahr));
        planungsursaechlicherBedarf.setAnzahlKinderGesamt(anzahlKinderGesamt);
        return planungsursaechlicherBedarf;
    }

    /**
     * Die Methode ermittelt den 10-Jahres-, 15-Jahres- und 20-Jahres-Mittelwert für die im Parameter gegebenen Liste
     * an planungsursächlichen Bedarfen.
     *
     * @param planungsursaechlicheBedarfe zur Ermittlung der 10-Jahres-, 15-Jahres- und 20-Jahres-Mittelwerte
     * @return den 10-Jahres-, 15-Jahres- und 20-Jahres-Mittelwert als Liste.
     */
    protected List<PlanungsursaechlicherBedarfProJahrModel> calculate10Year15YearAnd20YearMean(
        final List<PlanungsursaechlicherBedarfProJahrModel> planungsursaechlicheBedarfe
    ) {
        final var means10Year15YearAnd20Year = new ArrayList<PlanungsursaechlicherBedarfProJahrModel>(3);
        var sumAnzahlKinderGesamt = BigDecimal.ZERO;
        BigDecimal sumAnzahlKinderZuVersorgen = BigDecimal.ZERO;
        BigDecimal sumAnzahlGruppen = BigDecimal.ZERO;
        BigDecimal numberOfYear;
        for (int index = 0; index < planungsursaechlicheBedarfe.size(); index++) {
            numberOfYear = BigDecimal.valueOf(index + 1);
            sumAnzahlKinderGesamt =
                planungsursaechlicheBedarfe.get(index).getAnzahlKinderGesamt().add(sumAnzahlKinderGesamt);
            sumAnzahlKinderZuVersorgen =
                planungsursaechlicheBedarfe.get(index).getAnzahlKinderZuVersorgen().add(sumAnzahlKinderZuVersorgen);
            sumAnzahlGruppen = planungsursaechlicheBedarfe.get(index).getAnzahlGruppen().add(sumAnzahlGruppen);
            if (index == 9) {
                final var meanYear10 = new PlanungsursaechlicherBedarfProJahrModel();
                meanYear10.setJahr(TITLE_MEAN_YEAR_10);
                meanYear10.setAnzahlKinderGesamt(
                    sumAnzahlKinderGesamt.divide(numberOfYear, SCALE_ROUNDING_RESULT_DECIMAL, RoundingMode.HALF_EVEN)
                );
                meanYear10.setAnzahlKinderZuVersorgen(
                    sumAnzahlKinderZuVersorgen.divide(
                        numberOfYear,
                        SCALE_ROUNDING_RESULT_DECIMAL,
                        RoundingMode.HALF_EVEN
                    )
                );
                meanYear10.setAnzahlGruppen(
                    sumAnzahlGruppen.divide(numberOfYear, SCALE_ROUNDING_RESULT_DECIMAL, RoundingMode.HALF_EVEN)
                );
                means10Year15YearAnd20Year.add(meanYear10);
            } else if (index == 14) {
                final var meanYear15 = new PlanungsursaechlicherBedarfProJahrModel();
                meanYear15.setJahr(TITLE_MEAN_YEAR_15);
                meanYear15.setAnzahlKinderGesamt(
                    sumAnzahlKinderGesamt.divide(numberOfYear, SCALE_ROUNDING_RESULT_DECIMAL, RoundingMode.HALF_EVEN)
                );
                meanYear15.setAnzahlKinderZuVersorgen(
                    sumAnzahlKinderZuVersorgen.divide(
                        numberOfYear,
                        SCALE_ROUNDING_RESULT_DECIMAL,
                        RoundingMode.HALF_EVEN
                    )
                );
                meanYear15.setAnzahlGruppen(
                    sumAnzahlGruppen.divide(numberOfYear, SCALE_ROUNDING_RESULT_DECIMAL, RoundingMode.HALF_EVEN)
                );
                means10Year15YearAnd20Year.add(meanYear15);
            } else if (index == 19) {
                final var meanYear20 = new PlanungsursaechlicherBedarfProJahrModel();
                meanYear20.setJahr(TITLE_MEAN_YEAR_20);
                meanYear20.setAnzahlKinderGesamt(
                    sumAnzahlKinderGesamt.divide(numberOfYear, SCALE_ROUNDING_RESULT_DECIMAL, RoundingMode.HALF_EVEN)
                );
                meanYear20.setAnzahlKinderZuVersorgen(
                    sumAnzahlKinderZuVersorgen.divide(
                        numberOfYear,
                        SCALE_ROUNDING_RESULT_DECIMAL,
                        RoundingMode.HALF_EVEN
                    )
                );
                meanYear20.setAnzahlGruppen(
                    sumAnzahlGruppen.divide(numberOfYear, SCALE_ROUNDING_RESULT_DECIMAL, RoundingMode.HALF_EVEN)
                );
                means10Year15YearAnd20Year.add(meanYear20);
            }
        }
        return means10Year15YearAnd20Year;
    }
}
