package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Baurate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.stream.Stream;

public interface BaurateRepository extends JpaRepository<Baurate, UUID> {
    Stream<Baurate> findAllByOrderByJahrDesc();
}
