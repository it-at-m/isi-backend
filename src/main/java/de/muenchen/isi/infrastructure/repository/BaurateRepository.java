package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Baurate;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaurateRepository extends JpaRepository<Baurate, UUID> {
    Stream<Baurate> findAllByOrderByJahrDesc();
}
