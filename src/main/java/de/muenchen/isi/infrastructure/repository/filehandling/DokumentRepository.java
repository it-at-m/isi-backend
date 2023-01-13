package de.muenchen.isi.infrastructure.repository.filehandling;

import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface DokumentRepository extends PagingAndSortingRepository<Dokument, UUID> {
    // No custom methods to use
}
