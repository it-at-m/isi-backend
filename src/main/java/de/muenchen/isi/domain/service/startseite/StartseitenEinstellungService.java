package de.muenchen.isi.domain.service.startseite;

import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.mapper.BenutzerDomainMapper;
import de.muenchen.isi.domain.model.enums.SchnellfilterVorgaenge;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import de.muenchen.isi.domain.model.startseite.StartseitenEinstellungModel;
import de.muenchen.isi.domain.service.benutzer.BenutzerService;
import de.muenchen.isi.infrastructure.entity.benutzer.StartseitenEinstellung;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.springframework.stereotype.Service;

/**
 * Verwaltet die persönlichen Voreinstellungen eines Nutzers für den Startseitenbereich "Meine Vorgänge".
 * <p>
 * Die Einstellungen sind ein Attribut des {@link de.muenchen.isi.infrastructure.entity.benutzer.Benutzer}
 * und werden dort als JSON gehalten.
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

    private final BenutzerDomainMapper benutzerDomainMapper;

    private final BenutzerService benutzerService;

    /**
     * Gibt die Startseiteneinstellungen des authentifizierten Nutzers zurück.
     * <p>
     * Existiert noch kein Datensatz, so werden die Standardeinstellungen zurückgegeben, ohne sie zu persistieren.
     *
     * @return die Startseiteneinstellungen des authentifizierten Nutzers.
     * @throws UserRoleNotAllowedException falls der Nutzer nicht authentifiziert ist.
     */
    public StartseitenEinstellungModel getStartseitenEinstellung() throws UserRoleNotAllowedException {
        return benutzerService
            .findBenutzer(FEHLERMELDUNG_NICHT_AUTHENTIFIZIERT)
            .map(benutzer -> benutzerDomainMapper.entity2Model(benutzer.getStartseitenEinstellung()))
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
        final var benutzer = benutzerService.getOrCreateBenutzer(FEHLERMELDUNG_NICHT_AUTHENTIFIZIERT);
        benutzer.setStartseitenEinstellung(benutzerDomainMapper.model2Entity(startseitenEinstellungModel));
        final var gespeichert = benutzerService.save(benutzer);
        return benutzerDomainMapper.entity2Model(gespeichert.getStartseitenEinstellung());
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
