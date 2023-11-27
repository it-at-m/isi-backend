package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.mapper.StammdatenDomainMapper;
import de.muenchen.isi.domain.model.calculation.InfrastrukturbedarfProJahrModel;
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
public class InfrastrukturbedarfService {

    public enum ArtInfrastrukturbedarf {
        PLANUNGSURSAECHLICH,
        SOBON_URSAECHLICH,
    }

    public static final int SCALE_ROUNDING_RESULT_INTEGER = 0;

    public static final int SCALE_ROUNDING_RESULT_DECIMAL = 2;

    public static final String TITLE_MEAN_YEAR_10 = "Mittelwert 10 J.";

    public static final String TITLE_MEAN_YEAR_15 = "Mittelwert 15 J.";

    public static final String TITLE_MEAN_YEAR_20 = "Mittelwert 20 J.";

    private final SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository;

    private final VersorgungsquoteGruppenstaerkeRepository versorgungsquoteGruppenstaerkeRepository;

    private final StammdatenDomainMapper stammdatenDomainMapper;

    /**
     * Ermittlung die gerundeten Bedarfe sowie den 10-Jahres, 15-jahres und 20-Jahres-Mittelwert
     * für den Zeitraum von 20 Jahren auf Basis der gegebenen Wohneinheiten für Kinderkrippen.
     *
     * @param wohneinheiten zur Ermittlung der Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastuktur.
     * @param artInfrastrukturbedarf zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die Bedarfe für den Zeitraum von 20 Jahren
     */
    public List<InfrastrukturbedarfProJahrModel> calculateBedarfForKinderkrippeRoundedAndWithMean(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final ArtInfrastrukturbedarf artInfrastrukturbedarf,
        final LocalDate gueltigAb
    ) {
        final var roundedBedarfe =
            this.calculateBedarfForKinderkrippe(wohneinheiten, sobonJahr, artInfrastrukturbedarf, gueltigAb)
                .map(this::roundValuesAndReturnModelWithRoundedValues)
                .collect(Collectors.toList());
        final var meansForRoundedBedarfe = this.calculate10Year15YearAnd20YearMean(roundedBedarfe);
        roundedBedarfe.addAll(meansForRoundedBedarfe);
        return roundedBedarfe;
    }

    /**
     * Ermittlung die Bedarfe für den Zeitraum von 20 Jahren auf Basis der gegebenen Wohneinheiten für Kinderkrippen.
     *
     * @param wohneinheiten zur Ermittlung der Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastuktur.
     * @param artInfrastrukturbedarf zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die Bedarfe für den Zeitraum von 20 Jahren
     */
    public Stream<InfrastrukturbedarfProJahrModel> calculateBedarfForKinderkrippe(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final ArtInfrastrukturbedarf artInfrastrukturbedarf,
        final LocalDate gueltigAb
    ) {
        return this.calculateBedarf(
                InfrastruktureinrichtungTyp.KINDERKRIPPE,
                wohneinheiten,
                sobonJahr,
                artInfrastrukturbedarf,
                gueltigAb
            );
    }

    /**
     * Ermittlung die gerundeten Bedarfe sowie den 10-Jahres, 15-jahres und 20-Jahres-Mittelwert
     * für den Zeitraum von 20 Jahren auf Basis der gegebenen Wohneinheiten für Kindergärten.
     *
     * @param wohneinheiten zur Ermittlung der Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastruktur.
     * @param artInfrastrukturbedarf zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die Bedarfe für den Zeitraum von 20 Jahren
     */
    public List<InfrastrukturbedarfProJahrModel> calculateBedarfForKindergartenRoundedAndWithMean(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final ArtInfrastrukturbedarf artInfrastrukturbedarf,
        final LocalDate gueltigAb
    ) {
        final var roundedBedarfe =
            this.calculateBedarfForKindergarten(wohneinheiten, sobonJahr, artInfrastrukturbedarf, gueltigAb)
                .map(this::roundValuesAndReturnModelWithRoundedValues)
                .collect(Collectors.toList());
        final var meansForRoundedBedarfe = this.calculate10Year15YearAnd20YearMean(roundedBedarfe);
        roundedBedarfe.addAll(meansForRoundedBedarfe);
        return roundedBedarfe;
    }

    /**
     * Ermittlung die Bedarfe für den Zeitraum von 20 Jahren auf Basis der gegebenen Wohneinheiten für Kindergärten.
     *
     * @param wohneinheiten zur Ermittlung der Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastuktur.
     * @param artInfrastrukturbedarf zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die Bedarfe für den Zeitraum von 20 Jahren
     */
    public Stream<InfrastrukturbedarfProJahrModel> calculateBedarfForKindergarten(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final ArtInfrastrukturbedarf artInfrastrukturbedarf,
        final LocalDate gueltigAb
    ) {
        return this.calculateBedarf(
                InfrastruktureinrichtungTyp.KINDERGARTEN,
                wohneinheiten,
                sobonJahr,
                artInfrastrukturbedarf,
                gueltigAb
            );
    }

    /**
     * Ermittlung die Bedarfe für den Zeitraum von 20 Jahren für die im Parameter gegebene Einrichtung auf Basis der gegebenen Wohneinheiten.
     *
     * @param einrichtung zur Ermittlung der Bedarfe.
     * @param wohneinheiten zur Ermittlung der Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastuktur.
     * @param artInfrastrukturbedarf zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die Bedarfe für den Zeitraum von 20 Jahren
     */
    protected Stream<InfrastrukturbedarfProJahrModel> calculateBedarf(
        final InfrastruktureinrichtungTyp einrichtung,
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final ArtInfrastrukturbedarf artInfrastrukturbedarf,
        final LocalDate gueltigAb
    ) {
        final var wohneinheitenWithoutSum = wohneinheiten
            .stream()
            .filter(wohneinheitenProJahr -> NumberUtils.isParsable(wohneinheitenProJahr.getJahr()))
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

        final var earliestYear = wohneinheitenWithoutSum
            .stream()
            .map(WohneinheitenProFoerderartProJahrModel::getJahr)
            .mapToInt(Integer::parseInt)
            .min()
            .getAsInt();

        // Berechnung Gesamtanzahl der Kinder je Jahr
        return wohneinheitenWithoutSum
            .stream()
            .flatMap(wohneinheitenProJahr ->
                calculateBedarfeForWohneinheitProJahr(
                    earliestYear,
                    wohneinheitenProJahr,
                    sobonOrientierungswertForFoerderart.get(wohneinheitenProJahr.getFoerderart())
                )
            )
            // Gruppieren der Bedarfe je Jahr
            .collect(
                Collectors.groupingBy(
                    InfrastrukturbedarfProJahrModel::getJahr,
                    Collectors.mapping(Function.identity(), Collectors.toList())
                )
            )
            .values()
            .stream()
            // Bilden der Jahressumme der vorher gruppierten Bedarfe.
            .map(bedarfeForJahr -> bedarfeForJahr.stream().reduce(new InfrastrukturbedarfProJahrModel(), this::add))
            .sorted(Comparator.comparing(InfrastrukturbedarfProJahrModel::getJahr))
            // Ermitteln der Versorgungsquote und Gruppenstärke
            .map(bedarf ->
                this.setVersorgungsquoteAndGruppenstaerkeInBedarf(
                        bedarf,
                        versorgungsquoteGruppenstaerke,
                        artInfrastrukturbedarf
                    )
            );
    }

    /**
     * Ermittelt auf Basis des Bedarfs und der Versorgungsquote und Gruppenstärke die Anzahl an zu versorgenden Kinder
     * sowie die Gruppenstärke für die zu versorgenden Kinder und setzt die ermittelten Werte im gegebenen Bedarfsobjekt.
     *
     * @param bedarf zur Ermittlung.
     * @param versorgungsquoteGruppenstaerke zur Ermittlung.
     * @param artInfrastrukturbedarf zur Ermittlung der korrekten Versorgungsquote.
     * @return den um die zu versorgenden Kinder und die Anzahl der Gruppen angereicherten Bedarf.
     */
    protected InfrastrukturbedarfProJahrModel setVersorgungsquoteAndGruppenstaerkeInBedarf(
        final InfrastrukturbedarfProJahrModel bedarf,
        final VersorgungsquoteGruppenstaerke versorgungsquoteGruppenstaerke,
        final ArtInfrastrukturbedarf artInfrastrukturbedarf
    ) {
        final var anzahlKinderGesamt = bedarf.getAnzahlKinderGesamt();
        final var versorgungsquote = ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH.equals(artInfrastrukturbedarf)
            ? versorgungsquoteGruppenstaerke.getVersorgungsquotePlanungsursaechlich()
            : versorgungsquoteGruppenstaerke.getVersorgungsquoteSobonUrsaechlich();
        final var anzahlKinderZuVersorgen = anzahlKinderGesamt.multiply(versorgungsquote);
        final var anzahlGruppen = anzahlKinderZuVersorgen.divide(
            BigDecimal.valueOf(versorgungsquoteGruppenstaerke.getGruppenstaerke()),
            SCALE_ROUNDING_RESULT_DECIMAL,
            RoundingMode.HALF_EVEN
        );
        bedarf.setAnzahlKinderZuVersorgen(anzahlKinderZuVersorgen);
        bedarf.setAnzahlGruppen(anzahlGruppen);
        return bedarf;
    }

    /**
     * Rundet die Werte für den im Parameter gegebenen Bedarf und gibt den Bedarf mit den gerundeten Werten zurück.
     *
     * - {@link InfrastrukturbedarfProJahrModel#getAnzahlKinderGesamt()} runden auf {@link this#SCALE_ROUNDING_RESULT_INTEGER}.
     * - {@link InfrastrukturbedarfProJahrModel#getAnzahlKinderZuVersorgen()} ()} runden auf {@link this#SCALE_ROUNDING_RESULT_INTEGER}.
     * - {@link InfrastrukturbedarfProJahrModel#getAnzahlGruppen()} runden auf {@link this#SCALE_ROUNDING_RESULT_DECIMAL}.
     *
     * @param bedarf zum Runden der Werte.
     * @return den Bedarf mit gerundeten Werten.
     */
    protected InfrastrukturbedarfProJahrModel roundValuesAndReturnModelWithRoundedValues(
        final InfrastrukturbedarfProJahrModel bedarf
    ) {
        final var anzahlKinderGesamtRounded = bedarf
            .getAnzahlKinderGesamt()
            .setScale(SCALE_ROUNDING_RESULT_INTEGER, RoundingMode.HALF_EVEN);
        final var anzahlKinderZuVersorgenRounded = bedarf
            .getAnzahlKinderZuVersorgen()
            .setScale(SCALE_ROUNDING_RESULT_INTEGER, RoundingMode.HALF_EVEN);
        final var anzahlGruppenRounded = bedarf
            .getAnzahlGruppen()
            .setScale(SCALE_ROUNDING_RESULT_DECIMAL, RoundingMode.HALF_EVEN);
        bedarf.setAnzahlKinderGesamt(anzahlKinderGesamtRounded);
        bedarf.setAnzahlKinderZuVersorgen(anzahlKinderZuVersorgenRounded);
        bedarf.setAnzahlGruppen(anzahlGruppenRounded);
        return bedarf;
    }

    /**
     * Summiert die Gesamtanzahl der Kinder der in den Parameter gegebenen Bedarfe.
     *
     * Als relevantes Jahr wird das Jahr der Bedarfe im Parameter o1 gesetzt, falls diese vorhanden ist.
     * Ansonsten wird das jahr des Parameter o2 verwendet.
     *
     * @param o1 zum Summieren.
     * @param o2 zum Summieren.
     * @return den Bedarf mit der summierten Gesamtanzahl der Kinder
     */
    protected InfrastrukturbedarfProJahrModel add(
        final InfrastrukturbedarfProJahrModel o1,
        final InfrastrukturbedarfProJahrModel o2
    ) {
        final var bedarf = new InfrastrukturbedarfProJahrModel();
        final var jahr = ObjectUtils.defaultIfNull(o1.getJahr(), o2.getJahr());
        final var sumAnzahlKinderGesamt = ObjectUtils
            .defaultIfNull(o1.getAnzahlKinderGesamt(), BigDecimal.ZERO)
            .add(ObjectUtils.defaultIfNull(o2.getAnzahlKinderGesamt(), BigDecimal.ZERO));
        bedarf.setJahr(jahr);
        bedarf.setAnzahlKinderGesamt(sumAnzahlKinderGesamt);
        return bedarf;
    }

    /**
     * Ermittelt je Förderart in den gegebenen Wohneinheiten die Sobon-Orientierungswerte zur sozialen Infrastruktur.
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
     * Ermittelt für die im Parameter gegebenen Wohneinheiten die Bedarfe
     * auf Basis der im Paramter Sobon-Orientierungswerte der sozialen Infrastruktur.
     *
     * Entspricht das Jahr der Wohneinheiten dem frühesten Jahr, so werden Bedarfe für 20 Jahre ermittelt.
     * Jedes weitere Jahr der Wohneinheiten reduziert die Anzahl der Bedarfe jeweils um ein Jahr.
     *
     * @param earliestYear zur Ermittlung der notwendigen Anzahl an jährlichen Bedarfe.
     * @param wohneinheiten zur Ermittlung der Bedarfe.
     * @param sobonOrientierungswertSozialeInfrastruktur zur Ermittlung der Bedarfe.
     * @return die Bedarfe der gegebenen Wohneinheiten unter Berücksichtung des ersten Jahres.
     */
    protected Stream<InfrastrukturbedarfProJahrModel> calculateBedarfeForWohneinheitProJahr(
        final Integer earliestYear,
        final WohneinheitenProFoerderartProJahrModel wohneinheiten,
        final SobonOrientierungswertSozialeInfrastrukturModel sobonOrientierungswertSozialeInfrastruktur
    ) {
        final var bedarfe = new ArrayList<InfrastrukturbedarfProJahrModel>();
        final var numberOfYearsToCalculate = 20 - (Integer.parseInt(wohneinheiten.getJahr()) - earliestYear);
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
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
            bedarfe.add(
                createInfrastrukturbedarf(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate,
                    anzahlKinderGesamt
                )
            );
        }
        return bedarfe.stream();
    }

    /**
     * Erstellt ein {@link InfrastrukturbedarfProJahrModel} mit den im Parameter gegebenen Werten.
     *
     * @param jahr des Bedarfs.
     * @param anzahlKinderGesamt des Bedarfs.
     * @return den Bedarf mit gesetzten Jahr und der Gesamtanzahl an Kinder.
     */
    protected InfrastrukturbedarfProJahrModel createInfrastrukturbedarf(
        final Integer jahr,
        final BigDecimal anzahlKinderGesamt
    ) {
        final var bedarf = new InfrastrukturbedarfProJahrModel();
        bedarf.setJahr(Integer.toString(jahr));
        bedarf.setAnzahlKinderGesamt(anzahlKinderGesamt);
        return bedarf;
    }

    /**
     * Die Methode ermittelt den 10-Jahres-, 15-Jahres- und 20-Jahres-Mittelwert für die im Parameter gegebenen Liste
     * an Infrastrukturbedarfen.
     *
     * @param bedarfe zur Ermittlung der 10-Jahres-, 15-Jahres- und 20-Jahres-Mittelwerte
     * @return den 10-Jahres-, 15-Jahres- und 20-Jahres-Mittelwert als Liste.
     */
    protected List<InfrastrukturbedarfProJahrModel> calculate10Year15YearAnd20YearMean(
        final List<InfrastrukturbedarfProJahrModel> bedarfe
    ) {
        final var means10Year15YearAnd20Year = new ArrayList<InfrastrukturbedarfProJahrModel>(3);
        var sumAnzahlKinderGesamt = BigDecimal.ZERO;
        BigDecimal sumAnzahlKinderZuVersorgen = BigDecimal.ZERO;
        BigDecimal sumAnzahlGruppen = BigDecimal.ZERO;
        BigDecimal numberOfYear;
        for (int index = 0; index < bedarfe.size(); index++) {
            numberOfYear = BigDecimal.valueOf(index + 1);
            sumAnzahlKinderGesamt = bedarfe.get(index).getAnzahlKinderGesamt().add(sumAnzahlKinderGesamt);
            sumAnzahlKinderZuVersorgen =
                bedarfe.get(index).getAnzahlKinderZuVersorgen().add(sumAnzahlKinderZuVersorgen);
            sumAnzahlGruppen = bedarfe.get(index).getAnzahlGruppen().add(sumAnzahlGruppen);
            if (index == 9) {
                final var meanYear10 = new InfrastrukturbedarfProJahrModel();
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
                final var meanYear15 = new InfrastrukturbedarfProJahrModel();
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
                final var meanYear20 = new InfrastrukturbedarfProJahrModel();
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
