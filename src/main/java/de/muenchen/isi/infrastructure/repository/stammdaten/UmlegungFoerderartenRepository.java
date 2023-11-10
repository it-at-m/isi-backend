package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.UmlegungFoerderarten;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UmlegungFoerderartenRepository extends JpaRepository<UmlegungFoerderarten, UUID> {
    /**
     * Findet den ersten Eintrag mit einer bestimmten Bezeichnung, bei dem das Gültigkeitsdatum vor oder am
     * angegebenen Datum liegt, und sortiert die Ergebnisse nach dem Gültigkeitsdatum absteigend.
     *
     * @param bezeichnung Die Bezeichnung, nach der gesucht werden soll.
     * @param datum       Das Datum, bis zu dem das Gültigkeitsdatum liegen soll (einschließlich).
     * @return Ein {@link Optional} mit dem gefundenen Eintrag, oder ein leeres {@link Optional}, falls kein Eintrag gefunden wurde.
     */
    Optional<UmlegungFoerderarten> findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
        final String bezeichnung,
        final LocalDate datum
    );
}
