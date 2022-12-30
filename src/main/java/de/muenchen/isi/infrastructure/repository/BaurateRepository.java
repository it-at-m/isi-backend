package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Baurate;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;
import java.util.stream.Stream;

public interface BaurateRepository extends PagingAndSortingRepository<Baurate, UUID> {
    Stream<Baurate> findAllByOrderByJahrDesc();
}
