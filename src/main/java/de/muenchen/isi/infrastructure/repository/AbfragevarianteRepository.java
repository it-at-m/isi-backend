package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbfragevarianteRepository extends JpaRepository<Abfragevariante, UUID> {}
