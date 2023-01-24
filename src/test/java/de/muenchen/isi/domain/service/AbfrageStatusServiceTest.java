package de.muenchen.isi.domain.service;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.configuration.StateMachineConfiguration;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.infrastructure.entity.Abfrage;
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

@SpringBootTest(
        classes = {IsiBackendApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"tomcat.gracefulshutdown.pre-wait-seconds=0"})
@ActiveProfiles(profiles = {"local", "no-security"})

@MockitoSettings(strictness = Strictness.LENIENT)
class AbfrageStatusServiceTest {

    @Test
    void freigabeInfrasturkturabfrage() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final AbfrageStatusService abfrageStatusService = Mockito.mock(AbfrageStatusService.class);
        final AbfrageService abfrageService = Mockito.mock(AbfrageService.class);
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        Mockito.when(abfrageService.updateInfrastrukturabfrage(abfrage)).thenReturn(abfrage);

        abfrageStatusService.freigabeAbfrage(uuid);

        Mockito.verify(abfrageStatusService, Mockito.times(1)).freigabeAbfrage(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
    }
}
