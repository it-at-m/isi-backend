package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.UmlegungFoerderarten;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UmlegungFoerderartenRepository extends JpaRepository<UmlegungFoerderarten, UUID> {
    @Query(
        "SELECT umlegung FROM UmlegungFoerderarten umlegung WHERE umlegung.bezeichnung = :bezeichnung AND umlegung.gueltigAb <= :datum ORDER BY umlegung.gueltigAb DESC"
    )
    Optional<UmlegungFoerderarten> findFirstByBezeichnungAndGueltigAbBeforeOrEqualToOrderByGueltigAbDesc(
        @Param("bezeichnung") final String bezeichnung,
        @Param("datum") final LocalDate datum
    );
}
