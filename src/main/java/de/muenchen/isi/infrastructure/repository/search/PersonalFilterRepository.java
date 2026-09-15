package de.muenchen.isi.infrastructure.repository.search;

import de.muenchen.isi.infrastructure.entity.search.filter.PersonalFilter;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalFilterRepository extends JpaRepository<PersonalFilter, UUID> {
    List<PersonalFilter> findByPersonalIDOrderByLastModifiedDateTimeDesc(String personalId);

    PersonalFilter findByIdAndPersonalID(UUID id, String personalid);

    long countByPersonalID(String personalId);
}
