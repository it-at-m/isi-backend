package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Bauabschnitt;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface BauabschnittRepository extends PagingAndSortingRepository<Bauabschnitt, UUID> {
}
