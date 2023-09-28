package de.muenchen.isi.domain.service.common;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.mapper.KommentarDomainMapper;
import de.muenchen.isi.domain.model.common.KommentarModel;
import de.muenchen.isi.infrastructure.repository.common.KommentarRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KommentarService {

    private final KommentarRepository kommentarRepository;

    private final KommentarDomainMapper kommentarMapper;

    public List<KommentarModel> getKommentareForBauvorhaben(final UUID bauvorhabenId) {
        return kommentarRepository
            .findAllByBauvorhabenOrderByCreatedDateTimeDesc(bauvorhabenId)
            .map(kommentarMapper::entity2Model)
            .collect(Collectors.toList());
    }

    public List<KommentarModel> getKommentareForInfrastruktureinrichtung(final UUID infrastruktureinrichtungId) {
        return kommentarRepository
            .findAllByInfrastruktureinrichtungOrderByCreatedDateTimeDesc(infrastruktureinrichtungId)
            .map(kommentarMapper::entity2Model)
            .collect(Collectors.toList());
    }

    public KommentarModel getKommentarById(final UUID id) throws EntityNotFoundException {
        final var entity = kommentarRepository
            .findById(id)
            .orElseThrow(() -> {
                final var message = "Kommentar nicht gefunden.";
                log.error(message);
                return new EntityNotFoundException(message);
            });
        return kommentarMapper.entity2Model(entity);
    }

    public KommentarModel saveKommentar(final KommentarModel kommentar) throws OptimisticLockingException {
        var entity = kommentarMapper.model2Entity(kommentar);
        try {
            entity = kommentarRepository.saveAndFlush(entity);
        } catch (final ObjectOptimisticLockingFailureException exception) {
            final var message =
                "Der Kommentar wurde in der Zwischenzeit geändert. Bitte öffnen Sie die Kommentare neu!";
            throw new OptimisticLockingException(message, exception);
        }
        return kommentarMapper.entity2Model(entity);
    }

    public KommentarModel updateKommentar(final KommentarModel kommentar)
        throws EntityNotFoundException, OptimisticLockingException {
        this.getKommentarById(kommentar.getId());
        return this.saveKommentar(kommentar);
    }

    public void deleteKommentarById(final UUID id) {
        kommentarRepository.deleteById(id);
    }
}
