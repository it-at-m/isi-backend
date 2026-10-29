package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonOrientierungswertSozialeInfrastruktur;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SobonOrientierungswertSozialeInfrastrukturRepository
    extends JpaRepository<SobonOrientierungswertSozialeInfrastruktur, UUID>
{
    /**
     * Findet den Eintrag mit einem bestimmten Einrichtungstyp, Altersklasse und Förderart-Bezeichnung,
     * bei dem das Gültigkeitsdatum vor oder am angegebenen Datum liegt, und sortiert die Ergebnisse nach dem
     * Gültigkeitsdatum absteigend.
     *
     * @param gueltigAb             Das Gültigkeitsdatum, vor oder am dem gesucht werden soll (einschließlich).
     * @param einrichtungstyp       Der Einrichtungstyp, nach dem gesucht werden soll.
     * @param foerderartBezeichnung Die Bezeichnung der Förderart, nach der gesucht werden soll.
     * @return Ein {@link Optional} mit dem gefundenen Eintrag, oder ein leeres {@link Optional}, falls kein Eintrag gefunden wurde.
     */
    Optional<SobonOrientierungswertSozialeInfrastruktur> findFirstByEinrichtungstypAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
        final InfrastruktureinrichtungTyp einrichtungstyp,
        final String foerderartBezeichnung,
        final LocalDate gueltigAb
    );

    /**
     * Findet den Eintrag mit einem bestimmten Einrichtungstyp, Altersklasse, Förderart-Bezeichnung und Jahr-Bezeichnung.
     * Gibt es mehrere Einträge, wird der mit dem jüngsten Gültigkeitsdatum geliefert.
     * <p>
     * Die Altersklasse ist Teil des fachlichen Schlüssels: Für den Einrichtungstyp {@link InfrastruktureinrichtungTyp#UNSPECIFIED}
     * existieren mehrere Altersklassen je Förderart und Jahr.
     *
     * @param einrichtungstyp       Der Einrichtungstyp, nach dem gesucht werden soll.
     * @param altersklasse          Die Altersklasse, nach der gesucht werden soll.
     * @param foerderartBezeichnung Die Bezeichnung der Förderart, nach der gesucht werden soll.
     * @param jahrBezeichnung       Die Bezeichnung des SoBoN-Orientierungswertjahres, nach dem gesucht werden soll.
     * @return Ein {@link Optional} mit dem gefundenen Eintrag, oder ein leeres {@link Optional}, falls kein Eintrag gefunden wurde.
     */
    Optional<SobonOrientierungswertSozialeInfrastruktur> findFirstByEinrichtungstypAndAltersklasseAndFoerderartBezeichnungAndJahrBezeichnungOrderByGueltigAbDesc(
        InfrastruktureinrichtungTyp einrichtungstyp,
        Altersklasse altersklasse,
        String foerderartBezeichnung,
        String jahrBezeichnung
    );
}
