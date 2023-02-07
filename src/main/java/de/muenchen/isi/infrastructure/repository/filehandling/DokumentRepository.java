package de.muenchen.isi.infrastructure.repository.filehandling;

import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DokumentRepository extends JpaRepository<Dokument, UUID> {
    // No custom methods to use
}
