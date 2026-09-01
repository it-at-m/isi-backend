package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface AbfragevarianteRepository extends JpaRepository<Abfragevariante, UUID> {}
