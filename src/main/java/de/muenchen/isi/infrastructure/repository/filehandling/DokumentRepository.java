package de.muenchen.isi.infrastructure.repository.filehandling;

import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DokumentRepository extends JpaRepository<Dokument, UUID> {
    // No custom methods to use
}
