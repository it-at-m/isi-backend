package de.muenchen.isi.domain.service;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.configuration.StateMachineConfiguration;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AbfrageStatusServiceTest {

    @Test
    void freigabeInfrasturkturabfrage() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final AbfrageStatusService abfrageStatusService = Mockito.mock(AbfrageStatusService.class);
        final var uuid = UUID.randomUUID();

        abfrageStatusService.freigabeAbfrage(uuid);

        Mockito.verify(abfrageStatusService, Mockito.times(1)).freigabeAbfrage(uuid);
    }

    @Test
    void freigabeInfrastrukturAbfrageException() throws  EntityNotFoundException, AbfrageStatusNotAllowedException {
        final AbfrageStatusService abfrageStatusService = Mockito.mock(AbfrageStatusService.class);

        final var uuid = UUID.randomUUID();

        Mockito.doThrow(new EntityNotFoundException("Entität nicht gefunden")).when(abfrageStatusService).freigabeAbfrage(Mockito.any(UUID.class));
        abfrageStatusService.freigabeAbfrage(uuid);
    }
}
