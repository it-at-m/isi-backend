package de.muenchen.isi.infrastructure.repository.common;

import de.muenchen.isi.infrastructure.entity.common.GlobalCounter;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalCounterRepository extends JpaRepository<GlobalCounter, UUID> {}
