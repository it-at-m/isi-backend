package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.TestData;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.ReportingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.BedarfsmeldungModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung.AbfragevarianteBaugenehmigungsverfahrenEinpflegenBedarfsmeldungModel;
import de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung.AbfragevarianteBauleitplanverfahrenEinpflegenBedarfsmeldungModel;
import de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung.AbfragevarianteWeiteresVerfahrenEinpflegenBedarfsmeldungModel;
import de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung.BaugenehmigungsverfahrenEinpflegenBedarfsmeldungModel;
import de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung.BauleitplanverfahrenEinpflegenBedarfsmeldungModel;
import de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung.WeiteresVerfahrenEinpflegenBedarfsmeldungModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.AbfragevarianteBaugenehmigungsverfahrenSachbearbeitungStartBearbeitungModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.AbfragevarianteBauleitplanverfahrenSachbearbeitungStartBearbeitungModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.AbfragevarianteWeiteresVerfahrenSachbearbeitungStartBearbeitungModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.BaugenehmigungsverfahrenStartBearbeitungModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.BauleitplanverfahrenStartBearbeitungModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.WeiteresVerfahrenStartBearbeitungModel;
import de.muenchen.isi.domain.model.common.SobonBerechnungModel;
import de.muenchen.isi.domain.service.calculation.CalculationService;
import de.muenchen.isi.domain.service.etlInterface.EtlInterfaceService;
import de.muenchen.isi.domain.service.reporting.ReportingdataTransferService;
import de.muenchen.isi.domain.service.transition.MockCustomUser;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    private CalculationService calculationService;

    @MockBean
    private ReportingdataTransferService reportingdataTransferService;

    @MockBean
    private EtlInterfaceService etlInterfaceService;

    @Test
    @Transactional
    @MockCustomUser(roles = { "anwender" })
    void getAbfrageInStatusErledigtMitFachreferateRoleAnwender()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, UserRoleNotAllowedException, CalculationException, ReportingException {
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
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, UserRoleNotAllowedException, CalculationException, ReportingException {
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
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, UserRoleNotAllowedException, CalculationException, ReportingException {
        AbfrageModel abfrageModel = TestData.createBauleitplanverfahrenModel();
        abfrageModel = this.abfrageService.save(abfrageModel);

        var result = this.abfrageService.getById(abfrageModel.getId());

        assertThat(result, is(abfrageModel));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "anwender" })
    void throwUserRoleNotAllowedExceptionWhenStatusIsNotErledigt()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, CalculationException, ReportingException {
        AbfrageModel abfrageModel = TestData.createBauleitplanverfahrenModel();
        abfrageModel = this.abfrageService.save(abfrageModel);

        final var uuid = abfrageModel.getId();
        Assertions.assertThrows(UserRoleNotAllowedException.class, () -> this.abfrageService.getById(uuid));

        abfrageModel.setStatusAbfrage(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);
        abfrageModel = this.abfrageService.save(abfrageModel);
        Assertions.assertThrows(UserRoleNotAllowedException.class, () -> this.abfrageService.getById(uuid));

        abfrageModel.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);
        abfrageModel = this.abfrageService.save(abfrageModel);
        Assertions.assertThrows(UserRoleNotAllowedException.class, () -> this.abfrageService.getById(uuid));

        abfrageModel.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);
        abfrageModel = this.abfrageService.save(abfrageModel);
        Assertions.assertThrows(UserRoleNotAllowedException.class, () -> this.abfrageService.getById(uuid));

        abfrageModel.setStatusAbfrage(StatusAbfrage.EINPLANUNG_BEDARFE);
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
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, UserRoleNotAllowedException, CalculationException, ReportingException {
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        UUID abfragevarianteId =
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren().get(0).getId();
        AbfrageModel foundAbfrage = abfrageService.getByAbfragevarianteId(abfragevarianteId);
        assertThat(foundAbfrage, is(abfrage));

        abfrage = TestData.createBaugenehmigungsverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfragevarianteId = ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevariantenBaugenehmigungsverfahren()
            .get(0)
            .getId();
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
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, CalculationException, ReportingException {
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        this.abfrageService.save(abfrage);

        Assertions.assertThrows(EntityNotFoundException.class, () ->
            this.abfrageService.getByAbfragevarianteId(UUID.randomUUID())
        );
        abfrageRepository.deleteAll();
    }

    @Test
    @Transactional
    @MockCustomUser
    void patchAngelegtBauleitplanverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException, CalculationException, ReportingException {
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();

        abfrage = this.abfrageService.save(abfrage);

        AbfrageAngelegtModel abfrageAngelegt = TestData.createBauleitplanverfahrenAngelegtModel();
        abfrageAngelegt.setVersion(abfrage.getVersion());

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
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createBaugenehmigungsverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);

        AbfrageAngelegtModel abfrageAngelegt = TestData.createBaugenehmigungsverfahrenAngelegtModel();
        abfrageAngelegt.setVersion(abfrage.getVersion());

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
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException, CalculationException, ReportingException {
        AbfrageModel abfrage = TestData.createWeiteresVerfahrenModel();
        abfrage = this.abfrageService.save(abfrage);

        AbfrageAngelegtModel abfrageAngelegt = TestData.createWeiteresVerfahrenAngelegtModel();
        abfrageAngelegt.setVersion(abfrage.getVersion());

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
    void patchStartBearbeitungBauleitplanverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);

        final var sobonBerechnung = new SobonBerechnungModel();
        sobonBerechnung.setIsASobonBerechnung(true);
        sobonBerechnung.setSobonFoerdermix(new FoerdermixModel());

        final var abfragePatch = new BauleitplanverfahrenStartBearbeitungModel();
        abfragePatch.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragePatch.setVersion(abfrage.getVersion());
        abfragePatch.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());
        final var abfragevariantePatch = new AbfragevarianteBauleitplanverfahrenSachbearbeitungStartBearbeitungModel();
        abfragevariantePatch.setId(
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren().get(0).getId()
        );
        abfragevariantePatch.setVersion(
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren().get(0).getVersion()
        );
        abfragevariantePatch.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevariantePatch.setSobonOrientierungswertJahrPlanungsursaechlich(SobonOrientierungswertJahr.JAHR_2017);
        abfragevariantePatch.setSobonBerechnung(sobonBerechnung);
        abfragevariantePatch.setAnmerkung("Die Anmerkung Bauleitplanverfahren Patch Sachbearbeitung");
        abfragePatch.setAbfragevariantenBauleitplanverfahren(List.of(abfragevariantePatch));

        abfrage = this.abfrageService.patchStartBearbeitung(abfragePatch, abfrage.getId());
        assertThat(
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren().get(0).getAnmerkung(),
            is("Die Anmerkung Bauleitplanverfahren Patch Sachbearbeitung")
        );

        abfrageRepository.deleteAll();
    }

    @Test
    @Transactional
    @MockCustomUser
    void patchStartBearbeitungBaugenehmigungsverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createBaugenehmigungsverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);

        final var abfragePatch = new BaugenehmigungsverfahrenStartBearbeitungModel();
        abfragePatch.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragePatch.setVersion(abfrage.getVersion());
        abfragePatch.setVerortung(((BaugenehmigungsverfahrenModel) abfrage).getVerortung());
        abfragePatch.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());
        final var abfragevariantePatch =
            new AbfragevarianteBaugenehmigungsverfahrenSachbearbeitungStartBearbeitungModel();
        abfragevariantePatch.setId(
            ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevariantenBaugenehmigungsverfahren().get(0).getId()
        );
        abfragevariantePatch.setVersion(
            ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevariantenBaugenehmigungsverfahren().get(0).getVersion()
        );
        abfragevariantePatch.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragevariantePatch.setSobonOrientierungswertJahrPlanungsursaechlich(SobonOrientierungswertJahr.JAHR_2017);
        abfragevariantePatch.setAnmerkung("Die Anmerkung Baugenehmigungsverfahren Patch Sachbearbeitung");
        abfragePatch.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevariantePatch));

        abfrage = this.abfrageService.patchStartBearbeitung(abfragePatch, abfrage.getId());
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
    void patchStartBearbeitungWeiteresVerfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createWeiteresVerfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);

        final var sobonBerechnung = new SobonBerechnungModel();
        sobonBerechnung.setIsASobonBerechnung(true);
        sobonBerechnung.setSobonFoerdermix(new FoerdermixModel());

        final var abfragePatch = new WeiteresVerfahrenStartBearbeitungModel();
        abfragePatch.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        abfragePatch.setVersion(abfrage.getVersion());
        abfragePatch.setVerortung(((WeiteresVerfahrenModel) abfrage).getVerortung());
        abfragePatch.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());
        final var abfragevariantePatch = new AbfragevarianteWeiteresVerfahrenSachbearbeitungStartBearbeitungModel();
        abfragevariantePatch.setId(
            ((WeiteresVerfahrenModel) abfrage).getAbfragevariantenWeiteresVerfahren().get(0).getId()
        );
        abfragevariantePatch.setVersion(
            ((WeiteresVerfahrenModel) abfrage).getAbfragevariantenWeiteresVerfahren().get(0).getVersion()
        );
        abfragevariantePatch.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfragevariantePatch.setSobonOrientierungswertJahrPlanungsursaechlich(SobonOrientierungswertJahr.JAHR_2017);
        abfragevariantePatch.setSobonBerechnung(sobonBerechnung);
        abfragevariantePatch.setAnmerkung("Die Anmerkung WeiteresVerfahren Patch Sachbearbeitung");
        abfragePatch.setAbfragevariantenWeiteresVerfahren(List.of(abfragevariantePatch));

        abfrage = this.abfrageService.patchStartBearbeitung(abfragePatch, abfrage.getId());
        assertThat(
            ((WeiteresVerfahrenModel) abfrage).getAbfragevariantenWeiteresVerfahren().get(0).getAnmerkung(),
            is("Die Anmerkung WeiteresVerfahren Patch Sachbearbeitung")
        );

        abfrageRepository.deleteAll();
    }

    @Test
    @Transactional
    @MockCustomUser
    void patchEinpflegenBedarfsmeldungBauleitplanverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);
        abfrage = this.abfrageService.save(abfrage);

        final var abfragePatch = new BauleitplanverfahrenEinpflegenBedarfsmeldungModel();
        abfragePatch.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragePatch.setVersion(abfrage.getVersion());
        abfragePatch.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());
        final var abfragevariantePatch = new AbfragevarianteBauleitplanverfahrenEinpflegenBedarfsmeldungModel();
        abfragevariantePatch.setId(
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren().get(0).getId()
        );
        abfragevariantePatch.setVersion(
            ((BauleitplanverfahrenModel) abfrage).getAbfragevariantenBauleitplanverfahren().get(0).getVersion()
        );
        abfragevariantePatch.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        final var bedarfmeldungFachreferate = new BedarfsmeldungModel();
        bedarfmeldungFachreferate.setAnzahlEinrichtungen(3);
        bedarfmeldungFachreferate.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERGARTEN);
        abfragevariantePatch.setBedarfsmeldungFachreferate(List.of(bedarfmeldungFachreferate));
        abfragePatch.setAbfragevariantenBauleitplanverfahren(List.of(abfragevariantePatch));

        abfrage = this.abfrageService.patchEinpflegenBedarfsmeldung(abfragePatch, abfrage.getId());
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
    void patchEinpflegenBedarfsmeldungBaugenehmigungsverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createBaugenehmigungsverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);
        abfrage = this.abfrageService.save(abfrage);

        final var abfragePatch = new BaugenehmigungsverfahrenEinpflegenBedarfsmeldungModel();
        abfragePatch.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragePatch.setVersion(abfrage.getVersion());
        abfragePatch.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());
        final var abfragevariantePatch = new AbfragevarianteBaugenehmigungsverfahrenEinpflegenBedarfsmeldungModel();
        abfragevariantePatch.setId(
            ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevariantenBaugenehmigungsverfahren().get(0).getId()
        );
        abfragevariantePatch.setVersion(
            ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevariantenBaugenehmigungsverfahren().get(0).getVersion()
        );
        abfragevariantePatch.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        final var bedarfmeldungFachreferate = new BedarfsmeldungModel();
        bedarfmeldungFachreferate.setAnzahlEinrichtungen(2);
        bedarfmeldungFachreferate.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        abfragevariantePatch.setBedarfsmeldungFachreferate(List.of(bedarfmeldungFachreferate));
        abfragePatch.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevariantePatch));

        abfrage = this.abfrageService.patchEinpflegenBedarfsmeldung(abfragePatch, abfrage.getId());
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
    @MockCustomUser
    void patchEinpflegenBedarfsmeldungWeiteresVerfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        AbfrageModel abfrage = TestData.createWeiteresVerfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);
        abfrage = this.abfrageService.save(abfrage);

        final var abfragePatch = new WeiteresVerfahrenEinpflegenBedarfsmeldungModel();
        abfragePatch.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        abfragePatch.setVersion(abfrage.getVersion());
        abfragePatch.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());
        final var abfragevariantePatch = new AbfragevarianteWeiteresVerfahrenEinpflegenBedarfsmeldungModel();
        abfragevariantePatch.setId(
            ((WeiteresVerfahrenModel) abfrage).getAbfragevariantenWeiteresVerfahren().get(0).getId()
        );
        abfragevariantePatch.setVersion(
            ((WeiteresVerfahrenModel) abfrage).getAbfragevariantenWeiteresVerfahren().get(0).getVersion()
        );
        abfragevariantePatch.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        final var bedarfmeldungFachreferate = new BedarfsmeldungModel();
        bedarfmeldungFachreferate.setAnzahlEinrichtungen(2);
        bedarfmeldungFachreferate.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        abfragevariantePatch.setBedarfsmeldungFachreferate(List.of(bedarfmeldungFachreferate));
        abfragePatch.setAbfragevariantenWeiteresVerfahren(List.of(abfragevariantePatch));

        abfrage = this.abfrageService.patchEinpflegenBedarfsmeldung(abfragePatch, abfrage.getId());
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
