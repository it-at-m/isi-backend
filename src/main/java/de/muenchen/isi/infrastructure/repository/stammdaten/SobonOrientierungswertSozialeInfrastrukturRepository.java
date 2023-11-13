package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.Einrichtungstyp;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonOrientierungswertSozialeInfrastruktur;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SobonOrientierungswertSozialeInfrastrukturRepository
    extends JpaRepository<SobonOrientierungswertSozialeInfrastruktur, UUID> {
    Optional<
        SobonOrientierungswertSozialeInfrastruktur
    > findByGueltigAbAndEinrichtungstypAndAltersklasseAndFoerderartBezeichnung(
        final LocalDate gueltigAb,
        final Einrichtungstyp einrichtungstyp,
        final Altersklasse altersklasse,
        final String foerderartBezeichnung
    );
}
