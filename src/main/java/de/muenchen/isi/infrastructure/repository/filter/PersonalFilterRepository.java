package de.muenchen.isi.infrastructure.repository.filter;

import de.muenchen.isi.infrastructure.entity.filter.PersonalFilter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Zugriff auf die persönlichen Nutzerdaten.
 * <p>
 * Die Tabelle hält sowohl die echten persönlichen Filter als auch die Startseiteneinstellung
 * je Nutzer. Die Startseiten-Zeile ist über {@code istStartseite} markiert und wird von den
 * Filter-Abfragen bewusst ausgeschlossen.
 */
public interface PersonalFilterRepository extends JpaRepository<PersonalFilter, UUID> {
    List<PersonalFilter> findByPersonalIDAndIstStartseiteFalseOrderByLastModifiedDateTimeDesc(String personalId);

    PersonalFilter findByIdAndPersonalIDAndIstStartseiteFalse(UUID id, String personalid);

    long countByPersonalIDAndIstStartseiteFalse(String personalId);

    Optional<PersonalFilter> findByPersonalIDAndIstStartseiteTrue(String personalId);
}
