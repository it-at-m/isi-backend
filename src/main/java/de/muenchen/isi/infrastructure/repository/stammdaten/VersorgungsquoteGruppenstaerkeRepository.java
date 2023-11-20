package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.VersorgungsquoteGruppenstaerke;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersorgungsquoteGruppenstaerkeRepository extends JpaRepository<VersorgungsquoteGruppenstaerke, UUID> {
    /**
     * Findet den Eintrag mit einer bestimmten Bildungseinrichtung-Bezeichnung, bei dem das Gültigkeitsdatum vor oder am
     * angegebenen Datum liegt, und sortiert die Ergebnisse nach dem Gültigkeitsdatum absteigend.
     *
     * @param bildungseinrichtungBezeichnung Die Bezeichnung der Bildungseinrichtung, nach der gesucht werden soll.
     * @param gueltigAb                      Das Gültigkeitsdatum, vor oder am dem gesucht werden soll (einschließlich).
     * @return Ein {@link Optional} mit dem gefundenen Eintrag, oder ein leeres {@link Optional}, falls kein Eintrag gefunden wurde.
     */
    Optional<
        VersorgungsquoteGruppenstaerke
    > findFirstByBildungseinrichtungBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
        String bildungseinrichtungBezeichnung,
        LocalDate gueltigAb
    );
}
