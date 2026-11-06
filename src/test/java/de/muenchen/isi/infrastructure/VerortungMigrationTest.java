package de.muenchen.isi.infrastructure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.UUID;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.testcontainers.jdbc.ContainerDatabaseDriver;

/**
 * Prüft V50 und V51 mit Bestandsdaten: Flyway migriert bis V49, die Testdaten kommen im alten Format hinein,
 * danach laufen die Migrationen. Eine eigene Datenbank verhindert, dass andere Tests die Daten sehen.
 */
class VerortungMigrationTest {

    private static final String URL =
        "jdbc:tc:postgis:15-3.4:///verortungmigration?TC_DAEMON=true&TC_INITSCRIPT=testcontainers/init-isidbuser.sql&currentSchema=isidbuser,public";

    private static final String BAUVORHABEN = UUID.randomUUID().toString();

    private static final String BAUVORHABEN_NUR_UMGRIFF = UUID.randomUUID().toString();

    private static final String BAUVORHABEN_OHNE_VERORTUNG = UUID.randomUUID().toString();

    private static final String BAUVORHABEN_OHNE_UMGRIFF = UUID.randomUUID().toString();

    private static final String ABFRAGE = UUID.randomUUID().toString();

    private static final String EINRICHTUNG = UUID.randomUUID().toString();

    private static final String QUADRAT = """
    {"type":"MultiPolygon","coordinates":[[[[11.0,48.0],[11.1,48.0],[11.1,48.1],[11.0,48.1],[11.0,48.0]]]]}""";

    /** Zwei Teilflächen, die erste hat ein Loch. Der geometrische Schwerpunkt läge im Loch. */
    private static final String MEHRTEILIG = """
    {"type":"MultiPolygon","coordinates":[
      [[[11.0,48.0],[11.1,48.0],[11.1,48.1],[11.0,48.1],[11.0,48.0]],
       [[11.01,48.01],[11.01,48.09],[11.09,48.09],[11.09,48.01],[11.01,48.01]]],
      [[[11.2,48.2],[11.3,48.2],[11.3,48.3],[11.2,48.3],[11.2,48.2]]]]}""";

    private static final String VOLLE_VERORTUNG = """
    {"multiPolygon":%1$s,
     "centroid":{"type":"Point","coordinates":[1,2]},
     "centroidUtm":{"zone":"32U","east":1,"north":2},
     "stadtbezirke":[{"nummer":"09","name":"Neuhausen","multiPolygon":%2$s}],
     "bezirksteile":[{"nummer":"09.1","multiPolygon":%2$s}],
     "viertel":[{"nummer":"09.1.1","flaecheQm":1000,"multiPolygon":%2$s}],
     "gemarkungen":[{"nummer":1900,"name":"Gemarkung","multiPolygon":%2$s,"flurstuecke":[
        {"nummer":"19001","zaehler":19001,"nenner":null,"gemarkungNummer":1900,"flaecheQm":10,"multiPolygon":%2$s},
        {"nummer":"1900/2","zaehler":1900,"nenner":2,"gemarkungNummer":null,"multiPolygon":%2$s},
        {"nummer":"1900/2","zaehler":1900,"nenner":2,"gemarkungNummer":1900,"flaecheQm":20,"multiPolygon":%2$s}]}],
     "kitaplanungsbereiche":[{"kitaPlb":200,"kitaPlbT":"zwei","multiPolygon":%2$s},{"kitaPlb":100,"kitaPlbT":"eins","multiPolygon":%2$s}],
     "grundschulsprengel":[{"nummer":101,"multiPolygon":%2$s}],
     "mittelschulsprengel":[{"nummer":201,"multiPolygon":%2$s}]}""".formatted(MEHRTEILIG, QUADRAT);

    private static final String NUR_UMGRIFF = """
    {"multiPolygon":%s,"stadtbezirke":[],"bezirksteile":[],"viertel":[],"gemarkungen":[],
     "kitaplanungsbereiche":[],"grundschulsprengel":[],"mittelschulsprengel":[]}""".formatted(QUADRAT);

    private static final String OHNE_UMGRIFF = """
    {"multiPolygon":null,"stadtbezirke":[{"nummer":"01","name":"Altstadt","multiPolygon":%s}]}""".formatted(QUADRAT);

    private static final String PUNKT = """
    {"point":{"type":"Point","coordinates":[11.5,48.1]},"pointUtm":{"zone":"32U","east":1,"north":2},
     "stadtbezirke":[{"nummer":"05","name":"Au","multiPolygon":%s}],"viertel":null,"gemarkungen":null}""".formatted(
        QUADRAT
    );

    private static JdbcTemplate jdbc;

    @BeforeAll
    static void migrieren() {
        final var dataSource = new SimpleDriverDataSource(new ContainerDatabaseDriver(), URL);
        final var flyway = Flyway.configure()
            .dataSource(dataSource)
            .schemas("isidbuser")
            .locations("classpath:db/migration");
        flyway.target("49").load().migrate();

        jdbc = new JdbcTemplate(dataSource);
        bauvorhaben(BAUVORHABEN, VOLLE_VERORTUNG);
        bauvorhaben(BAUVORHABEN_NUR_UMGRIFF, NUR_UMGRIFF);
        bauvorhaben(BAUVORHABEN_OHNE_VERORTUNG, null);
        bauvorhaben(BAUVORHABEN_OHNE_UMGRIFF, OHNE_UMGRIFF);
        jdbc.update(
            """
            INSERT INTO isidbuser.bauleitplanverfahren (id, name, status_abfrage, sub, frist_bearbeitung, mitzeichnung_beschlussentwurf,
                sobon_relevant, verfahrensstand, start_42_verfahren_datum_unbekannt, verortung)
            VALUES (?, 'Abfrage', 'ANGELEGT', 'test', now(), 'FALSE', 'FALSE', 'VORBEREITUNG_SATZUNGSBESCHLUSS', true, ?::jsonb)""",
            ABFRAGE,
            NUR_UMGRIFF
        );
        jdbc.update(
            """
            INSERT INTO isidbuser.infrastruktureinrichtung (id, infrastruktureinrichtung_typ, lfd_nr, name_einrichtung, status, anlass_planung, verortung)
            VALUES (?, 'NACHBARSCHAFTSTREFF', 1, 'Einrichtung', 'BESTAND', 'NEUBAU_BEBAUUNGSPLAN', ?::jsonb)""",
            EINRICHTUNG,
            PUNKT
        );

        Flyway.configure()
            .dataSource(dataSource)
            .schemas("isidbuser")
            .locations("classpath:db/migration")
            .load()
            .migrate();
    }

    private static void bauvorhaben(final String id, final String verortung) {
        jdbc.update(
            """
            INSERT INTO isidbuser.bauvorhaben (id, name_vorhaben, sobon_relevant, verfahrensstand, verortung)
            VALUES (?, 'Bauvorhaben', 'FALSE', 'VORABFRAGE_OHNE_KONKRETEN_STAND', ?::jsonb)""",
            id,
            verortung
        );
    }

    private static String verortung(final String tabelle, final String id) {
        return jdbc.queryForObject(
            "SELECT verortung::text FROM isidbuser." + tabelle + " WHERE id = ?",
            String.class,
            id
        );
    }

    private static Long zahl(final String sql, final Object... parameter) {
        return jdbc.queryForObject(sql, Long.class, parameter);
    }

    @Test
    void umgriffBleibtMitLochUndTeilflaechenErhalten() {
        assertThat(
            zahl("SELECT ST_NumGeometries(umgriff) FROM isidbuser.verortung_geometrie WHERE id = ?", BAUVORHABEN),
            is(2L)
        );
        assertThat(
            zahl(
                "SELECT ST_NRings(ST_GeometryN(umgriff, 1)) FROM isidbuser.verortung_geometrie WHERE id = ?",
                BAUVORHABEN
            ),
            is(2L)
        );
    }

    @Test
    void flurstueckeWerdenUebernommenUndDoppelteZusammengefasst() {
        assertThat(
            zahl("SELECT count(*) FROM isidbuser.verortung_flurstueck WHERE verortung_geometrie_id = ?", BAUVORHABEN),
            is(2L)
        );
        // Ohne eigene Gemarkungsnummer gilt die der Gemarkung, ohne Nenner bleibt die Nummer der Schlüssel.
        assertThat(
            zahl(
                "SELECT count(*) FROM isidbuser.verortung_flurstueck WHERE verortung_geometrie_id = ? AND gemarkung_nummer = 1900 AND nummer IN ('19001', '1900/2')",
                BAUVORHABEN
            ),
            is(2L)
        );
    }

    @Test
    void jsonbEnthaeltKeinPolygonMehrAberAlleSachdaten() {
        final var verortung = verortung("bauvorhaben", BAUVORHABEN);
        assertThat(verortung, not(containsString("multiPolygon")));
        assertThat(verortung, containsString("\"name\": \"Neuhausen\""));
        assertThat(verortung, containsString("\"nummer\": \"09.1.1\""));
        assertThat(verortung, containsString("\"flaecheQm\": 20"));
        assertThat(verortung, containsString("\"nummer\": 201"));
        assertThat(verortung("bauleitplanverfahren", ABFRAGE), not(containsString("multiPolygon")));
    }

    @Test
    void schwerpunktLiegtInDerFlaecheAuchBeiLoch() {
        assertThat(
            zahl(
                """
                SELECT count(*) FROM isidbuser.bauvorhaben b JOIN isidbuser.verortung_geometrie g ON g.id = b.id
                WHERE ST_Intersects(g.umgriff, ST_SetSRID(ST_GeomFromGeoJSON(b.verortung -> 'centroid'), 4326))"""
            ),
            is(2L)
        );
        assertThat(
            jdbc.queryForObject(
                "SELECT verortung -> 'centroidUtm' ->> 'zone' FROM isidbuser.bauvorhaben WHERE id = ?",
                String.class,
                BAUVORHABEN
            ),
            is("32U")
        );
        // Der alte Platzhalter-Schwerpunkt ist überschrieben.
        assertThat(
            zahl(
                "SELECT ((verortung -> 'centroidUtm' ->> 'east')::numeric)::bigint FROM isidbuser.bauvorhaben WHERE id = ?",
                BAUVORHABEN
            ) > 1000,
            is(true)
        );
    }

    @Test
    void kitaplanungsbereicheStehenSortiert() {
        assertThat(
            jdbc.queryForObject(
                "SELECT verortung -> 'kitaplanungsbereiche' -> 0 ->> 'kitaPlbT' FROM isidbuser.bauvorhaben WHERE id = ?",
                String.class,
                BAUVORHABEN
            ),
            is("eins")
        );
    }

    @Test
    void ohneVerortungBleibtLeer() {
        assertThat(verortung("bauvorhaben", BAUVORHABEN_OHNE_VERORTUNG), is(nullValue()));
        assertThat(
            zahl("SELECT count(*) FROM isidbuser.verortung_geometrie WHERE id = ?", BAUVORHABEN_OHNE_VERORTUNG),
            is(0L)
        );
    }

    @Test
    void ohneUmgriffGibtEsKeineGeometrieAberSchlankeGebiete() {
        assertThat(
            zahl("SELECT count(*) FROM isidbuser.verortung_geometrie WHERE id = ?", BAUVORHABEN_OHNE_UMGRIFF),
            is(0L)
        );
        final var verortung = verortung("bauvorhaben", BAUVORHABEN_OHNE_UMGRIFF);
        assertThat(verortung, not(containsString("multiPolygon")));
        assertThat(verortung, containsString("Altstadt"));
    }

    @Test
    void nurUmgriffBekommtGeometrieUndSchwerpunkt() {
        assertThat(
            zahl("SELECT count(*) FROM isidbuser.verortung_geometrie WHERE id = ?", BAUVORHABEN_NUR_UMGRIFF),
            is(1L)
        );
        assertThat(verortung("bauvorhaben", BAUVORHABEN_NUR_UMGRIFF), containsString("centroid"));
    }

    @Test
    void einrichtungBehaeltPunktUndBekommtKeineGeometrie() {
        final var verortung = verortung("infrastruktureinrichtung", EINRICHTUNG);
        assertThat(verortung, not(containsString("multiPolygon")));
        assertThat(verortung, containsString("\"coordinates\": [11.5, 48.1]"));
        assertThat(verortung, containsString("\"name\": \"Au\""));
        // Werte, die keine Liste sind, bleiben unverändert.
        assertThat(verortung, containsString("\"viertel\": null"));
        assertThat(verortung, not(containsString("centroid")));
        assertThat(zahl("SELECT count(*) FROM isidbuser.verortung_geometrie WHERE id = ?", EINRICHTUNG), is(0L));
    }

    @Test
    void geometrieVerschwindetMitDemEigentuemer() {
        final var id = UUID.randomUUID().toString();
        bauvorhaben(id, null);
        jdbc.update(
            "INSERT INTO isidbuser.verortung_geometrie (id, umgriff) VALUES (?, ST_GeomFromText('MULTIPOLYGON(((11 48,11.1 48,11.1 48.1,11 48)))', 4326))",
            id
        );
        jdbc.update(
            "INSERT INTO isidbuser.verortung_flurstueck (verortung_geometrie_id, nummer, geometrie) SELECT id, '1', umgriff FROM isidbuser.verortung_geometrie WHERE id = ?",
            id
        );

        jdbc.update("DELETE FROM isidbuser.bauvorhaben WHERE id = ?", id);

        assertThat(zahl("SELECT count(*) FROM isidbuser.verortung_geometrie WHERE id = ?", id), is(0L));
        assertThat(
            zahl("SELECT count(*) FROM isidbuser.verortung_flurstueck WHERE verortung_geometrie_id = ?", id),
            is(0L)
        );
    }
}
