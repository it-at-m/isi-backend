package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KindergartenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KinderkrippeModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.MittelschuleModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.SchuleModel;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Schule;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.infrastructure.repository.InfrastruktureinrichtungRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = { IsiBackendApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
@MockitoSettings(strictness = Strictness.LENIENT)
class InfrastruktureinrichtungServiceTest {

    @Autowired
    BauvorhabenRepository bauvorhabenRepository;

    @Autowired
    private InfrastruktureinrichtungService infrastruktureinrichtungService;

    @Autowired
    private InfrastruktureinrichtungRepository infrastruktureinrichtungRepository;

    @BeforeEach
    public void beforeEach() {
        infrastruktureinrichtungRepository.deleteAll();
        bauvorhabenRepository.deleteAll();

        Kinderkrippe kinderkrippe1 = new Kinderkrippe();
        kinderkrippe1.setNameEinrichtung("Kinderkrippe 1");
        kinderkrippe1.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        kinderkrippe1.setAnzahlKinderkrippeGruppen(10);
        kinderkrippe1.setAnzahlKinderkrippePlaetze(100);

        Kinderkrippe kinderkrippe2 = new Kinderkrippe();
        kinderkrippe2.setNameEinrichtung("Kinderkrippe 2");
        kinderkrippe2.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_NEUE_EINR);
        kinderkrippe2.setAnzahlKinderkrippeGruppen(11);
        kinderkrippe2.setAnzahlKinderkrippePlaetze(110);

        Kindergarten kindergarten = new Kindergarten();
        kindergarten.setNameEinrichtung("Kindergarten");
        kindergarten.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        kindergarten.setAnzahlKindergartenGruppen(9);
        kindergarten.setAnzahlKindergartenPlaetze(90);

        Grundschule grundschule = new Grundschule();
        grundschule.setNameEinrichtung("Grundschule");
        grundschule.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        grundschule.setSchule(new Schule());
        grundschule.getSchule().setAnzahlKlassen(5);
        grundschule.getSchule().setAnzahlPlaetze(50);

        infrastruktureinrichtungRepository.saveAll(List.of(kinderkrippe1, kinderkrippe2, kindergarten, grundschule));
    }

    @Test
    @Transactional
    void getInfrastruktureinrichtungById() throws EntityNotFoundException {
        var entity = infrastruktureinrichtungRepository.findAll().get(0);

        var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setNameVorhaben("Bauvorhaben");
        bauvorhaben.setStandVerfahren(StandVerfahren.RAHMENPLANUNG);
        bauvorhaben.setWesentlicheRechtsgrundlage(
            List.of(WesentlicheRechtsgrundlage.EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30)
        );
        bauvorhaben.setSobonRelevant(UncertainBoolean.FALSE);
        bauvorhaben = bauvorhabenRepository.saveAndFlush(bauvorhaben);
        entity.setBauvorhaben(bauvorhaben);
        entity = infrastruktureinrichtungRepository.saveAndFlush(entity);

        final var model = infrastruktureinrichtungService.getInfrastruktureinrichtungById(entity.getId());

        assertThat(model.getNameEinrichtung(), is(entity.getNameEinrichtung()));
        assertThat(model.getBauvorhaben(), is(bauvorhaben.getId()));
        assertThat(model.getInfrastruktureinrichtungTyp(), is(entity.getInfrastruktureinrichtungTyp()));
        assertThat(model.getId(), is(entity.getId()));

        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.infrastruktureinrichtungService.getInfrastruktureinrichtungById(UUID.randomUUID())
        );
    }

    @Test
    @Transactional
    void saveInfrastruktureinrichtung() throws OptimisticLockingException, EntityNotFoundException {
        var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setNameVorhaben("Bauvorhaben");
        bauvorhaben.setStandVerfahren(StandVerfahren.RAHMENPLANUNG);
        bauvorhaben.setWesentlicheRechtsgrundlage(
            List.of(WesentlicheRechtsgrundlage.EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30)
        );
        bauvorhaben.setSobonRelevant(UncertainBoolean.FALSE);
        bauvorhaben = bauvorhabenRepository.saveAndFlush(bauvorhaben);

        MittelschuleModel mittelschule = new MittelschuleModel();
        mittelschule.setNameEinrichtung("Mittelschule");
        mittelschule.setStatus(StatusInfrastruktureinrichtung.UNGESICHERTE_PLANUNG_TF_KITA_STANDORT);
        mittelschule.setSchule(new SchuleModel());
        mittelschule.getSchule().setAnzahlKlassen(3);
        mittelschule.getSchule().setAnzahlPlaetze(30);
        mittelschule.setBauvorhaben(bauvorhaben.getId());

        final var savedMittelschule = this.infrastruktureinrichtungService.saveInfrastruktureinrichtung(mittelschule);

        assertThat(savedMittelschule.getId(), is(notNullValue()));
        assertThat(savedMittelschule.getVersion(), is(0L));
        assertThat(savedMittelschule.getNameEinrichtung(), is(mittelschule.getNameEinrichtung()));
        assertThat(savedMittelschule.getBauvorhaben(), is(bauvorhaben.getId()));
        assertThat(savedMittelschule.getInfrastruktureinrichtungTyp(), is(InfrastruktureinrichtungTyp.MITTELSCHULE));
        assertThat(savedMittelschule.getClass(), is(mittelschule.getClass()));
        assertThat(
            ((MittelschuleModel) savedMittelschule).getSchule().getAnzahlKlassen(),
            is(mittelschule.getSchule().getAnzahlKlassen())
        );
        assertThat(
            ((MittelschuleModel) savedMittelschule).getSchule().getAnzahlPlaetze(),
            is(mittelschule.getSchule().getAnzahlPlaetze())
        );
    }

    @Test
    @Transactional
    void updateInfrastruktureinrichtung() throws OptimisticLockingException, EntityNotFoundException {
        var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setNameVorhaben("Bauvorhaben");
        bauvorhaben.setStandVerfahren(StandVerfahren.RAHMENPLANUNG);
        bauvorhaben.setWesentlicheRechtsgrundlage(
            List.of(WesentlicheRechtsgrundlage.EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30)
        );
        bauvorhaben.setSobonRelevant(UncertainBoolean.FALSE);
        bauvorhaben = bauvorhabenRepository.saveAndFlush(bauvorhaben);

        MittelschuleModel mittelschule = new MittelschuleModel();
        mittelschule.setNameEinrichtung("Mittelschule");
        mittelschule.setStatus(StatusInfrastruktureinrichtung.UNGESICHERTE_PLANUNG_TF_KITA_STANDORT);
        mittelschule.setSchule(new SchuleModel());
        mittelschule.getSchule().setAnzahlKlassen(3);
        mittelschule.getSchule().setAnzahlPlaetze(30);
        mittelschule.setBauvorhaben(bauvorhaben.getId());

        final var savedMittelschule = this.infrastruktureinrichtungService.saveInfrastruktureinrichtung(mittelschule);
        assertThat(savedMittelschule.getVersion(), is(0L));

        savedMittelschule.setNameEinrichtung("Mittelschule XXX");

        final var updatedMittelschule =
            this.infrastruktureinrichtungService.updateInfrastruktureinrichtung(savedMittelschule);

        assertThat(updatedMittelschule.getId(), is(savedMittelschule.getId()));
        assertThat(updatedMittelschule.getVersion(), is(1L));
        assertThat(updatedMittelschule.getNameEinrichtung(), is("Mittelschule XXX"));
        assertThat(updatedMittelschule.getBauvorhaben(), is(bauvorhaben.getId()));
        assertThat(updatedMittelschule.getInfrastruktureinrichtungTyp(), is(InfrastruktureinrichtungTyp.MITTELSCHULE));
        assertThat(updatedMittelschule.getClass(), is(savedMittelschule.getClass()));
        assertThat(
            ((MittelschuleModel) updatedMittelschule).getSchule().getAnzahlKlassen(),
            is(((MittelschuleModel) savedMittelschule).getSchule().getAnzahlKlassen())
        );
        assertThat(
            ((MittelschuleModel) updatedMittelschule).getSchule().getAnzahlPlaetze(),
            is(((MittelschuleModel) savedMittelschule).getSchule().getAnzahlPlaetze())
        );

        updatedMittelschule.setVersion(0L);
        Assertions.assertThrows(
            OptimisticLockingException.class,
            () -> this.infrastruktureinrichtungService.updateInfrastruktureinrichtung(updatedMittelschule)
        );

        updatedMittelschule.setId(UUID.randomUUID());
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.infrastruktureinrichtungService.updateInfrastruktureinrichtung(updatedMittelschule)
        );
    }

    @Test
    @Transactional
    void deleteInfrastruktureinrichtungById() throws EntityIsReferencedException, EntityNotFoundException {
        final var entity = infrastruktureinrichtungRepository.findAll().get(0);

        this.infrastruktureinrichtungService.deleteInfrastruktureinrichtungById(entity.getId());

        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.infrastruktureinrichtungService.deleteInfrastruktureinrichtungById(entity.getId())
        );
    }

    @Test
    @Transactional
    void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben()
        throws EntityIsReferencedException {
        this.infrastruktureinrichtungService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(
                new KinderkrippeModel()
            );

        var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setNameVorhaben("Bauvorhaben");
        bauvorhaben.setStandVerfahren(StandVerfahren.RAHMENPLANUNG);
        bauvorhaben.setWesentlicheRechtsgrundlage(
            List.of(WesentlicheRechtsgrundlage.EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30)
        );
        bauvorhaben.setSobonRelevant(UncertainBoolean.FALSE);
        bauvorhaben = bauvorhabenRepository.saveAndFlush(bauvorhaben);

        final InfrastruktureinrichtungModel infrastruktureinrichtung = new KindergartenModel();
        infrastruktureinrichtung.setBauvorhaben(bauvorhaben.getId());
        Assertions.assertThrows(
            EntityIsReferencedException.class,
            () ->
                this.infrastruktureinrichtungService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(
                        infrastruktureinrichtung
                    )
        );
    }
}
