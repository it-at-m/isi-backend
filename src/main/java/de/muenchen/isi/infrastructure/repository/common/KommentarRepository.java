package de.muenchen.isi.infrastructure.repository.common;

import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KommentarRepository extends JpaRepository<Kommentar, UUID> {
    List<Kommentar> findAllByBauvorhabenIdOrderByCreatedDateTimeDesc(final UUID bauvorhabenId);
}
