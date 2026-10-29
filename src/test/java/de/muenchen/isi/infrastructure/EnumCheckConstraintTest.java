package de.muenchen.isi.infrastructure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.CounterType;
import de.muenchen.isi.infrastructure.entity.enums.IdealtypischeBaurateTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtBaulicheNutzung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtDokument;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtGsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Bauratenmethodik;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planart;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

/**
 * Gleicht die Werte der Java-Enums mit den CHECK-Constraints der durch Flyway erzeugten Datenbank ab.
 * <p>
 * Damit fällt beim Build auf, wenn ein Enum erweitert wird, ohne den CHECK-Constraint per Migration anzupassen
 * (z. B. Nachbarschaftstreff in der Bedarfsmeldung), oder wenn ein CHECK-Constraint veraltete Werte enthält.
 * <p>
 * Neue Enum-Spalten mit CHECK-Constraint müssen in {@link #ENUM_COLUMNS} eingetragen werden, sonst schlägt der Test fehl.
 */
@SpringBootTest(classes = { IsiBackendApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
class EnumCheckConstraintTest {

    private static final String SCHEMA = "isidbuser";

    /**
     * Tabellen, deren CHECK-Constraints nicht geprüft werden (Altlasten ohne Entity).
     */
    private static final Set<String> IGNORED_TABLES = Set.of("infrastrukturabfrage", "abfragevariante");

    private static final Pattern IN_LIST = Pattern.compile("\\(*([a-z0-9_]+)\\)*(?:::text)? = ANY \\(");

    private static final Pattern NOT_EQUAL = Pattern.compile("\\(*([a-z0-9_]+)\\)*(?:::text)? <> '([A-Za-z0-9_]+)'");

    private static final Pattern EQUAL = Pattern.compile("\\(*([a-z0-9_]+)\\)*(?:::text)? = '([A-Za-z0-9_]+)'");

    private static final Pattern LITERAL = Pattern.compile("'([A-Za-z0-9_]+)'");

    /**
     * Enum-Spalten mit CHECK-Constraint und den Werten, welche die Anwendung in die jeweilige Spalte schreiben darf.
     */
    private static final List<EnumColumn> ENUM_COLUMNS = List.of(
        // Abfrage
        all("abfrage_bearbeitungshistorie", "ziel_status", StatusAbfrage.class),
        all("bauleitplanverfahren", "status_abfrage", StatusAbfrage.class),
        all("baugenehmigungsverfahren", "status_abfrage", StatusAbfrage.class),
        all("weiteres_verfahren", "status_abfrage", StatusAbfrage.class),
        subset(
            "bauleitplanverfahren",
            "verfahrensstand",
            Verfahrensstand.class,
            Verfahrensstand.getVerfahrensstandForBauleitplanverfahren()
        ),
        subset(
            "baugenehmigungsverfahren",
            "verfahrensstand",
            Verfahrensstand.class,
            Verfahrensstand.getVerfahrensstandForBaugenehmigungsverfahren()
        ),
        subset(
            "weiteres_verfahren",
            "verfahrensstand",
            Verfahrensstand.class,
            Verfahrensstand.getVerfahrensstandForWeiteresVerfahren()
        ),
        withoutUnspecified("bauleitplanverfahren", "sobon_relevant", UncertainBoolean.class),
        withoutUnspecified("weiteres_verfahren", "sobon_relevant", UncertainBoolean.class),
        all("bauleitplanverfahren", "mitzeichnung_beschlussentwurf", UncertainBoolean.class),
        all("weiteres_verfahren", "mitzeichnung_beschlussentwurf", UncertainBoolean.class),
        all("bauleitplanverfahren", "sobon_jahr", SobonVerfahrensgrundsaetzeJahr.class),
        all("weiteres_verfahren", "sobon_jahr", SobonVerfahrensgrundsaetzeJahr.class),
        all("bauleitplanverfahren", "bauratenmethodik_vorbelegung", Bauratenmethodik.class),
        // Abfragevariante
        all(
            "abfrgvar_bauleitplnvrfhrn",
            "sobon_orientierungswert_jahr_planungsursaechlich",
            SobonOrientierungswertJahr.class
        ),
        all(
            "abfrgvar_baugnhmgsverfhrn",
            "sobon_orientierungswert_jahr_planungsursaechlich",
            SobonOrientierungswertJahr.class
        ),
        all(
            "abfrgvar_weitrs_vrfhrn",
            "sobon_orientierungswert_jahr_planungsursaechlich",
            SobonOrientierungswertJahr.class
        ),
        all("abfrgvar_bauleitplnvrfhrn", "bauratenmethodik", Bauratenmethodik.class),
        all("abfrgvar_weitrs_vrfhrn", "bauratenmethodik", Bauratenmethodik.class),
        all("abfrgvar_bauleitplnvrfhrn_planart", "planart", Planart.class),
        subset(
            "abfrgvar_baugnhmgsverfhrn_wesentliche_rechtsgrundlage",
            "wesentliche_rechtsgrundlage",
            WesentlicheRechtsgrundlage.class,
            WesentlicheRechtsgrundlage.getWesentlicheRechtsgrundlageForBaugenehmigungsverfahren()
        ),
        subset(
            "abfrgvar_weitrs_vrfhrn_wesentliche_rechtsgrundlage",
            "wesentliche_rechtsgrundlage",
            WesentlicheRechtsgrundlage.class,
            WesentlicheRechtsgrundlage.getWesentlicheRechtsgrundlageForWeiteresVerfahren()
        ),
        all("baugebiet", "art_bauliche_nutzung", ArtBaulicheNutzung.class),
        all("bedarfsmeldung", "infrastruktureinrichtung_typ", InfrastruktureinrichtungTyp.class),
        // Bauvorhaben
        all("bauvorhaben", "verfahrensstand", Verfahrensstand.class),
        withoutUnspecified("bauvorhaben", "sobon_relevant", UncertainBoolean.class),
        all("bauvorhaben", "sobon_jahr", SobonVerfahrensgrundsaetzeJahr.class),
        subset(
            "bauvorhaben_art_fnp",
            "art_fnp",
            ArtBaulicheNutzung.class,
            ArtBaulicheNutzung.getArtBaulicheNutzungForBauvorhaben()
        ),
        subset(
            "bauvorhaben_wesentliche_rechtsgrundlage",
            "wesentliche_rechtsgrundlage",
            WesentlicheRechtsgrundlage.class,
            WesentlicheRechtsgrundlage.getWesentlicheRechtsgrundlageForBauvorhaben()
        ),
        // Infrastruktureinrichtung
        withoutUnspecified("infrastruktureinrichtung", "status", StatusInfrastruktureinrichtung.class),
        subset(
            "kinderkrippe",
            "einrichtungstraeger",
            Einrichtungstraeger.class,
            Einrichtungstraeger.getEinrichtungstraeger()
        ),
        subset(
            "kindergarten",
            "einrichtungstraeger",
            Einrichtungstraeger.class,
            Einrichtungstraeger.getEinrichtungstraeger()
        ),
        subset(
            "haus_fuer_kinder",
            "einrichtungstraeger",
            Einrichtungstraeger.class,
            Einrichtungstraeger.getEinrichtungstraeger()
        ),
        subset(
            "gs_nachmittag_betreuung",
            "einrichtungstraeger",
            Einrichtungstraeger.class,
            Einrichtungstraeger.getEinrichtungstraeger()
        ),
        subset(
            "grundschule",
            "einrichtungstraeger",
            Einrichtungstraeger.class,
            Einrichtungstraeger.getEinrichtungstraegerSchulen()
        ),
        subset(
            "mittelschule",
            "einrichtungstraeger",
            Einrichtungstraeger.class,
            Einrichtungstraeger.getEinrichtungstraegerSchulen()
        ),
        all("gs_nachmittag_betreuung", "art_gs_nachmittag_betreuung", ArtGsNachmittagBetreuung.class),
        withoutUnspecified("nachbarschaftstreff", "sobon_relevant", UncertainBoolean.class),
        // Sonstige
        all("dokument", "art_dokument", ArtDokument.class),
        all("global_counter", "counter_type", CounterType.class),
        // Stammdaten
        all("idealtypische_baurate", "typ", IdealtypischeBaurateTyp.class),
        all("sobon_orientierungswert_soziale_infrastruktur", "einrichtungstyp", InfrastruktureinrichtungTyp.class),
        all("sobon_orientierungswert_soziale_infrastruktur", "altersklasse", Altersklasse.class),
        all("versorgungsquote_gruppenstaerke", "infrastruktureinrichtung_typ", InfrastruktureinrichtungTyp.class)
    );

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void enumWerteEntsprechenDenCheckConstraints() {
        final Map<String, ColumnCheck> checks = readEnumChecks();
        final List<String> failures = new ArrayList<>();

        final Set<String> registered = new HashSet<>();
        for (final EnumColumn enumColumn : ENUM_COLUMNS) {
            registered.add(enumColumn.key());
            final ColumnCheck check = checks.get(enumColumn.key());
            if (check == null) {
                failures.add(enumColumn.key() + ": kein CHECK-Constraint mit Enum-Werten vorhanden");
                continue;
            }
            final Set<String> enumNames = Arrays.stream(enumColumn.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
            if (check.allowed != null) {
                final Set<String> veraltet = difference(check.allowed, enumNames);
                if (!veraltet.isEmpty()) {
                    failures.add(enumColumn.key() + ": CHECK enthält Werte, die es im Enum nicht gibt: " + veraltet);
                }
                final Set<String> fehlend = difference(enumColumn.writable(), check.allowed);
                if (!fehlend.isEmpty()) {
                    failures.add(
                        enumColumn.key() + ": CHECK erlaubt diese von der Anwendung genutzten Werte nicht: " + fehlend
                    );
                }
            }
            final Set<String> ausgeschlossen = intersection(enumColumn.writable(), check.excluded);
            if (!ausgeschlossen.isEmpty()) {
                failures.add(
                    enumColumn.key() + ": CHECK schließt diese von der Anwendung genutzten Werte aus: " + ausgeschlossen
                );
            }
        }

        for (final String key : checks.keySet()) {
            if (!registered.contains(key)) {
                failures.add(key + ": CHECK-Constraint mit Enum-Werten ist nicht in ENUM_COLUMNS eingetragen");
            }
        }

        assertThat(String.join("\n", failures), failures, empty());
    }

    /**
     * Liest alle CHECK-Constraints des Schemas, die Textliterale enthalten, und fasst sie je Spalte zusammen.
     */
    private Map<String, ColumnCheck> readEnumChecks() {
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList(
            "SELECT c.conrelid::regclass::text AS tabelle, pg_get_constraintdef(c.oid) AS definition " +
                "FROM pg_constraint c JOIN pg_namespace n ON n.oid = c.connamespace " +
                "WHERE n.nspname = ? AND c.contype = 'c'",
            SCHEMA
        );
        final Map<String, ColumnCheck> checks = new HashMap<>();
        for (final Map<String, Object> row : rows) {
            final String table = ((String) row.get("tabelle")).replace(SCHEMA + ".", "");
            if (IGNORED_TABLES.contains(table)) {
                continue;
            }
            final String definition = (String) row.get("definition");

            final Matcher inList = IN_LIST.matcher(definition);
            if (inList.find()) {
                final Set<String> values = new HashSet<>();
                final Matcher literal = LITERAL.matcher(definition.substring(inList.end()));
                while (literal.find()) {
                    values.add(literal.group(1));
                }
                checks.computeIfAbsent(table + "." + inList.group(1), key -> new ColumnCheck()).restrictTo(values);
                continue;
            }
            final Matcher notEqual = NOT_EQUAL.matcher(definition);
            if (notEqual.find()) {
                checks
                    .computeIfAbsent(table + "." + notEqual.group(1), key -> new ColumnCheck())
                    .excluded.add(notEqual.group(2));
                continue;
            }
            final Matcher equal = EQUAL.matcher(definition);
            if (equal.find()) {
                checks
                    .computeIfAbsent(table + "." + equal.group(1), key -> new ColumnCheck())
                    .restrictTo(Set.of(equal.group(2)));
            }
        }
        return checks;
    }

    private static Set<String> difference(final Collection<String> a, final Collection<String> b) {
        final Set<String> result = new HashSet<>(a);
        result.removeAll(b);
        return result;
    }

    private static Set<String> intersection(final Collection<String> a, final Collection<String> b) {
        final Set<String> result = new HashSet<>(a);
        result.retainAll(b);
        return result;
    }

    private static EnumColumn all(final String table, final String column, final Class<? extends Enum<?>> enumClass) {
        return new EnumColumn(table, column, enumClass, names(Arrays.asList(enumClass.getEnumConstants())));
    }

    private static EnumColumn withoutUnspecified(
        final String table,
        final String column,
        final Class<? extends Enum<?>> enumClass
    ) {
        final Set<String> writable = names(Arrays.asList(enumClass.getEnumConstants()));
        writable.remove("UNSPECIFIED");
        return new EnumColumn(table, column, enumClass, writable);
    }

    private static EnumColumn subset(
        final String table,
        final String column,
        final Class<? extends Enum<?>> enumClass,
        final Collection<? extends Enum<?>> writable
    ) {
        return new EnumColumn(table, column, enumClass, names(writable));
    }

    private static Set<String> names(final Collection<? extends Enum<?>> values) {
        return values.stream().map(Enum::name).collect(Collectors.toCollection(HashSet::new));
    }

    private record EnumColumn(String table, String column, Class<? extends Enum<?>> enumClass, Set<String> writable) {
        String key() {
            return table + "." + column;
        }
    }

    /**
     * Zusammengefasste Bedingungen aller CHECK-Constraints einer Spalte.
     */
    private static final class ColumnCheck {

        /**
         * Erlaubte Werte (Schnittmenge aller IN-Listen) oder {@code null}, falls es keine IN-Liste gibt.
         */
        private Set<String> allowed;

        /**
         * Per "<>" ausgeschlossene Werte.
         */
        private final Set<String> excluded = new HashSet<>();

        private void restrictTo(final Set<String> values) {
            if (allowed == null) {
                allowed = new HashSet<>(values);
            } else {
                allowed.retainAll(values);
            }
        }
    }
}
