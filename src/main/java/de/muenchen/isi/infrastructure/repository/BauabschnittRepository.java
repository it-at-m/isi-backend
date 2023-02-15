package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Bauabschnitt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BauabschnittRepository extends JpaRepository<Bauabschnitt, UUID> {
}
