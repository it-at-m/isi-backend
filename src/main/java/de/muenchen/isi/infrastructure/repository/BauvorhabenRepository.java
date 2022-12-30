package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;
import java.util.stream.Stream;

public interface BauvorhabenRepository extends PagingAndSortingRepository<Bauvorhaben, UUID> {

    Stream<Bauvorhaben> findAllByOrderByGrundstuecksgroesseDesc();

}
