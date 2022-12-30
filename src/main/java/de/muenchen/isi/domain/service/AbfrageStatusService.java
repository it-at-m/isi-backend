package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbfrageStatusService {

    private final AbfrageService abfrageService;

    /**
     * Gibt ein {@link InfrastrukturabfrageModel} identifiziert durch die ID frei.
     *
     * @param id zur Identifzierung des {@link InfrastrukturabfrageModel}s
     * @throws EntityNotFoundException          falls die Abfrage nicht gefunden werden kann.
     * @throws AbfrageStatusNotAllowedException falls die Abfrage nicht freigegeben werden kann.
     */
    public void freigabeInfrastrukturabfrage(final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final InfrastrukturabfrageModel abfrage = this.abfrageService
                .getInfrastrukturabfrageById(id);
        abfrage.setAbfrage(this.freigabeAbfrage(abfrage.getAbfrage(), id));
        this.abfrageService.updateInfrastrukturabfrage(abfrage);

    }

    /**
     * Gibt das {@link AbfrageModel} im Parameter frei falls möglich.
     *
     * @param abfrage vom Typ {@link AbfrageModel} zur Freigabe.
     * @return das freigegebene {@link AbfrageModel}.
     * @throws AbfrageStatusNotAllowedException falls die Abfrage nicht freigegeben werden kann.
     */
    protected AbfrageModel freigabeAbfrage(final AbfrageModel abfrage, final UUID id) throws AbfrageStatusNotAllowedException {
        if (Objects.equals(abfrage.getStatusAbfrage(), StatusAbfrage.ANGELEGT)) {
            abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);
            return abfrage;
        } else {
            final var message = String.format(
                    "Abfrage mit ID %s kann nicht freigegeben werden, da diese sich im Status %s befindet.",
                    id.toString(),
                    abfrage.getStatusAbfrage()
            );
            log.error(message);
            throw new AbfrageStatusNotAllowedException(message);
        }
    }

}
