package de.muenchen.isi.domain.service.startseite;

import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.mapper.StartseitenEinstellungDomainMapper;
import de.muenchen.isi.domain.model.enums.SchnellfilterVorgaenge;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import de.muenchen.isi.domain.model.startseite.StartseitenEinstellungModel;
import de.muenchen.isi.domain.service.common.AuthenticatedUserService;
import de.muenchen.isi.infrastructure.entity.startseite.StartseitenEinstellung;
import de.muenchen.isi.infrastructure.repository.startseite.StartseitenEinstellungRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

/**
 * Verwaltet die persönlichen Voreinstellungen eines Nutzers für den Startseitenbereich "Meine Vorgänge".
 * <p>
 * Der Nutzerbezug wird ausschließlich über den {@link AuthenticatedUserService} hergestellt und niemals
 * aus dem Request übernommen.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StartseitenEinstellungService {

    static final SchnellfilterVorgaenge DEFAULT_SCHNELLFILTER = SchnellfilterVorgaenge.ALLE;

    static final SortAttribute DEFAULT_SORT_BY = SortAttribute.CREATED_DATE_TIME;

    static final SortOrder DEFAULT_SORT_ORDER = SortOrder.DESC;

    static final String FEHLERMELDUNG_NICHT_AUTHENTIFIZIERT =
        "Sie müssen authentifiziert sein, um die Startseiteneinstellungen zu verwenden";

    private final StartseitenEinstellungDomainMapper startseitenEinstellungDomainMapper;

    private final StartseitenEinstellungRepository startseitenEinstellungRepository;

    private final AuthenticatedUserService authenticatedUserService;

    /**
     * Gibt die Startseiteneinstellungen des authentifizierten Nutzers zurück.
     * <p>
     * Existiert noch kein Datensatz, so werden die Standardeinstellungen zurückgegeben, ohne sie zu persistieren.
     *
     * @return die Startseiteneinstellungen des authentifizierten Nutzers.
     * @throws UserRoleNotAllowedException falls der Nutzer nicht authentifiziert ist.
     */
    public StartseitenEinstellungModel getStartseitenEinstellung() throws UserRoleNotAllowedException {
        final var userSub = this.getSubFromAuthenticatedUser();
        return startseitenEinstellungRepository
            .findByPersonalID(userSub)
            .map(startseitenEinstellungDomainMapper::entity2Model)
            .orElseGet(StartseitenEinstellungService::createDefaultModel);
    }

    /**
     * Speichert die Startseiteneinstellungen des authentifizierten Nutzers.
     * <p>
     * Existiert noch kein Datensatz, so wird er angelegt, andernfalls überschrieben.
     *
     * @param startseitenEinstellungModel mit den zu speichernden Einstellungen.
     * @return die gespeicherten Startseiteneinstellungen.
     * @throws UserRoleNotAllowedException falls der Nutzer nicht authentifiziert ist.
     * @throws OptimisticLockingException falls bereits eine neuere Version der Entität gespeichert ist.
     */
    public StartseitenEinstellungModel save(final StartseitenEinstellungModel startseitenEinstellungModel)
        throws UserRoleNotAllowedException, OptimisticLockingException {
        final var userSub = this.getSubFromAuthenticatedUser();
        var entity = startseitenEinstellungRepository.findByPersonalID(userSub).orElseGet(() -> {
            final var neueEinstellung = new StartseitenEinstellung();
            neueEinstellung.setPersonalID(userSub);
            return neueEinstellung;
        });
        startseitenEinstellungDomainMapper.updateEntityFromModel(startseitenEinstellungModel, entity);
        try {
            entity = startseitenEinstellungRepository.saveAndFlush(entity);
        } catch (final ObjectOptimisticLockingFailureException exception) {
            final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
            throw new OptimisticLockingException(message, exception);
        }
        return startseitenEinstellungDomainMapper.entity2Model(entity);
    }

    /**
     * Gibt den userSub zurück sofern es kein Fallback-Wert ist.
     *
     * @return den userSub aus dem {@link AuthenticatedUserService}.
     * @throws UserRoleNotAllowedException falls der Nutzer den Fallback-Sub zugewiesen hat.
     */
    private String getSubFromAuthenticatedUser() throws UserRoleNotAllowedException {
        return authenticatedUserService.getSubFromAuthenticatedUser(FEHLERMELDUNG_NICHT_AUTHENTIFIZIERT);
    }

    /**
     * @return die Standardeinstellungen für die Startseite.
     */
    private static StartseitenEinstellungModel createDefaultModel() {
        final var model = new StartseitenEinstellungModel();
        model.setSchnellfilter(DEFAULT_SCHNELLFILTER);
        model.setSortBy(DEFAULT_SORT_BY);
        model.setSortOrder(DEFAULT_SORT_ORDER);
        return model;
    }
}
