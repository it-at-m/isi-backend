package de.muenchen.isi.infrastructure.repository.startseite;

import de.muenchen.isi.infrastructure.entity.startseite.StartseitenEinstellung;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StartseitenEinstellungRepository extends JpaRepository<StartseitenEinstellung, UUID> {
    Optional<StartseitenEinstellung> findByPersonalID(String personalId);
}
