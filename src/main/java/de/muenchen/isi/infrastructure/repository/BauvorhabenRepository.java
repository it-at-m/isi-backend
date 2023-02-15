package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface BauvorhabenRepository extends JpaRepository<Bauvorhaben, UUID> {

    Stream<Bauvorhaben> findAllByOrderByGrundstuecksgroesseDesc();

    Optional<Bauvorhaben> findByNameVorhabenIgnoreCase(String nameVorhaben);
}
