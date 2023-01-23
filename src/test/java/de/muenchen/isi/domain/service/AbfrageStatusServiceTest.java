package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AbfrageStatusServiceTest {

    @Mock
    private AbfrageService abfrageService;

    private AbfrageStatusService abfrageStatusService;

    @BeforeEach
    public void beforeEach() {
        this.abfrageStatusService = new AbfrageStatusService(
                this.abfrageService
        );
        Mockito.reset(this.abfrageService);
    }

    @Test
    void freigabeInfrasturkturabfrage() throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        this.abfrageStatusService.freigabeInfrastrukturabfrage(uuid);

        final InfrastrukturabfrageModel abfrageExpected = new InfrastrukturabfrageModel();
        abfrageExpected.setId(uuid);
        abfrageExpected.setAbfrage(new AbfrageModel());
        abfrageExpected.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);

        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrageExpected);
    }

    @Test
    void freigabeInfrastrukturabfrageException() throws EntityNotFoundException {
        final var uuid = UUID.randomUUID();

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenThrow(new EntityNotFoundException("test"));
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageStatusService.freigabeInfrastrukturabfrage(uuid));
        Mockito.reset(this.abfrageService);

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.freigabeInfrastrukturabfrage(uuid));
    }

    @Test
    void freigabeAbfrage() throws AbfrageStatusNotAllowedException {
        final AbfrageModel abfrage = new AbfrageModel();
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        final AbfrageModel result = this.abfrageStatusService.freigabeAbfrage(abfrage, UUID.randomUUID());
        final AbfrageModel expected = new AbfrageModel();
        expected.setStatusAbfrage(StatusAbfrage.OFFEN);
        assertThat(result, is(expected));

        abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.freigabeAbfrage(abfrage, UUID.randomUUID()));

        abfrage.setStatusAbfrage(StatusAbfrage.IN_ARBEIT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.freigabeAbfrage(abfrage, UUID.randomUUID()));

        abfrage.setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.freigabeAbfrage(abfrage, UUID.randomUUID()));

        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.freigabeAbfrage(abfrage, UUID.randomUUID()));

        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.freigabeAbfrage(abfrage, UUID.randomUUID()));

        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.freigabeAbfrage(abfrage, UUID.randomUUID()));

        abfrage.setStatusAbfrage(null);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.freigabeAbfrage(abfrage, UUID.randomUUID()));
    }

}
