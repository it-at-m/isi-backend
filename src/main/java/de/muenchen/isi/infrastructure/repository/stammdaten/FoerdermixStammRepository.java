package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.FoerdermixStamm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface FoerdermixStammRepository extends JpaRepository<FoerdermixStamm, UUID> {

    Stream<FoerdermixStamm> findAllByOrderByBezeichnungAsc();

    Optional<FoerdermixStamm> findByBezeichnungJahrIgnoreCaseAndBezeichnungIgnoreCase(final String bezeichnungJahr, final String bezeichnung);

}
