package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.FoerdermixStamm;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoerdermixStammRepository extends JpaRepository<FoerdermixStamm, UUID> {
    Stream<FoerdermixStamm> findAllByOrderByBezeichnungAsc();
}
