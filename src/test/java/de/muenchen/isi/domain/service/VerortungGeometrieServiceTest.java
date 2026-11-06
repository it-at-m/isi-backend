package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.common.FlurstueckModel;
import de.muenchen.isi.domain.model.common.GemarkungModel;
import de.muenchen.isi.domain.model.common.MultiPolygonGeometryModel;
import de.muenchen.isi.domain.model.common.StadtbezirkModel;
import de.muenchen.isi.domain.model.common.VerortungMultiPolygonModel;
import de.muenchen.isi.infrastructure.entity.common.VerortungGeometrie;
import de.muenchen.isi.infrastructure.repository.common.VerortungGeometrieRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = { IsiBackendApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
@Transactional
class VerortungGeometrieServiceTest {

    @Autowired
    private VerortungGeometrieService verortungGeometrieService;

    @Autowired
    private VerortungGeometrieRepository verortungGeometrieRepository;

    @Autowired
    private EntityManager entityManager;

    private Statistics statistics;

    @BeforeEach
    void beforeEach() {
        statistics = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class).getStatistics();
        statistics.setStatisticsEnabled(true);
    }

    @Test
    void synchronisierenLegtGeometrieAn() {
        final var id = UUID.randomUUID();

        verortungGeometrieService.synchronisieren(id, verortung(11.0));
        schreibenUndLeeren();

        final var geometrie = verortungGeometrieRepository.findById(id).orElseThrow();
        assertThat(geometrie.getUmgriff().getSRID(), is(4326));
        assertThat(geometrie.getGeometrieHash(), is(notNullValue()));
        assertThat(geometrie.getFlurstuecke().size(), is(2));
    }

    @Test
    void synchronisierenSchreibtBeiGleicherGeometrieNicht() {
        final var id = UUID.randomUUID();
        verortungGeometrieService.synchronisieren(id, verortung(11.0));
        schreibenUndLeeren();
        statistics.clear();

        verortungGeometrieService.synchronisieren(id, verortung(11.0));
        schreibenUndLeeren();

        assertThat(statistics.getEntityStatistics(VerortungGeometrie.class.getName()).getUpdateCount(), is(0L));
        assertThat(statistics.getEntityStatistics(VerortungGeometrie.class.getName()).getInsertCount(), is(0L));
    }

    @Test
    void synchronisierenSchreibtGeaenderteGeometrie() {
        final var id = UUID.randomUUID();
        verortungGeometrieService.synchronisieren(id, verortung(11.0));
        schreibenUndLeeren();
        final var hashVorher = verortungGeometrieRepository.findById(id).orElseThrow().getGeometrieHash();
        schreibenUndLeeren();

        verortungGeometrieService.synchronisieren(id, verortung(12.0));
        schreibenUndLeeren();

        final var geometrie = verortungGeometrieRepository.findById(id).orElseThrow();
        assertThat(geometrie.getGeometrieHash().equals(hashVorher), is(false));
        assertThat(geometrie.getUmgriff().getCoordinate().getX(), is(12.0));
        assertThat(geometrie.getFlurstuecke().size(), is(2));
    }

    @Test
    void synchronisierenOhneUmgriffLaesstGeometrieUnberuehrtUndLaedtSieNicht() {
        final var id = UUID.randomUUID();
        verortungGeometrieService.synchronisieren(id, verortung(11.0));
        schreibenUndLeeren();
        statistics.clear();

        verortungGeometrieService.synchronisieren(id, new VerortungMultiPolygonModel());
        schreibenUndLeeren();

        assertThat(statistics.getEntityStatistics(VerortungGeometrie.class.getName()).getLoadCount(), is(0L));
        assertThat(verortungGeometrieRepository.findById(id).isPresent(), is(true));
    }

    @Test
    void synchronisierenOhneVerortungLoeschtGeometrie() {
        final var id = UUID.randomUUID();
        verortungGeometrieService.synchronisieren(id, verortung(11.0));
        schreibenUndLeeren();

        verortungGeometrieService.synchronisieren(id, null);
        schreibenUndLeeren();

        assertThat(verortungGeometrieRepository.findById(id).isPresent(), is(false));
    }

    @Test
    void synchronisierenUebergehtFlurstueckOhnePolygon() {
        final var id = UUID.randomUUID();
        final var verortung = verortung(11.0);
        final var ohnePolygon = new FlurstueckModel();
        ohnePolygon.setNummer("ohne");
        ohnePolygon.setGemarkungNummer(BigDecimal.valueOf(1900));
        final var gemarkung = new GemarkungModel();
        gemarkung.setNummer(BigDecimal.valueOf(1901));
        gemarkung.setFlurstuecke(Set.of(ohnePolygon));
        verortung.setGemarkungen(Set.of(verortung.getGemarkungen().iterator().next(), gemarkung));

        verortungGeometrieService.synchronisieren(id, verortung);
        schreibenUndLeeren();

        assertThat(verortungGeometrieRepository.findById(id).orElseThrow().getFlurstuecke().size(), is(2));
    }

    @Test
    void ergaenzenSetztUmgriffUndFlurstueckPolygone() {
        final var id = UUID.randomUUID();
        verortungGeometrieService.synchronisieren(id, verortung(11.0));
        schreibenUndLeeren();

        // So kommt das Model aus der Datenbank: Sachdaten vorhanden, keine Polygone.
        final var geladen = verortung(11.0);
        geladen.setMultiPolygon(null);
        geladen.getGemarkungen().forEach(g -> g.getFlurstuecke().forEach(f -> f.setMultiPolygon(null)));
        final var bauvorhaben = new BauvorhabenModel();
        bauvorhaben.setVerortung(geladen);

        verortungGeometrieService.ergaenzen(id, bauvorhaben);

        assertThat(geladen.getMultiPolygon().getCoordinates().get(0).get(0).get(0).get(0).doubleValue(), is(11.0));
        final var flurstuecke = geladen.getGemarkungen().iterator().next().getFlurstuecke();
        assertThat(flurstuecke.size(), is(2));
        flurstuecke.forEach(flurstueck -> assertThat(flurstueck.getMultiPolygon(), is(notNullValue())));
        // Die Mengen wurden nach dem Ändern neu aufgebaut, contains findet die Elemente weiterhin.
        flurstuecke.forEach(flurstueck -> assertThat(flurstuecke.contains(flurstueck), is(true)));
        assertThat(geladen.getStadtbezirke().iterator().next().getMultiPolygon(), is(nullValue()));
    }

    @Test
    void ergaenzenOhneVerortungOderGeometrieAendertNichts() {
        final var ohneVerortung = new BauvorhabenModel();
        verortungGeometrieService.ergaenzen(UUID.randomUUID(), ohneVerortung);
        assertThat(ohneVerortung.getVerortung(), is(nullValue()));

        final var ohneGeometrie = new BauvorhabenModel();
        ohneGeometrie.setVerortung(new VerortungMultiPolygonModel());
        verortungGeometrieService.ergaenzen(UUID.randomUUID(), ohneGeometrie);
        assertThat(ohneGeometrie.getVerortung().getMultiPolygon(), is(nullValue()));
    }

    @Test
    void findeUmgriffeLiefertNurVorhandene() {
        final var id = UUID.randomUUID();
        verortungGeometrieService.synchronisieren(id, verortung(11.0));
        schreibenUndLeeren();

        final var umgriffe = verortungGeometrieService.findeUmgriffe(List.of(id, UUID.randomUUID()));

        assertThat(umgriffe.size(), is(1));
        assertThat(umgriffe.get(id), is(notNullValue()));
        assertThat(verortungGeometrieService.findeUmgriffe(List.of()).isEmpty(), is(true));
    }

    private void schreibenUndLeeren() {
        entityManager.flush();
        entityManager.clear();
    }

    private static VerortungMultiPolygonModel verortung(final double x) {
        final var mitNenner = new FlurstueckModel();
        mitNenner.setNummer("1900/2");
        mitNenner.setZaehler(1900L);
        mitNenner.setNenner(2L);
        mitNenner.setGemarkungNummer(BigDecimal.valueOf(1900));
        mitNenner.setMultiPolygon(quadrat(x));
        final var ohneNenner = new FlurstueckModel();
        ohneNenner.setNummer("19001");
        ohneNenner.setZaehler(19001L);
        ohneNenner.setMultiPolygon(quadrat(x + 0.5));
        final var gemarkung = new GemarkungModel();
        gemarkung.setNummer(BigDecimal.valueOf(1900));
        gemarkung.setFlurstuecke(Set.of(mitNenner, ohneNenner));
        final var stadtbezirk = new StadtbezirkModel();
        stadtbezirk.setNummer("09");
        final var verortung = new VerortungMultiPolygonModel();
        verortung.setMultiPolygon(quadrat(x));
        verortung.setGemarkungen(Set.of(gemarkung));
        verortung.setStadtbezirke(Set.of(stadtbezirk));
        return verortung;
    }

    private static MultiPolygonGeometryModel quadrat(final double x) {
        final var y = 48.0;
        final var model = new MultiPolygonGeometryModel();
        model.setType("MultiPolygon");
        model.setCoordinates(
            List.of(
                List.of(
                    List.of(
                        List.of(BigDecimal.valueOf(x), BigDecimal.valueOf(y)),
                        List.of(BigDecimal.valueOf(x + 0.1), BigDecimal.valueOf(y)),
                        List.of(BigDecimal.valueOf(x + 0.1), BigDecimal.valueOf(y + 0.1)),
                        List.of(BigDecimal.valueOf(x), BigDecimal.valueOf(y))
                    )
                )
            )
        );
        return model;
    }
}
