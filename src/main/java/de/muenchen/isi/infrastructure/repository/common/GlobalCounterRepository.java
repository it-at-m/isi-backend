package de.muenchen.isi.infrastructure.repository.common;

import de.muenchen.isi.infrastructure.entity.common.GlobalCounter;
import de.muenchen.isi.infrastructure.entity.enums.CounterType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalCounterRepository extends JpaRepository<GlobalCounter, UUID> {
    Optional<GlobalCounter> findByCounterType(final CounterType counterType);
}
