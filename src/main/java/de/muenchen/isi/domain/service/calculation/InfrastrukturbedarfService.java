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
import java.util.HashMap;
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

    private final SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository;

    private final VersorgungsquoteGruppenstaerkeRepository versorgungsquoteGruppenstaerkeRepository;

    private final StammdatenDomainMapper stammdatenDomainMapper;

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
    public List<InfrastrukturbedarfProJahrModel> calculateBedarfForKinderkrippe(
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
                    "Für die Bedarfsberechnung der Kinderkrippen konnten die Stammdaten zur Versorgungsquote und Gruppenstärke nicht geladen werden.";
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
            )
            .toList();
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
    public List<InfrastrukturbedarfProJahrModel> calculateBedarfForKindergarten(
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
                    "Für die Bedarfsberechnung der Kindergärten konnten die Stammdaten zur Versorgungsquote und Gruppenstärke nicht geladen werden.";
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
            )
            .toList();
    }

    /**
     * Ermittlung die Bedarfe für den Zeitraum von 20 Jahren auf Basis der gegebenen Wohneinheiten für GS-Nachmittagsbetreuungen.
     * Erfolgt nur für SoBoN-ursächliche Berechnungen.
     *
     * @param wohneinheiten zur Ermittlung der Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastruktur.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die Bedarfe für den Zeitraum von 20 Jahren
     * @throws CalculationException falls die Stammdaten zur Durchführung der Berechnung nicht geladen werden können.
     */
    public List<InfrastrukturbedarfProJahrModel> calculateBedarfForGsNachmittagBetreuung(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) throws CalculationException {
        // Ermittlung der Versorgungsquote und Gruppenstärke für die Einrichtung.
        final var versorgungsquoteGruppenstaerke = versorgungsquoteGruppenstaerkeRepository
            .findFirstByInfrastruktureinrichtungTypAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                InfrastruktureinrichtungTyp.GS_NACHMITTAG_BETREUUNG,
                gueltigAb
            )
            .orElseThrow(() -> {
                final var message =
                    "Für die Bedarfsberechnung der GS-Nachmittagsbetreuungen konnten die Stammdaten zur Versorgungsquote und Gruppenstärke nicht geladen werden.";
                log.error(message);
                return new CalculationException(message);
            });

        return this.calculatePersonen(InfrastruktureinrichtungTyp.GS_NACHMITTAG_BETREUUNG, wohneinheiten, sobonJahr)
            // Ermitteln der Versorgungsquote und Gruppenstärke
            .map(bedarf ->
                this.getVersorgungsquoteAndGruppenstaerkeWithBedarf(
                        bedarf,
                        versorgungsquoteGruppenstaerke,
                        ArtInfrastrukturbedarf.SOBON_URSAECHLICH
                    )
            )
            .toList();
    }

    /**
     * Ermittlung die Bedarfe für den Zeitraum von 20 Jahren auf Basis der gegebenen Wohneinheiten für Grundschulen.
     * Erfolgt nur für SoBoN-ursächliche Berechnungen.
     *
     * @param wohneinheiten zur Ermittlung der Bedarfe.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastruktur.
     * @param gueltigAb zur Ermittlung der korrekten Versorgungsquote und Gruppenstärke.
     * @return die Bedarfe für den Zeitraum von 20 Jahren
     * @throws CalculationException falls die Stammdaten zur Durchführung der Berechnung nicht geladen werden können.
     */
    public List<InfrastrukturbedarfProJahrModel> calculateBedarfForGrundschule(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) throws CalculationException {
        // Ermittlung der Versorgungsquote und Gruppenstärke für die Einrichtung.
        final var versorgungsquoteGruppenstaerke = versorgungsquoteGruppenstaerkeRepository
            .findFirstByInfrastruktureinrichtungTypAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                InfrastruktureinrichtungTyp.GRUNDSCHULE,
                gueltigAb
            )
            .orElseThrow(() -> {
                final var message =
                    "Für die Bedarfsberechnung der Grundschulen konnten die Stammdaten zur Versorgungsquote und Gruppenstärke nicht geladen werden.";
                log.error(message);
                return new CalculationException(message);
            });

        return this.calculatePersonen(InfrastruktureinrichtungTyp.GRUNDSCHULE, wohneinheiten, sobonJahr)
            // Ermitteln der Versorgungsquote und Gruppenstärke
            .map(bedarf ->
                this.getVersorgungsquoteAndGruppenstaerkeWithBedarf(
                        bedarf,
                        versorgungsquoteGruppenstaerke,
                        ArtInfrastrukturbedarf.SOBON_URSAECHLICH
                    )
            )
            .toList();
    }

    /**
     * Ermittlung aller Einwohner für den Zeitraum von 20 Jahren auf Basis der gegebenen Wohneinheiten.
     *
     * @param wohneinheiten zur Ermittlung der Personen.
     * @param sobonJahr zur Ermittlung der Sobon-Orientierungswerte der sozialen Infrastruktur.
     * @return die Personen für den Zeitraum von 20 Jahren
     * @throws CalculationException falls die Stammdaten zur Durchführung der Berechnung nicht geladen werden können.
     */
    public List<PersonenProJahrModel> calculateAlleEinwohner(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheiten,
        final SobonOrientierungswertJahr sobonJahr
    ) throws CalculationException {
        return this.calculatePersonen(InfrastruktureinrichtungTyp.UNSPECIFIED, wohneinheiten, sobonJahr).toList();
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
            .toList();

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
                "Für die planungsursächliche Bedarfsberechnung konnten die Stammdaten zu den SoBoN-Orientierungswerten zur sozialen Infrastruktur nicht geladen werden.";
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
        BigDecimal anzahlPersonenGesamt;
        final var obererRichtwerteByYear = getObererRichtwerteByYear(sobonOrientierungswertSozialeInfrastruktur);
        for (var yearToCalculate = 1; yearToCalculate <= numberOfYearsToCalculate; yearToCalculate++) {
            anzahlPersonenGesamt =
                wohneinheiten
                    .getWohneinheiten()
                    .multiply(obererRichtwerteByYear.get(yearToCalculate))
                    .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
            personen.add(
                createPersonenProJahr(
                    Integer.parseInt(wohneinheiten.getJahr()) + yearToCalculate - 1,
                    anzahlPersonenGesamt
                )
            );
        }
        return personen.stream();
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
     * Die Methode gibt die oberen Richtwerte für die Jahre ein bis zwanzig als Map zurück.
     *
     * @param sobonOrientierungswertSozialeInfrastruktur zur Erstellung der Map.
     * @return die oberen Rechtwerte der Jahre 1 bis 20 als Map mit Jahr als Schlüssel und oberer Richtwert als Wert.
     */
    protected Map<Integer, BigDecimal> getObererRichtwerteByYear(
        final SobonOrientierungswertSozialeInfrastrukturModel sobonOrientierungswertSozialeInfrastruktur
    ) {
        final var obereRichtwerteByYear = new HashMap<Integer, BigDecimal>();
        obereRichtwerteByYear.put(
            1,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr1NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            2,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr2NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            3,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr3NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            4,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr4NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            5,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr5NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            6,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr6NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            7,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr7NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            8,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr8NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            9,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr9NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            10,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr10NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            11,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr11NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            12,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr12NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            13,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr13NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            14,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr14NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            15,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr15NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            16,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr16NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            17,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr17NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            18,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr18NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            19,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr19NachErsterstellung()
        );
        obereRichtwerteByYear.put(
            20,
            sobonOrientierungswertSozialeInfrastruktur.getObererRichtwertEinwohnerJahr20NachErsterstellung()
        );
        return obereRichtwerteByYear;
    }
}
