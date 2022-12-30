package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface AbfragevarianteRepository extends PagingAndSortingRepository<Abfragevariante, UUID> {
}
