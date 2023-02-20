package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AbfragevarianteRepository extends JpaRepository<Abfragevariante, UUID> {
}
