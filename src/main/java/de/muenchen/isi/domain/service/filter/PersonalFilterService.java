package de.muenchen.isi.domain.service.filter;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.MaxCreationsReachedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.mapper.PersonalFilterDomainMapper;
import de.muenchen.isi.domain.model.filter.PersonalFilterRequestModel;
import de.muenchen.isi.domain.model.filter.PersonalFilterResponseModel;
import de.muenchen.isi.domain.service.benutzer.BenutzerService;
import de.muenchen.isi.infrastructure.entity.benutzer.Benutzer;
import de.muenchen.isi.infrastructure.entity.filter.PersoenlicherFilter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Verwaltet die persönlichen Filter eines Nutzers.
 * <p>
 * Die Filter sind ein Attribut des {@link Benutzer} und werden dort als JSON-Liste gehalten. Alle
 * Operationen arbeiten deshalb auf dieser Liste und speichern anschließend den gesamten Datensatz.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PersonalFilterService {

    private static final int MAX_PERSONAL_FILTERS = 10;

    static final String FEHLERMELDUNG_NICHT_AUTHENTIFIZIERT =
        "Sie müssen authentifiziert sein, um mit persönlichen Filtern interagieren zu können";

    private final PersonalFilterDomainMapper personalFilterDomainMapper;

    private final BenutzerService benutzerService;

    /**
     * Gibt alle eigenen persönlichen Filter zurück, absteigend nach letzter Änderung.
     *
     * @return alle von diesem Nutzer existierenden persönlichen Filter als Liste
     * @throws UserRoleNotAllowedException falls der Nutzer nicht authentifiziert ist
     */
    public List<PersonalFilterResponseModel> getPersonalFilters() throws UserRoleNotAllowedException {
        final var filter = new ArrayList<>(this.getPersoenlicheFilter());
        filter.sort(
            Comparator.comparing(
                PersoenlicherFilter::getLastModifiedDateTime,
                Comparator.nullsLast(Comparator.reverseOrder())
            )
        );
        return personalFilterDomainMapper.entities2Models(filter);
    }

    /**
     * Gibt einen spezifisch angefragten persönlichen Filter zurück.
     *
     * @param filterId des zu lesenden persönlichen Filters
     * @return den persönlichen Filter mit dieser ID
     * @throws EntityNotFoundException falls der Nutzer keinen persönlichen Filter mit dieser ID besitzt
     * @throws UserRoleNotAllowedException falls der Nutzer nicht authentifiziert ist
     */
    public PersonalFilterResponseModel getByFilterID(final UUID filterId)
        throws EntityNotFoundException, UserRoleNotAllowedException {
        return personalFilterDomainMapper.entity2Model(this.getFilter(this.getPersoenlicheFilter(), filterId));
    }

    /**
     * Aktualisiert einen spezifizierten persönlichen Filter.
     *
     * @param personalFilterRequestModel entspricht neuen persönlichen Filtereinstellungen, die bestehende Filtereinstellungen überschreiben sollen
     * @return den aktualisierten persönlichen Filter
     * @throws EntityNotFoundException falls der Nutzer keinen persönlichen Filter mit dieser ID besitzt
     * @throws OptimisticLockingException falls es bereits eine neuere Version der Entität in der Datenbank gibt
     * @throws UserRoleNotAllowedException falls der Nutzer nicht authentifiziert ist
     */
    public PersonalFilterResponseModel update(final PersonalFilterRequestModel personalFilterRequestModel)
        throws EntityNotFoundException, OptimisticLockingException, UserRoleNotAllowedException {
        final var benutzer = benutzerService.getOrCreateBenutzer(FEHLERMELDUNG_NICHT_AUTHENTIFIZIERT);
        final var filter = this.getFilter(benutzer.getPersoenlicheFilter(), personalFilterRequestModel.getId());
        personalFilterDomainMapper.updateEntityFromModel(personalFilterRequestModel, filter);
        filter.setLastModifiedDateTime(LocalDateTime.now());
        benutzerService.save(benutzer);
        return personalFilterDomainMapper.entity2Model(filter);
    }

    /**
     * Diese Methode speichert ein {@link PersonalFilterRequestModel}.
     *
     * @param personalFilterRequestModel entspricht dem persönlichen Filter, der gespeichert werden soll
     * @return das gespeicherte {@link PersonalFilterResponseModel}.
     * @throws OptimisticLockingException falls es bereits eine neuere Version der Entität in der Datenbank gibt
     * @throws UserRoleNotAllowedException falls der Nutzer nicht authentifiziert ist
     * @throws MaxCreationsReachedException falls der Nutzer bereits die maximale Anzahl an Filter erstellt hat
     */
    public PersonalFilterResponseModel save(final PersonalFilterRequestModel personalFilterRequestModel)
        throws OptimisticLockingException, UserRoleNotAllowedException, MaxCreationsReachedException {
        final var benutzer = benutzerService.getOrCreateBenutzer(FEHLERMELDUNG_NICHT_AUTHENTIFIZIERT);
        if (benutzer.getPersoenlicheFilter() == null) {
            benutzer.setPersoenlicheFilter(new ArrayList<>());
        }
        if (benutzer.getPersoenlicheFilter().size() >= MAX_PERSONAL_FILTERS) {
            throw new MaxCreationsReachedException(
                "Maximale Anzahl an persönlichen Filtern erreicht (" + MAX_PERSONAL_FILTERS + ")."
            );
        }
        final var filter = personalFilterDomainMapper.model2Entity(personalFilterRequestModel);
        final var jetzt = LocalDateTime.now();
        filter.setId(UUID.randomUUID());
        filter.setCreatedDateTime(jetzt);
        filter.setLastModifiedDateTime(jetzt);
        benutzer.getPersoenlicheFilter().add(filter);
        benutzerService.save(benutzer);
        return personalFilterDomainMapper.entity2Model(filter);
    }

    /**
     * Löscht einen spezifisch angegebenen Filter.
     *
     * @param filterId des zu löschenden persönlichen Filters
     * @throws EntityNotFoundException falls der Nutzer keinen persönlichen Filter mit dieser ID besitzt
     * @throws OptimisticLockingException falls es bereits eine neuere Version der Entität in der Datenbank gibt
     * @throws UserRoleNotAllowedException falls der Nutzer nicht authentifiziert ist
     */
    public void delete(final UUID filterId)
        throws EntityNotFoundException, OptimisticLockingException, UserRoleNotAllowedException {
        final var benutzer = benutzerService.getOrCreateBenutzer(FEHLERMELDUNG_NICHT_AUTHENTIFIZIERT);
        final var filter = this.getFilter(benutzer.getPersoenlicheFilter(), filterId);
        benutzer.getPersoenlicheFilter().remove(filter);
        benutzerService.save(benutzer);
    }

    /**
     * @return die persönlichen Filter des authentifizierten Nutzers, niemals {@code null}.
     * @throws UserRoleNotAllowedException falls der Nutzer nicht authentifiziert ist
     */
    private List<PersoenlicherFilter> getPersoenlicheFilter() throws UserRoleNotAllowedException {
        return benutzerService
            .findBenutzer(FEHLERMELDUNG_NICHT_AUTHENTIFIZIERT)
            .map(Benutzer::getPersoenlicheFilter)
            .orElseGet(List::of);
    }

    /**
     * @param persoenlicheFilter des Nutzers
     * @param filterId des gesuchten persönlichen Filters
     * @return den gefundenen Filter sofern keine Exception geworfen wurde
     * @throws EntityNotFoundException falls der Nutzer keinen persönlichen Filter mit dieser ID besitzt
     */
    private PersoenlicherFilter getFilter(final List<PersoenlicherFilter> persoenlicheFilter, final UUID filterId)
        throws EntityNotFoundException {
        if (persoenlicheFilter != null) {
            for (final var filter : persoenlicheFilter) {
                if (Objects.equals(filter.getId(), filterId)) {
                    return filter;
                }
            }
        }
        throw new EntityNotFoundException("PersonalFilter nicht gefunden.");
    }
}
