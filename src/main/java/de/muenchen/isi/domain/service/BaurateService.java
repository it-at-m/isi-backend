package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.BaurateDomainMapper;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.infrastructure.repository.BaurateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaurateService {

    private final BaurateDomainMapper baurateDomainMapper;

    private final BaurateRepository baurateRepository;

    public List<BaurateModel> getBauraten() {
        return this.baurateRepository.findAllByOrderByJahrDesc()
                .map(this.baurateDomainMapper::entity2Model)
                .collect(Collectors.toList());
    }

    /**
     * Die Methode gibt ein {@link BaurateModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link BaurateModel}.
     * @return {@link BaurateModel}.
     */
    public BaurateModel getBaurateById(final UUID id) throws EntityNotFoundException {
        final var optEntity = this.baurateRepository.findById(id);
        final var entity = optEntity.orElseThrow(() -> {
            final var message = "Baurate nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.baurateDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode speichert ein {@link BaurateModel}.
     *
     * @param baurate zum Speichern.
     * @return das gespeicherte {@link BaurateModel}.
     */
    public BaurateModel saveBaurate(final BaurateModel baurate) {
        var entity = this.baurateDomainMapper.model2entity(baurate);
        entity = this.baurateRepository.save(entity);
        return this.baurateDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode updated ein {@link BaurateModel}.
     *
     * @param baurate zum Updaten.
     * @return das geupdatete {@link BaurateModel}.
     * @throws EntityNotFoundException falls die Abfrage identifiziert durch die {@link BaurateModel#getId()} nicht gefunden wird.
     */
    public BaurateModel updateBaurate(final BaurateModel baurate) throws EntityNotFoundException {
        this.getBaurateById(baurate.getId());
        return this.saveBaurate(baurate);
    }

    /**
     * Diese Methode löscht ein {@link BaurateModel}.
     *
     * @param id zum Identifizieren des {@link BaurateModel}.
     * @throws EntityNotFoundException falls die Abfrage identifiziert durch die {@link BaurateModel#getId()} nicht gefunden wird.
     */
    public void deleteBaurateById(final UUID id) throws EntityNotFoundException {
        final var model = this.getBaurateById(id);
        this.baurateRepository.deleteById(model.getId());
    }

}
