package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KindergartenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KinderkrippeModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.MittelschuleModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.SchuleModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Schule;
import de.muenchen.isi.infrastructure.repository.InfrastruktureinrichtungRepository;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
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

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = { IsiBackendApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
@MockitoSettings(strictness = Strictness.LENIENT)
class InfrastruktureinrichtungServiceTest {

    @Autowired
    private InfrastruktureinrichtungService infrastruktureinrichtungService;

    @Autowired
    private InfrastruktureinrichtungRepository infrastruktureinrichtungRepository;

    @BeforeEach
    public void beforeEach() {
        infrastruktureinrichtungRepository.deleteAll();

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
    void getInfrastruktureinrichtungListElements() {
        final var result = infrastruktureinrichtungService.getInfrastruktureinrichtungListElements();

        assertThat(result.getListElements().size(), is(4));

        assertThat(result.getListElements().get(0).getId(), is(notNullValue()));
        assertThat(
            result.getListElements().get(0).getInfrastruktureinrichtungTyp(),
            is(InfrastruktureinrichtungTyp.GRUNDSCHULE)
        );
        assertThat(result.getListElements().get(0).getNameEinrichtung(), is("Grundschule"));

        assertThat(result.getListElements().get(1).getId(), is(notNullValue()));
        assertThat(
            result.getListElements().get(1).getInfrastruktureinrichtungTyp(),
            is(InfrastruktureinrichtungTyp.KINDERGARTEN)
        );
        assertThat(result.getListElements().get(1).getNameEinrichtung(), is("Kindergarten"));

        assertThat(result.getListElements().get(2).getId(), is(notNullValue()));
        assertThat(
            result.getListElements().get(2).getInfrastruktureinrichtungTyp(),
            is(InfrastruktureinrichtungTyp.KINDERKRIPPE)
        );
        assertThat(result.getListElements().get(2).getNameEinrichtung(), is("Kinderkrippe 1"));

        assertThat(result.getListElements().get(3).getId(), is(notNullValue()));
        assertThat(
            result.getListElements().get(3).getInfrastruktureinrichtungTyp(),
            is(InfrastruktureinrichtungTyp.KINDERKRIPPE)
        );
        assertThat(result.getListElements().get(3).getNameEinrichtung(), is("Kinderkrippe 2"));
    }

    @Test
    void getInfrastruktureinrichtungById() throws EntityNotFoundException {
        final var entity = infrastruktureinrichtungRepository.findAll().get(0);

        final var model = infrastruktureinrichtungService.getInfrastruktureinrichtungById(entity.getId());

        assertThat(entity.getNameEinrichtung(), is(model.getNameEinrichtung()));
        assertThat(entity.getInfrastruktureinrichtungTyp(), is(model.getInfrastruktureinrichtungTyp()));
        assertThat(entity.getId(), is(model.getId()));

        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.infrastruktureinrichtungService.getInfrastruktureinrichtungById(UUID.randomUUID())
        );
    }

    @Test
    void saveInfrastruktureinrichtung() throws OptimisticLockingException {
        MittelschuleModel mittelschule = new MittelschuleModel();
        mittelschule.setNameEinrichtung("Mittelschule");
        mittelschule.setStatus(StatusInfrastruktureinrichtung.UNGESICHERTE_PLANUNG_TF_KITA_STANDORT);
        mittelschule.setSchule(new SchuleModel());
        mittelschule.getSchule().setAnzahlKlassen(3);
        mittelschule.getSchule().setAnzahlPlaetze(30);

        final var savedMittelschule = this.infrastruktureinrichtungService.saveInfrastruktureinrichtung(mittelschule);

        assertThat(savedMittelschule.getId(), is(notNullValue()));
        assertThat(savedMittelschule.getVersion(), is(0L));
        assertThat(savedMittelschule.getNameEinrichtung(), is(mittelschule.getNameEinrichtung()));
        assertThat(
            savedMittelschule.getInfrastruktureinrichtungTyp(),
            is(mittelschule.getInfrastruktureinrichtungTyp())
        );
        assertThat(savedMittelschule.getClass(), is(mittelschule.getClass()));
        assertThat(
            ((MittelschuleModel) savedMittelschule).getSchule().getAnzahlKlassen(),
            is(((MittelschuleModel) mittelschule).getSchule().getAnzahlKlassen())
        );
        assertThat(
            ((MittelschuleModel) savedMittelschule).getSchule().getAnzahlPlaetze(),
            is(((MittelschuleModel) mittelschule).getSchule().getAnzahlPlaetze())
        );
    }

    @Test
    void updateInfrastruktureinrichtung() throws OptimisticLockingException, EntityNotFoundException {
        MittelschuleModel mittelschule = new MittelschuleModel();
        mittelschule.setNameEinrichtung("Mittelschule");
        mittelschule.setStatus(StatusInfrastruktureinrichtung.UNGESICHERTE_PLANUNG_TF_KITA_STANDORT);
        mittelschule.setSchule(new SchuleModel());
        mittelschule.getSchule().setAnzahlKlassen(3);
        mittelschule.getSchule().setAnzahlPlaetze(30);

        final var savedMittelschule = this.infrastruktureinrichtungService.saveInfrastruktureinrichtung(mittelschule);
        assertThat(savedMittelschule.getVersion(), is(0L));

        savedMittelschule.setNameEinrichtung("Mittelschule XXX");

        final var updatedMittelschule =
            this.infrastruktureinrichtungService.updateInfrastruktureinrichtung(savedMittelschule);

        assertThat(updatedMittelschule.getId(), is(savedMittelschule.getId()));
        assertThat(updatedMittelschule.getVersion(), is(1L));
        assertThat(updatedMittelschule.getNameEinrichtung(), is("Mittelschule XXX"));
        assertThat(
            updatedMittelschule.getInfrastruktureinrichtungTyp(),
            is(savedMittelschule.getInfrastruktureinrichtungTyp())
        );
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
    void deleteInfrastruktureinrichtungById() throws EntityIsReferencedException, EntityNotFoundException {
        final var entity = infrastruktureinrichtungRepository.findAll().get(0);

        this.infrastruktureinrichtungService.deleteInfrastruktureinrichtungById(entity.getId());

        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.infrastruktureinrichtungService.deleteInfrastruktureinrichtungById(entity.getId())
        );
    }

    @Test
    void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben()
        throws EntityIsReferencedException {
        this.infrastruktureinrichtungService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(
                new KinderkrippeModel()
            );

        final InfrastruktureinrichtungModel infrastruktureinrichtung = new KindergartenModel();
        infrastruktureinrichtung.setBauvorhaben(new BauvorhabenModel());
        Assertions.assertThrows(
            EntityIsReferencedException.class,
            () ->
                this.infrastruktureinrichtungService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(
                        infrastruktureinrichtung
                    )
        );
    }
}
