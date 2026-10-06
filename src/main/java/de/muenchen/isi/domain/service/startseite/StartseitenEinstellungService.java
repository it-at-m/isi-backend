package de.muenchen.isi.domain.service.startseite;

import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.model.enums.SchnellfilterVorgaenge;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import de.muenchen.isi.domain.model.startseite.StartseitenEinstellungModel;
import de.muenchen.isi.infrastructure.entity.filter.FilterSettings;
import de.muenchen.isi.infrastructure.entity.filter.PersonalFilter;
import de.muenchen.isi.infrastructure.repository.filter.PersonalFilterRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

/**
 * Verwaltet die persönlichen Voreinstellungen eines Nutzers für den Startseitenbereich "Meine Vorgänge".
 * <p>
 * Die Einstellungen werden gemeinsam mit den persönlichen Filtern in {@link PersonalFilter} gehalten.
 * Je Nutzer existiert dort maximal eine über {@link PersonalFilter#getIstStartseite()} markierte Zeile,
 * welche von der Filter-API nicht ausgeliefert wird.
 * <p>
 * Der Nutzerbezug wird ausschließlich über die {@link AuthenticationUtils} hergestellt und niemals
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

    private final PersonalFilterRepository personalFilterRepository;

    private final AuthenticationUtils authenticationUtils;

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
        return personalFilterRepository
            .findByPersonalIDAndIstStartseiteTrue(userSub)
            .map(StartseitenEinstellungService::entity2Model)
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
        var entity = personalFilterRepository
            .findByPersonalIDAndIstStartseiteTrue(userSub)
            .orElseGet(() -> createEntity(userSub));
        updateEntityFromModel(startseitenEinstellungModel, entity);
        try {
            entity = personalFilterRepository.saveAndFlush(entity);
        } catch (final ObjectOptimisticLockingFailureException exception) {
            final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
            throw new OptimisticLockingException(message, exception);
        }
        return entity2Model(entity);
    }

    /**
     * Gibt den userSub zurück sofern es kein Fallback-Wert ist.
     *
     * @return den userSub aus {@link AuthenticationUtils}.
     * @throws UserRoleNotAllowedException falls der Nutzer den Fallback-Sub zugewiesen hat.
     */
    private String getSubFromAuthenticatedUser() throws UserRoleNotAllowedException {
        return authenticationUtils.getSubFromAuthenticatedUser(FEHLERMELDUNG_NICHT_AUTHENTIFIZIERT);
    }

    /**
     * Erzeugt die systemeigene Zeile, welche die Startseiteneinstellung des Nutzers hält.
     *
     * @param userSub des Nutzers, dem die Zeile gehört.
     * @return die noch nicht persistierte Entität.
     */
    private static PersonalFilter createEntity(final String userSub) {
        final var entity = new PersonalFilter();
        entity.setPersonalID(userSub);
        entity.setFilterName(PersonalFilter.STARTSEITE_FILTER_NAME);
        entity.setIstStartseite(true);
        entity.setFilterSettings(new FilterSettings());
        return entity;
    }

    /**
     * Überträgt die Startseiteneinstellungen in die Entität. Die übrigen Filtereinstellungen der
     * Startseiten-Zeile bleiben ungenutzt und damit leer.
     *
     * @param model mit den zu übernehmenden Einstellungen.
     * @param entity in welche die Einstellungen übertragen werden.
     */
    private static void updateEntityFromModel(final StartseitenEinstellungModel model, final PersonalFilter entity) {
        if (entity.getFilterSettings() == null) {
            entity.setFilterSettings(new FilterSettings());
        }
        final var filterSettings = entity.getFilterSettings();
        filterSettings.setSchnellfilter(model.getSchnellfilter());
        filterSettings.setSortBy(model.getSortBy());
        filterSettings.setSortOrder(model.getSortOrder());
    }

    /**
     * @param entity mit den gespeicherten Startseiteneinstellungen.
     * @return das zugehörige {@link StartseitenEinstellungModel}.
     */
    private static StartseitenEinstellungModel entity2Model(final PersonalFilter entity) {
        final var model = new StartseitenEinstellungModel();
        final var filterSettings = entity.getFilterSettings();
        if (filterSettings != null) {
            model.setSchnellfilter(filterSettings.getSchnellfilter());
            model.setSortBy(filterSettings.getSortBy());
            model.setSortOrder(filterSettings.getSortOrder());
        }
        return model;
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
