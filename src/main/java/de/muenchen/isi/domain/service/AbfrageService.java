package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbfrageService {

    private final AbfrageRepository abfrageRepository;

    private final AbfrageDomainMapper abfrageDomainMapper;

    private final DokumentService dokumentService;

    private final AuthenticationUtils authenticationUtils;

    /**
     * Die Methode gibt ein {@link AbfrageModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link AbfrageModel}.
     * @return {@link AbfrageModel}.
     * @throws EntityNotFoundException falls die Abfrage identifiziert durch die {@link AbfrageModel#getId()} nicht gefunden wird.
     */
    public AbfrageModel getAbfrageById(final UUID id) throws EntityNotFoundException {
        final var optAbfrage = this.abfrageRepository.findById(id);
        final var abfrage = optAbfrage.orElseThrow(() -> {
            final var message = "Abfrage nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.abfrageDomainMapper.entity2Model(abfrage);
    }

    public AbfrageModel saveAbfrage(final AbfrageModel abfrage)
        throws EntityNotFoundException, OptimisticLockingException, UniqueViolationException {
        if (abfrage.getId() == null) {
            abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
            abfrage.setSub(authenticationUtils.getUserSub());
        }
        var entity = this.abfrageDomainMapper.model2Entity(abfrage);
        final var saved = this.abfrageRepository.findByNameIgnoreCase(abfrage.getName());
        if ((saved.isPresent() && saved.get().getId().equals(entity.getId())) || saved.isEmpty()) {
            try {
                entity = this.abfrageRepository.saveAndFlush(entity);
            } catch (final ObjectOptimisticLockingFailureException exception) {
                final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
                throw new OptimisticLockingException(message, exception);
            } catch (final DataIntegrityViolationException exception) {
                final var message =
                    "Der angegebene Name der Abfragevariante ist schon vorhanden, bitte wählen Sie daher einen anderen Namen und speichern Sie die Abfrage erneut.";
                throw new UniqueViolationException(message);
            }
            return this.abfrageDomainMapper.entity2Model(entity);
        } else {
            throw new UniqueViolationException(
                "Der angegebene Name der Abfrage ist schon vorhanden, bitte wählen Sie daher einen anderen Namen und speichern Sie die Abfrage erneut."
            );
        }
    }
}
