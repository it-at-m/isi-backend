package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Baugebiet;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface BaugebietRepository extends PagingAndSortingRepository<Baugebiet, UUID> {
}
