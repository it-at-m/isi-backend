package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapper;
import de.muenchen.isi.domain.model.calculation.InfrastrukturbedarfProJahrModel;
import de.muenchen.isi.domain.model.calculation.PersonenProJahrModel;
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
import java.util.NoSuchElementException;
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

    public static final String TITLE_MEAN_WITH_PLACEHOLDER = "Mittelwert %d J.";

    private final SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository;

    private final VersorgungsquoteGruppenstaerkeRepository versorgungsquoteGruppenstaerkeRepository;

    private final StammdatenDomainMapper stammdatenDomainMapper;

    /**
     * Ermittlung die gerundeten Bedarfe für den Zeitraum von 20 Jahren auf Basis der gegebenen Wohneinheiten für Kinderkrippen.
     *
     * @param wohneinheiten zur Ermittlung der Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastruktur.
     * @param artInfrastrukturbedarf zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die Bedarfe für den Zeitraum von 20 Jahren
     * @throws CalculationException falls die Stammdaten zur Durchführung der Berechnung nicht geladen werden können.
     */
    public List<InfrastrukturbedarfProJahrModel> calculateBedarfForKinderkrippeRounded(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final ArtInfrastrukturbedarf artInfrastrukturbedarf,
        final LocalDate gueltigAb
    ) throws CalculationException {
        return this.calculateBedarfForKinderkrippe(wohneinheiten, sobonJahr, artInfrastrukturbedarf, gueltigAb)
            .map(this::roundValuesAndReturnModelWithRoundedValues)
            .collect(Collectors.toList());
    }

    /**
     * Ermittlung die Bedarfe für den Zeitraum von 20 Jahren auf Basis der gegebenen Wohneinheiten für Kinderkrippen.
     *
     * @param wohneinheiten zur Ermittlung der Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastruktur.
     * @param artInfrastrukturbedarf zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die Bedarfe für den Zeitraum von 20 Jahren
     * @throws CalculationException falls die Stammdaten zur Durchführung der Berechnung nicht geladen werden können.
     */
    public Stream<InfrastrukturbedarfProJahrModel> calculateBedarfForKinderkrippe(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final ArtInfrastrukturbedarf artInfrastrukturbedarf,
        final LocalDate gueltigAb
    ) throws CalculationException {
        // Ermittlung der Versorgungsquote und Gruppenstärke für die Einrichtung.
        final var versorgungsquoteGruppenstaerke = versorgungsquoteGruppenstaerkeRepository
            .findFirstByInfrastruktureinrichtungTypAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                InfrastruktureinrichtungTyp.KINDERKRIPPE,
                gueltigAb
            )
            .orElseThrow(() -> {
                final var message =
                    "Für die planungsursächliche Bedarfsberechnung der Kinderkrippen konnten die Stammdaten zur Versorgungsquote und Gruppenstärke nicht geladen werden.";
                log.error(message);
                return new CalculationException(message);
            });

        return this.calculatePersonen(InfrastruktureinrichtungTyp.KINDERKRIPPE, wohneinheiten, sobonJahr)
            // Ermitteln der Versorgungsquote und Gruppenstärke
            .map(bedarf ->
                this.getVersorgungsquoteAndGruppenstaerkeWithBedarf(
                        bedarf,
                        versorgungsquoteGruppenstaerke,
                        artInfrastrukturbedarf
                    )
            );
    }

    /**
     * Ermittlung die gerundeten Bedarfe für den Zeitraum von 20 Jahren auf Basis der gegebenen Wohneinheiten für Kindergärten.
     *
     * @param wohneinheiten zur Ermittlung der Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastruktur.
     * @param artInfrastrukturbedarf zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die Bedarfe für den Zeitraum von 20 Jahren
     * @throws CalculationException falls die Stammdaten zur Durchführung der Berechnung nicht geladen werden können.
     */
    public List<InfrastrukturbedarfProJahrModel> calculateBedarfForKindergartenRounded(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final ArtInfrastrukturbedarf artInfrastrukturbedarf,
        final LocalDate gueltigAb
    ) throws CalculationException {
        return this.calculateBedarfForKindergarten(wohneinheiten, sobonJahr, artInfrastrukturbedarf, gueltigAb)
            .map(this::roundValuesAndReturnModelWithRoundedValues)
            .collect(Collectors.toList());
    }

    /**
     * Ermittlung die Bedarfe für den Zeitraum von 20 Jahren auf Basis der gegebenen Wohneinheiten für Kindergärten.
     *
     * @param wohneinheiten zur Ermittlung der Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastruktur.
     * @param artInfrastrukturbedarf zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die Bedarfe für den Zeitraum von 20 Jahren
     * @throws CalculationException falls die Stammdaten zur Durchführung der Berechnung nicht geladen werden können.
     */
    public Stream<InfrastrukturbedarfProJahrModel> calculateBedarfForKindergarten(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final ArtInfrastrukturbedarf artInfrastrukturbedarf,
        final LocalDate gueltigAb
    ) throws CalculationException {
        // Ermittlung der Versorgungsquote und Gruppenstärke für die Einrichtung.
        final var versorgungsquoteGruppenstaerke = versorgungsquoteGruppenstaerkeRepository
            .findFirstByInfrastruktureinrichtungTypAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                InfrastruktureinrichtungTyp.KINDERGARTEN,
                gueltigAb
            )
            .orElseThrow(() -> {
                final var message =
                    "Für die planungsursächliche Bedarfsberechnung der Kindergärten konnten die Stammdaten zur Versorgungsquote und Gruppenstärke nicht geladen werden.";
                log.error(message);
                return new CalculationException(message);
            });

        return this.calculatePersonen(InfrastruktureinrichtungTyp.KINDERGARTEN, wohneinheiten, sobonJahr)
            // Ermitteln der Versorgungsquote und Gruppenstärke
            .map(bedarf ->
                this.getVersorgungsquoteAndGruppenstaerkeWithBedarf(
                        bedarf,
                        versorgungsquoteGruppenstaerke,
                        artInfrastrukturbedarf
                    )
            );
    }

    /**
     * Ermittlung aller Einwohner für den Zeitraum von 20 Jahren auf Basis der gegebenen Wohneinheiten.
     *
     * @param wohneinheiten zur Ermittlung der Personen.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastruktur.
     * @return die Personen für den Zeitraum von 20 Jahren
     * @throws CalculationException falls die Stammdaten zur Durchführung der Berechnung nicht geladen werden können.
     */
    public List<PersonenProJahrModel> calculateAlleEinwohnerRounded(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr
    ) throws CalculationException {
        return this.calculateAlleEinwohner(wohneinheiten, sobonJahr)
            .map(this::roundValuesAndReturnModelWithRoundedValues)
            .collect(Collectors.toList());
    }

    /**
     * Ermittlung aller Einwohner für den Zeitraum von 20 Jahren auf Basis der gegebenen Wohneinheiten.
     *
     * @param wohneinheiten zur Ermittlung der Personen.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastruktur.
     * @return die Personen für den Zeitraum von 20 Jahren
     * @throws CalculationException falls die Stammdaten zur Durchführung der Berechnung nicht geladen werden können.
     */
    public Stream<PersonenProJahrModel> calculateAlleEinwohner(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr
    ) throws CalculationException {
        return this.calculatePersonen(InfrastruktureinrichtungTyp.UNSPECIFIED, wohneinheiten, sobonJahr);
    }

    /**
     * Ermittlung die Personen für den Zeitraum von 20 Jahren für die im Parameter gegebene Einrichtung auf Basis der gegebenen Wohneinheiten.
     *
     * @param einrichtung zur Ermittlung der Personen.
     * @param wohneinheiten zur Ermittlung der Personen.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastuktur.
     * @return die Bedarfe für den Zeitraum von 20 Jahren
     * @throws CalculationException falls die Stammdaten zur Durchführung der Berechnung nicht geladen werden können.
     */
    protected Stream<PersonenProJahrModel> calculatePersonen(
        final InfrastruktureinrichtungTyp einrichtung,
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr
    ) throws CalculationException {
        final var wohneinheitenWithoutSum = wohneinheiten
            .stream()
            .filter(wohneinheitenProJahr -> NumberUtils.isParsable(wohneinheitenProJahr.getJahr()))
            .collect(Collectors.toList());

        // Ermittlung und Gruppierung der SoBon-Orientierungswerte nach Förderart
        final var sobonOrientierungswertForFoerderart =
            this.getSobonOrientierungswertGroupedByFoerderart(wohneinheiten, sobonJahr, einrichtung);

        final var earliestYear = wohneinheitenWithoutSum
            .stream()
            .map(WohneinheitenProFoerderartProJahrModel::getJahr)
            .mapToInt(Integer::parseInt)
            .min()
            .getAsInt();

        // Berechnung Gesamtanzahl der Personen je Jahr
        return wohneinheitenWithoutSum
            .stream()
            .flatMap(wohneinheitenProJahr ->
                calculatePersonenForWohneinheitProJahr(
                    earliestYear,
                    wohneinheitenProJahr,
                    sobonOrientierungswertForFoerderart.get(wohneinheitenProJahr.getFoerderart())
                )
            )
            // Gruppieren der Bedarfe je Jahr
            .collect(
                Collectors.groupingBy(
                    PersonenProJahrModel::getJahr,
                    Collectors.mapping(Function.identity(), Collectors.toList())
                )
            )
            .values()
            .stream()
            // Bilden der Jahressumme der vorher gruppierten Personen.
            .map(bedarfeForJahr -> bedarfeForJahr.stream().reduce(new PersonenProJahrModel(), this::add))
            .sorted(Comparator.comparing(PersonenProJahrModel::getJahr));
    }

    /**
     * Ermittelt auf Basis des Bedarfs und der Versorgungsquote und Gruppenstärke die Anzahl an zu versorgenden Personen
     * sowie die Gruppenstärke für die zu versorgenden Personen und gibt diese Informationen samt den
     * übergebenen Bedarfsinformationen zurück.
     *
     * @param bedarf zur Ermittlung.
     * @param versorgungsquoteGruppenstaerke zur Ermittlung.
     * @param artInfrastrukturbedarf zur Ermittlung der korrekten Versorgungsquote.
     * @return den um die zu versorgenden Personen und die Anzahl der Gruppen angereicherten Bedarf.
     */
    protected InfrastrukturbedarfProJahrModel getVersorgungsquoteAndGruppenstaerkeWithBedarf(
        final PersonenProJahrModel bedarf,
        final VersorgungsquoteGruppenstaerke versorgungsquoteGruppenstaerke,
        final ArtInfrastrukturbedarf artInfrastrukturbedarf
    ) {
        final var anzahlPersonenGesamt = bedarf.getAnzahlPersonenGesamt();
        final var versorgungsquote = ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH.equals(artInfrastrukturbedarf)
            ? versorgungsquoteGruppenstaerke.getVersorgungsquotePlanungsursaechlich()
            : versorgungsquoteGruppenstaerke.getVersorgungsquoteSobonUrsaechlich();
        final var anzahlPersonenZuVersorgen = anzahlPersonenGesamt
            .multiply(versorgungsquote)
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
        final var anzahlGruppen = anzahlPersonenZuVersorgen.divide(
            BigDecimal.valueOf(versorgungsquoteGruppenstaerke.getGruppenstaerke()),
            CalculationService.DIVISION_SCALE,
            RoundingMode.HALF_UP
        );
        final var bedarfMitVersorgungsquoteAndGruppen = new InfrastrukturbedarfProJahrModel();
        bedarfMitVersorgungsquoteAndGruppen.setJahr(bedarf.getJahr());
        bedarfMitVersorgungsquoteAndGruppen.setAnzahlPersonenGesamt(bedarf.getAnzahlPersonenGesamt());
        bedarfMitVersorgungsquoteAndGruppen.setAnzahlPersonenZuVersorgen(anzahlPersonenZuVersorgen);
        bedarfMitVersorgungsquoteAndGruppen.setAnzahlGruppen(anzahlGruppen);
        return bedarfMitVersorgungsquoteAndGruppen;
    }

    /**
     * Rundet die Werte für den im Parameter gegebenen Bedarf und gibt den Bedarf mit den gerundeten Werten zurück.
     *
     * - {@link InfrastrukturbedarfProJahrModel#getAnzahlPersonenGesamt()} runden auf {@link this#SCALE_ROUNDING_RESULT_INTEGER}.
     * - {@link InfrastrukturbedarfProJahrModel#getAnzahlPersonenZuVersorgen()} ()} runden auf {@link this#SCALE_ROUNDING_RESULT_INTEGER}.
     * - {@link InfrastrukturbedarfProJahrModel#getAnzahlGruppen()} runden auf {@link this#SCALE_ROUNDING_RESULT_DECIMAL}.
     *
     * @param bedarf zum Runden der Werte.
     * @return den Bedarf mit gerundeten Werten.
     */
    protected InfrastrukturbedarfProJahrModel roundValuesAndReturnModelWithRoundedValues(
        final InfrastrukturbedarfProJahrModel bedarf
    ) {
        final var anzahlPersonenGesamtRounded = bedarf
            .getAnzahlPersonenGesamt()
            .setScale(SCALE_ROUNDING_RESULT_INTEGER, RoundingMode.HALF_UP);
        final var anzahlPersonenZuVersorgenRounded = bedarf
            .getAnzahlPersonenZuVersorgen()
            .setScale(SCALE_ROUNDING_RESULT_INTEGER, RoundingMode.HALF_UP);
        final var anzahlGruppenRounded = bedarf
            .getAnzahlGruppen()
            .setScale(SCALE_ROUNDING_RESULT_DECIMAL, RoundingMode.HALF_UP);
        bedarf.setAnzahlPersonenGesamt(anzahlPersonenGesamtRounded);
        bedarf.setAnzahlPersonenZuVersorgen(anzahlPersonenZuVersorgenRounded);
        bedarf.setAnzahlGruppen(anzahlGruppenRounded);
        return bedarf;
    }

    /**
     * Rundet die Werte für die im Parameter gegebenen Personen und gibt das Objekt mit den gerundeten Werten zurück.
     *
     * - {@link InfrastrukturbedarfProJahrModel#getAnzahlPersonenGesamt()} runden auf {@link this#SCALE_ROUNDING_RESULT_INTEGER}.
     *
     * @param personen zum Runden der Werte.
     * @return das Objekt mit gerundeten Werten.
     */
    protected PersonenProJahrModel roundValuesAndReturnModelWithRoundedValues(final PersonenProJahrModel personen) {
        final var anzahlPersonenGesamtRounded = personen
            .getAnzahlPersonenGesamt()
            .setScale(SCALE_ROUNDING_RESULT_INTEGER, RoundingMode.HALF_UP);
        personen.setAnzahlPersonenGesamt(anzahlPersonenGesamtRounded);
        return personen;
    }

    /**
     * Summiert die Gesamtanzahl der Personen der in den Parameter gegebenen Bedarfe.
     *
     * Als relevantes Jahr wird das Jahr der Bedarfe im Parameter o1 gesetzt, falls diese vorhanden ist.
     * Ansonsten wird das jahr des Parameter o2 verwendet.
     *
     * @param o1 zum Summieren.
     * @param o2 zum Summieren.
     * @return den Bedarf mit der summierten Gesamtanzahl der Personen
     */
    protected PersonenProJahrModel add(final PersonenProJahrModel o1, final PersonenProJahrModel o2) {
        final var bedarf = new PersonenProJahrModel();
        final var jahr = ObjectUtils.defaultIfNull(o1.getJahr(), o2.getJahr());
        final var sumAnzahlPersonenGesamt = ObjectUtils
            .defaultIfNull(o1.getAnzahlPersonenGesamt(), BigDecimal.ZERO)
            .add(ObjectUtils.defaultIfNull(o2.getAnzahlPersonenGesamt(), BigDecimal.ZERO));
        bedarf.setJahr(jahr);
        bedarf.setAnzahlPersonenGesamt(sumAnzahlPersonenGesamt);
        return bedarf;
    }

    /**
     * Ermittelt je Förderart in den gegebenen Wohneinheiten die Sobon-Orientierungswerte zur sozialen Infrastruktur.
     *
     * @param wohneinheiten mit Förderarten zur Ermittlung der Sobon-Orientierungswerte zur sozialen Infrastruktur je Förderart.
     * @param sobonJahr zur Extraktion der korrekten Sobon-Orientierungswerte zur sozialen Infrastruktur je Förderart.
     * @param einrichtungstyp zur Extraktion der korrekten Sobon-Orientierungswerte zur sozialen Infrastruktur je Förderart.
     * @return die Sobon-Orientierungswerte zur sozialen Infrastruktur gruppiert nach Förderart.
     * @throws CalculationException falls die Stammdaten zur Durchführung der Berechnung nicht geladen werden können.
     */
    protected Map<String, SobonOrientierungswertSozialeInfrastrukturModel> getSobonOrientierungswertGroupedByFoerderart(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final InfrastruktureinrichtungTyp einrichtungstyp
    ) throws CalculationException {
        try {
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
                .map(Optional::get)
                .map(stammdatenDomainMapper::entity2Model)
                .collect(
                    Collectors.toMap(
                        SobonOrientierungswertSozialeInfrastrukturModel::getFoerderartBezeichnung,
                        Function.identity()
                    )
                );
        } catch (final NoSuchElementException exception) {
            final var message =
                "Für die planungsursächliche Bedarfsberechnung konnten die Stammdaten zu den Sobon-Orientierungswerten zur sozialen Infrastruktur nicht geladen werden.";
            log.error(message);
            throw new CalculationException(message, exception);
        }
    }

    /**
     * Ermittelt für die im Parameter gegebenen Wohneinheiten die Personen
     * auf Basis der im Paramter Sobon-Orientierungswerte der sozialen Infrastruktur.
     *
     * Entspricht das Jahr der Wohneinheiten dem frühesten Jahr, so werden Bedarfe für 20 Jahre ermittelt.
     * Jedes weitere Jahr der Wohneinheiten reduziert die Anzahl der Personen jeweils um ein Jahr.
     *
     * @param earliestYear zur Ermittlung der notwendigen Anzahl an jährlichen Personen.
     * @param wohneinheiten zur Ermittlung der Personen.
     * @param sobonOrientierungswertSozialeInfrastruktur zur Ermittlung der Personen.
     * @return die Personen der gegebenen Wohneinheiten unter Berücksichtung des ersten Jahres.
     */
    protected Stream<PersonenProJahrModel> calculatePersonenForWohneinheitProJahr(
        final Integer earliestYear,
        final WohneinheitenProFoerderartProJahrModel wohneinheiten,
        final SobonOrientierungswertSozialeInfrastrukturModel sobonOrientierungswertSozialeInfrastruktur
    ) {
        final var personen = new ArrayList<PersonenProJahrModel>();
        final var numberOfYearsToCalculate = 20 - (Integer.parseInt(wohneinheiten.getJahr()) - earliestYear);
        var yearToCalculate = 0;
        PersonenProJahrModel anzahlPersonen;
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr1NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr2NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr3NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr4NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr5NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr6NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr7NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr8NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr9NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr10NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr11NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr12NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr13NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr14NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr15NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr16NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr17NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr18NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr19NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
            yearToCalculate++;
        }
        if (yearToCalculate < numberOfYearsToCalculate) {
            anzahlPersonen =
                calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
                    wohneinheiten,
                    sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr20NachErsterstellung(),
                    yearToCalculate
                );
            personen.add(anzahlPersonen);
        }
        return personen.stream();
    }

    /**
     * Die Methode ermittelt auf Basis der gegebenen Parameter die Personen welche für das gegebene Jahr in den Wohneinheiten leben.
     *
     * @param wohneinheiten für die Berechnung.
     * @param obererRichtwert für die Berechnung.
     * @param yearToCalculate für die Berechnung.
     * @return Personen welche für das gegebene Jahr in den Wohneinheiten leben.
     */
    protected PersonenProJahrModel calculatePersonenWithObererRichtwertAndWohneinheitenForCertainYear(
        final WohneinheitenProFoerderartProJahrModel wohneinheiten,
        final BigDecimal obererRichtwert,
        final int yearToCalculate
    ) {
        final var anzahlPersonenGesamt = wohneinheiten
            .getWohneinheiten()
            .multiply(obererRichtwert)
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
        return createPersonenProJahr(Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate, anzahlPersonenGesamt);
    }

    /**
     * Erstellt ein {@link InfrastrukturbedarfProJahrModel} mit den im Parameter gegebenen Werten.
     *
     * @param jahr der Personen.
     * @param anzahlPersonenGesamt der Personen.
     * @return die Personen mit gesetzten Jahr und der Gesamtanzahl an Personen.
     */
    protected PersonenProJahrModel createPersonenProJahr(final Integer jahr, final BigDecimal anzahlPersonenGesamt) {
        final var bedarf = new PersonenProJahrModel();
        bedarf.setJahr(Integer.toString(jahr));
        bedarf.setAnzahlPersonenGesamt(anzahlPersonenGesamt);
        return bedarf;
    }

    /**
     * Die Methode ermittelt den für die im Parameter gegebenen Liste an Infrastrukturbedarfen für die zweiten Parameter
     * gegebene Anzahl an in der Liste befindlichen Objekten.
     *
     * @param bedarfe zur Ermittlung des Mittelwerts.
     * @param numberOfBedarfeProJahrForMean als Anzahl der Objekte für welche der Mittelwert errechnet werden soll.
     * @return den Mittelwert.
     */
    protected InfrastrukturbedarfProJahrModel calculateMeanInfrastrukturbedarfe(
        final List<InfrastrukturbedarfProJahrModel> bedarfe,
        final int numberOfBedarfeProJahrForMean
    ) {
        BigDecimal sumAnzahlPersonenGesamt = BigDecimal.ZERO;
        BigDecimal sumAnzahlPersonenZuVersorgen = BigDecimal.ZERO;
        BigDecimal sumAnzahlGruppen = BigDecimal.ZERO;
        BigDecimal numberOfYear;
        for (int index = 0; index < bedarfe.size(); index++) {
            numberOfYear = BigDecimal.valueOf(index + 1);
            sumAnzahlPersonenGesamt = bedarfe.get(index).getAnzahlPersonenGesamt().add(sumAnzahlPersonenGesamt);
            sumAnzahlPersonenZuVersorgen =
                bedarfe.get(index).getAnzahlPersonenZuVersorgen().add(sumAnzahlPersonenZuVersorgen);
            sumAnzahlGruppen = bedarfe.get(index).getAnzahlGruppen().add(sumAnzahlGruppen);
            if (index == numberOfBedarfeProJahrForMean - 1) {
                final var mean = new InfrastrukturbedarfProJahrModel();
                mean.setJahr(String.format(TITLE_MEAN_WITH_PLACEHOLDER, numberOfBedarfeProJahrForMean));
                mean.setAnzahlPersonenGesamt(
                    sumAnzahlPersonenGesamt.divide(numberOfYear, SCALE_ROUNDING_RESULT_DECIMAL, RoundingMode.HALF_UP)
                );
                mean.setAnzahlPersonenZuVersorgen(
                    sumAnzahlPersonenZuVersorgen.divide(
                        numberOfYear,
                        SCALE_ROUNDING_RESULT_DECIMAL,
                        RoundingMode.HALF_UP
                    )
                );
                mean.setAnzahlGruppen(
                    sumAnzahlGruppen.divide(numberOfYear, SCALE_ROUNDING_RESULT_DECIMAL, RoundingMode.HALF_UP)
                );
                return mean;
            }
        }
        return null;
    }

    /**
     * Die Methode ermittelt den für die im Parameter gegebenen Liste an Personen für die zweiten Parameter
     * gegebene Anzahl an in der Liste befindlichen Objekten.
     *
     * @param personen zur Ermittlung des Mittelwerts.
     * @param numberOfPersonenProJahrForMean als Anzahl der Objekte für welche der Mittelwert errechnet werden soll.
     * @return den Mittelwert.
     */
    protected PersonenProJahrModel calculateMeanPersonen(
        final List<PersonenProJahrModel> personen,
        final int numberOfPersonenProJahrForMean
    ) {
        BigDecimal sumAnzahlPersonenGesamt = BigDecimal.ZERO;
        BigDecimal numberOfYear;
        for (int index = 0; index < personen.size(); index++) {
            numberOfYear = BigDecimal.valueOf(index + 1);
            sumAnzahlPersonenGesamt = personen.get(index).getAnzahlPersonenGesamt().add(sumAnzahlPersonenGesamt);
            if (index == numberOfPersonenProJahrForMean - 1) {
                final var mean = new PersonenProJahrModel();
                mean.setJahr(String.format(TITLE_MEAN_WITH_PLACEHOLDER, numberOfPersonenProJahrForMean));
                mean.setAnzahlPersonenGesamt(
                    sumAnzahlPersonenGesamt.divide(numberOfYear, SCALE_ROUNDING_RESULT_DECIMAL, RoundingMode.HALF_UP)
                );
                return mean;
            }
        }
        return null;
    }
}
