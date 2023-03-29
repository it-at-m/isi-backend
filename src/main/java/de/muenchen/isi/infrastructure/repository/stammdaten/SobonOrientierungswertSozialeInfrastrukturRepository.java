package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.Einrichtungstyp;
import de.muenchen.isi.infrastructure.entity.enums.Wohnungstyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonOrientierungswertSozialeInfrastruktur;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SobonOrientierungswertSozialeInfrastrukturRepository
    extends JpaRepository<SobonOrientierungswertSozialeInfrastruktur, UUID> {
    Optional<SobonOrientierungswertSozialeInfrastruktur> findByJahrAndEinrichtungstypAndAltersklasseAndWohnungstyp(
        final SobonVerfahrensgrundsaetzeJahr jahr,
        final Einrichtungstyp einrichtungstyp,
        final Altersklasse altersklasse,
        final Wohnungstyp wohnungstyp
    );
}
