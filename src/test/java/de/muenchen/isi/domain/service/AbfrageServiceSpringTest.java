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
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
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
    void getByAbfragevarianteId() throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException {
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        UUID abfragevarianteId = ((BauleitplanverfahrenModel) abfrage).getAbfragevarianten().get(0).getId();
        AbfrageModel foundAbfrage = abfrageService.getByAbfragevarianteId(abfragevarianteId);
        assertThat(foundAbfrage, is(abfrage));

        abfrage = TestData.createBaugenehmigungsverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfragevarianteId = ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevarianten().get(0).getId();
        foundAbfrage = abfrageService.getByAbfragevarianteId(abfragevarianteId);
        assertThat(foundAbfrage, is(abfrage));

        abfrageRepository.deleteAll();
    }

    @Test
    @Transactional
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
    void patchAngelegtBauleitplanverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException, AbfrageStatusNotAllowedException {
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);

        AbfrageAngelegtModel abfrageAngelegt = TestData.createBauleitplanverfahrenAngelegtModel();

        abfrage = this.abfrageService.patchAngelegt(abfrageAngelegt, abfrage.getId());
        assertThat(abfrage.getName(), is("Neubausiedlung in Musterort 2"));
        assertThat(
            ((BauleitplanverfahrenModel) abfrage).getAbfragevarianten().get(0).getName(),
            is("Name Abfragevariante 102")
        );

        abfrageRepository.deleteAll();
    }

    @Test
    @Transactional
    void patchAngelegtBaugenehmigungsverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException, AbfrageStatusNotAllowedException {
        AbfrageModel abfrage = TestData.createBaugenehmigungsverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);

        AbfrageAngelegtModel abfrageAngelegt = TestData.createBaugenehmigungsverfahrenAngelegtModel();

        abfrage = this.abfrageService.patchAngelegt(abfrageAngelegt, abfrage.getId());
        assertThat(abfrage.getName(), is("Altbausiedlung in Musterort 2"));
        assertThat(
            ((BaugenehmigungsverfahrenModel) abfrage).getAbfragevarianten().get(0).getName(),
            is("Name Abfragevariante 112")
        );

        abfrageRepository.deleteAll();
    }
}
