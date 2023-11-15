package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.StaedtebaulicheOrientierungswert;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaedtebaulicheOrientierungswertRepository
    extends JpaRepository<StaedtebaulicheOrientierungswert, UUID> {
    /**
     * Findet den Eintrag mit einer bestimmten Förderart-Bezeichnung, bei dem das Gültigkeitsdatum vor oder am
     * angegebenen Datum liegt, und sortiert die Ergebnisse nach dem Gültigkeitsdatum absteigend.
     *
     * @param foerderartBezeichnung Die Bezeichnung der Förderart, nach der gesucht werden soll.
     * @param gueltigAb             Das Gültigkeitsdatum, vor oder am dem gesucht werden soll (einschließlich).
     * @return Ein {@link Optional} mit dem gefundenen Eintrag, oder ein leeres {@link Optional}, falls kein Eintrag gefunden wurde.
     */
    Optional<
        StaedtebaulicheOrientierungswert
    > findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
        String foerderartBezeichnung,
        LocalDate gueltigAb
    );
}
