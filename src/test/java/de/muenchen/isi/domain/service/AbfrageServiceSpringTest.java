package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.TestData;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.BedarfsmeldungFachreferateModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.AbfragevarianteBaugenehmigungsverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.AbfragevarianteWeiteresVerfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.BaugenehmigungsverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.BauleitplanverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.WeiteresVerfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBaugenehmigungsverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.WeiteresVerfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.service.transition.MockCustomUser;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
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
class AbfrageServiceSpringTest {

    @Autowired
    private AbfrageService abfrageService;

    @Autowired
    private AbfrageRepository abfrageRepository;

    @Test
    @Transactional
    @MockCustomUser(roles = { "anwender" })
    void getAbfrageInStatusErledigtMitFachreferateRoleAnwender()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, UserRoleNotAllowedException {
        AbfrageModel abfrageModel = TestData.createBauleitplanverfahrenModel();
        abfrageModel = this.abfrageService.save(abfrageModel);
        abfrageModel.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        abfrageModel = this.abfrageService.save(abfrageModel);

        var result = this.abfrageService.getById(abfrageModel.getId());

        assertThat(result, is(abfrageModel));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "anwender" })
    void getAbfrageInStatusErledigtOhneFachreferateRoleAnwender()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, UserRoleNotAllowedException {
        AbfrageModel abfrageModel = TestData.createBauleitplanverfahrenModel();
        abfrageModel = this.abfrageService.save(abfrageModel);
        abfrageModel.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        abfrageModel = this.abfrageService.save(abfrageModel);

        var result = this.abfrageService.getById(abfrageModel.getId());

        assertThat(result, is(abfrageModel));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "anwender", "admin" })
    void getAbfrageInStatusAngelegtRoleAnwenderAndAdmin()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, UserRoleNotAllowedException {
        AbfrageModel abfrageModel = TestData.createBauleitplanverfahrenModel();
        abfrageModel = this.abfrageService.save(abfrageModel);

        var result = this.abfrageService.getById(abfrageModel.getId());

        assertThat(result, is(abfrageModel));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "anwender" })
    void throwUserRoleNotAllowedExceptionWhenStatusIsNotErledigt()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, UserRoleNotAllowedException {
        AbfrageModel abfrageModel = TestData.createBauleitplanverfahrenModel();
        abfrageModel = this.abfrageService.save(abfrageModel);

        final var uuid = abfrageModel.getId();
        Assertions.assertThrows(UserRoleNotAllowedException.class, () -> this.abfrageService.getById(uuid));

        abfrageModel.setStatusAbfrage(StatusAbfrage.OFFEN);
        abfrageModel = this.abfrageService.save(abfrageModel);
        Assertions.assertThrows(UserRoleNotAllowedException.class, () -> this.abfrageService.getById(uuid));

        abfrageModel.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrageModel = this.abfrageService.save(abfrageModel);
        Assertions.assertThrows(UserRoleNotAllowedException.class, () -> this.abfrageService.getById(uuid));

        abfrageModel.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        abfrageModel = this.abfrageService.save(abfrageModel);
        Assertions.assertThrows(UserRoleNotAllowedException.class, () -> this.abfrageService.getById(uuid));

        abfrageModel.setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        abfrageModel = this.abfrageService.save(abfrageModel);
        Assertions.assertThrows(UserRoleNotAllowedException.class, () -> this.abfrageService.getById(uuid));

        abfrageModel.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrageModel);
        Assertions.assertThrows(UserRoleNotAllowedException.class, () -> this.abfrageService.getById(uuid));
    }

    @Test
    @Transactional
    @MockCustomUser
    void getByAbfragevarianteId()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        UUID abfragevarianteId =
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren().get(0).getId();
        AbfrageModel foundAbfrage = abfrageService.getByAbfragevarianteId(abfragevarianteId);
        assertThat(foundAbfrage, is(abfrage));

        abfrage = TestData.createBaugenehmigungsverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfragevarianteId =
            ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevariantenBaugenehmigungsverfahren().get(0).getId();
        foundAbfrage = abfrageService.getByAbfragevarianteId(abfragevarianteId);
        assertThat(foundAbfrage, is(abfrage));

        abfrage = TestData.createWeiteresVerfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfragevarianteId = ((WeiteresVerfahrenModel) abfrage).getAbfragevariantenWeiteresVerfahren().get(0).getId();
        foundAbfrage = abfrageService.getByAbfragevarianteId(abfragevarianteId);
        assertThat(foundAbfrage, is(abfrage));

        abfrageRepository.deleteAll();
    }

    @Test
    @Transactional
    @MockCustomUser
    void getByAbfragevarianteIdEntityNotFoundException()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException {
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        this.abfrageService.save(abfrage);

        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageService.getByAbfragevarianteId(UUID.randomUUID())
        );
        abfrageRepository.deleteAll();
    }

    @Test
    @Transactional
    @MockCustomUser
    void patchAngelegtBauleitplanverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);

        AbfrageAngelegtModel abfrageAngelegt = TestData.createBauleitplanverfahrenAngelegtModel();

        abfrage = this.abfrageService.patchAngelegt(abfrageAngelegt, abfrage.getId());
        assertThat(abfrage.getName(), is("Neubausiedlung in Musterort 2"));
        assertThat(
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren().get(0).getName(),
            is("Name Abfragevariante 102")
        );

        abfrageRepository.deleteAll();
    }

    @Test
    @Transactional
    @MockCustomUser
    void patchAngelegtBaugenehmigungsverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createBaugenehmigungsverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);

        AbfrageAngelegtModel abfrageAngelegt = TestData.createBaugenehmigungsverfahrenAngelegtModel();

        abfrage = this.abfrageService.patchAngelegt(abfrageAngelegt, abfrage.getId());
        assertThat(abfrage.getName(), is("Altbausiedlung in Musterort 2"));
        assertThat(
            ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevariantenBaugenehmigungsverfahren().get(0).getName(),
            is("Name Abfragevariante 112")
        );

        abfrageRepository.deleteAll();
    }

    @Test
    @Transactional
    @MockCustomUser
    void patchAngelegtWeiteresVerfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createWeiteresVerfahrenModel();
        abfrage = this.abfrageService.save(abfrage);

        AbfrageAngelegtModel abfrageAngelegt = TestData.createWeiteresVerfahrenAngelegtModel();

        abfrage = this.abfrageService.patchAngelegt(abfrageAngelegt, abfrage.getId());
        assertThat(abfrage.getName(), is("Überbausiedlung in Musterort 2"));
        assertThat(
            ((WeiteresVerfahrenModel) abfrage).getAbfragevariantenWeiteresVerfahren().get(0).getName(),
            is("Name Abfragevariante 92")
        );

        abfrageRepository.deleteAll();
    }

    @Test
    @Transactional
    @MockCustomUser
    void patchInBearbeitungSachbearbeitungBauleitplanverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);

        final var abfragePatch = new BauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        abfragePatch.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragePatch.setVersion(abfrage.getVersion());
        abfragePatch.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());
        final var abfragevariantePatch =
            new AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel();
        abfragevariantePatch.setId(
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren().get(0).getId()
        );
        abfragevariantePatch.setVersion(
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren().get(0).getVersion()
        );
        abfragevariantePatch.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevariantePatch.setGfWohnenPlanungsursaechlich(
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren()
                .get(0)
                .getGfWohnenPlanungsursaechlich()
        );
        abfragevariantePatch.setSobonOrientierungswertJahr(SobonOrientierungswertJahr.JAHR_2017);
        abfragevariantePatch.setAnmerkung("Die Anmerkung Bauleitplanverfahren Patch Sachbearbeitung");
        abfragePatch.setAbfragevariantenBauleitplanverfahren(List.of(abfragevariantePatch));

        abfrage = this.abfrageService.patchInBearbeitungSachbearbeitung(abfragePatch, abfrage.getId());
        assertThat(
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren().get(0).getAnmerkung(),
            is("Die Anmerkung Bauleitplanverfahren Patch Sachbearbeitung")
        );

        abfrageRepository.deleteAll();
    }

    @Test
    @Transactional
    @MockCustomUser
    void patchInBearbeitungSachbearbeitungBaugenehmigungsverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createBaugenehmigungsverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);

        final var abfragePatch = new BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel();
        abfragePatch.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragePatch.setVersion(abfrage.getVersion());
        abfragePatch.setVerortung(((BaugenehmigungsverfahrenModel) abfrage).getVerortung());
        abfragePatch.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());
        final var abfragevariantePatch =
            new AbfragevarianteBaugenehmigungsverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel();
        abfragevariantePatch.setId(
            ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevariantenBaugenehmigungsverfahren().get(0).getId()
        );
        abfragevariantePatch.setVersion(
            ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevariantenBaugenehmigungsverfahren().get(0).getVersion()
        );
        abfragevariantePatch.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragevariantePatch.setGfWohnenPlanungsursaechlich(
            ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevariantenBaugenehmigungsverfahren()
                .get(0)
                .getGfWohnenPlanungsursaechlich()
        );
        abfragevariantePatch.setSobonOrientierungswertJahr(SobonOrientierungswertJahr.JAHR_2017);
        abfragevariantePatch.setAnmerkung("Die Anmerkung Baugenehmigungsverfahren Patch Sachbearbeitung");
        abfragePatch.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevariantePatch));

        abfrage = this.abfrageService.patchInBearbeitungSachbearbeitung(abfragePatch, abfrage.getId());
        assertThat(
            ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevariantenBaugenehmigungsverfahren()
                .get(0)
                .getAnmerkung(),
            is("Die Anmerkung Baugenehmigungsverfahren Patch Sachbearbeitung")
        );

        abfrageRepository.deleteAll();
    }

    @Test
    @Transactional
    @MockCustomUser
    void patchInBearbeitungSachbearbeitungWeiteresVerfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createWeiteresVerfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);

        final var abfragePatch = new WeiteresVerfahrenInBearbeitungSachbearbeitungModel();
        abfragePatch.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        abfragePatch.setVersion(abfrage.getVersion());
        abfragePatch.setVerortung(((WeiteresVerfahrenModel) abfrage).getVerortung());
        abfragePatch.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());
        final var abfragevariantePatch =
            new AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungModel();
        abfragevariantePatch.setId(
            ((WeiteresVerfahrenModel) abfrage).getAbfragevariantenWeiteresVerfahren().get(0).getId()
        );
        abfragevariantePatch.setVersion(
            ((WeiteresVerfahrenModel) abfrage).getAbfragevariantenWeiteresVerfahren().get(0).getVersion()
        );
        abfragevariantePatch.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfragevariantePatch.setGfWohnenPlanungsursaechlich(
            ((WeiteresVerfahrenModel) abfrage).getAbfragevariantenWeiteresVerfahren()
                .get(0)
                .getGfWohnenPlanungsursaechlich()
        );
        abfragevariantePatch.setSobonOrientierungswertJahr(SobonOrientierungswertJahr.JAHR_2017);
        abfragevariantePatch.setAnmerkung("Die Anmerkung WeiteresVerfahren Patch Sachbearbeitung");
        abfragePatch.setAbfragevariantenWeiteresVerfahren(List.of(abfragevariantePatch));

        abfrage = this.abfrageService.patchInBearbeitungSachbearbeitung(abfragePatch, abfrage.getId());
        assertThat(
            ((WeiteresVerfahrenModel) abfrage).getAbfragevariantenWeiteresVerfahren().get(0).getAnmerkung(),
            is("Die Anmerkung WeiteresVerfahren Patch Sachbearbeitung")
        );

        abfrageRepository.deleteAll();
    }

    @Test
    @Transactional
    @MockCustomUser
    void patchInBearbeitungFachreferatBauleitplanverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        abfrage = this.abfrageService.save(abfrage);

        final var abfragePatch = new BauleitplanverfahrenInBearbeitungFachreferatModel();
        abfragePatch.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragePatch.setVersion(abfrage.getVersion());
        abfragePatch.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());
        final var abfragevariantePatch = new AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel();
        abfragevariantePatch.setId(
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren().get(0).getId()
        );
        abfragevariantePatch.setVersion(
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren().get(0).getVersion()
        );
        abfragevariantePatch.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        final var bedarfmeldungFachreferate = new BedarfsmeldungFachreferateModel();
        bedarfmeldungFachreferate.setAnzahlEinrichtungen(3);
        bedarfmeldungFachreferate.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERGARTEN);
        abfragevariantePatch.setBedarfsmeldungFachreferate(List.of(bedarfmeldungFachreferate));
        abfragePatch.setAbfragevariantenBauleitplanverfahren(List.of(abfragevariantePatch));

        abfrage = this.abfrageService.patchInBearbeitungFachreferat(abfragePatch, abfrage.getId());
        assertThat(
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren()
                .get(0)
                .getBedarfsmeldungFachreferate()
                .get(0)
                .getAnzahlEinrichtungen(),
            is(3)
        );
        assertThat(
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren()
                .get(0)
                .getBedarfsmeldungFachreferate()
                .get(0)
                .getInfrastruktureinrichtungTyp(),
            is(InfrastruktureinrichtungTyp.KINDERGARTEN)
        );

        abfrageRepository.deleteAll();
    }

    @Test
    @Transactional
    @MockCustomUser
    void patchInBearbeitungFachreferatBaugenehmigungsverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createBaugenehmigungsverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        abfrage = this.abfrageService.save(abfrage);

        final var abfragePatch = new BaugenehmigungsverfahrenInBearbeitungFachreferatModel();
        abfragePatch.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragePatch.setVersion(abfrage.getVersion());
        abfragePatch.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());
        final var abfragevariantePatch = new AbfragevarianteBaugenehmigungsverfahrenInBearbeitungFachreferatModel();
        abfragevariantePatch.setId(
            ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevariantenBaugenehmigungsverfahren().get(0).getId()
        );
        abfragevariantePatch.setVersion(
            ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevariantenBaugenehmigungsverfahren().get(0).getVersion()
        );
        abfragevariantePatch.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        final var bedarfmeldungFachreferate = new BedarfsmeldungFachreferateModel();
        bedarfmeldungFachreferate.setAnzahlEinrichtungen(2);
        bedarfmeldungFachreferate.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        abfragevariantePatch.setBedarfsmeldungFachreferate(List.of(bedarfmeldungFachreferate));
        abfragePatch.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevariantePatch));

        abfrage = this.abfrageService.patchInBearbeitungFachreferat(abfragePatch, abfrage.getId());
        assertThat(
            ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevariantenBaugenehmigungsverfahren()
                .get(0)
                .getBedarfsmeldungFachreferate()
                .get(0)
                .getAnzahlEinrichtungen(),
            is(2)
        );
        assertThat(
            ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevariantenBaugenehmigungsverfahren()
                .get(0)
                .getBedarfsmeldungFachreferate()
                .get(0)
                .getInfrastruktureinrichtungTyp(),
            is(InfrastruktureinrichtungTyp.KINDERKRIPPE)
        );

        abfrageRepository.deleteAll();
    }

    @Test
    @Transactional
    void patchInBearbeitungFachreferatWeiteresVerfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException {
        AbfrageModel abfrage = TestData.createWeiteresVerfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        abfrage = this.abfrageService.save(abfrage);

        final var abfragePatch = new WeiteresVerfahrenInBearbeitungFachreferatModel();
        abfragePatch.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        abfragePatch.setVersion(abfrage.getVersion());
        abfragePatch.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());
        final var abfragevariantePatch = new AbfragevarianteWeiteresVerfahrenInBearbeitungFachreferatModel();
        abfragevariantePatch.setId(
            ((WeiteresVerfahrenModel) abfrage).getAbfragevariantenWeiteresVerfahren().get(0).getId()
        );
        abfragevariantePatch.setVersion(
            ((WeiteresVerfahrenModel) abfrage).getAbfragevariantenWeiteresVerfahren().get(0).getVersion()
        );
        abfragevariantePatch.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        final var bedarfmeldungFachreferate = new BedarfsmeldungFachreferateModel();
        bedarfmeldungFachreferate.setAnzahlEinrichtungen(2);
        bedarfmeldungFachreferate.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        abfragevariantePatch.setBedarfsmeldungFachreferate(List.of(bedarfmeldungFachreferate));
        abfragePatch.setAbfragevariantenWeiteresVerfahren(List.of(abfragevariantePatch));

        abfrage = this.abfrageService.patchInBearbeitungFachreferat(abfragePatch, abfrage.getId());
        assertThat(
            ((WeiteresVerfahrenModel) abfrage).getAbfragevariantenWeiteresVerfahren()
                .get(0)
                .getBedarfsmeldungFachreferate()
                .get(0)
                .getAnzahlEinrichtungen(),
            is(2)
        );
        assertThat(
            ((WeiteresVerfahrenModel) abfrage).getAbfragevariantenWeiteresVerfahren()
                .get(0)
                .getBedarfsmeldungFachreferate()
                .get(0)
                .getInfrastruktureinrichtungTyp(),
            is(InfrastruktureinrichtungTyp.KINDERKRIPPE)
        );

        abfrageRepository.deleteAll();
    }
}
