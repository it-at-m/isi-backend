package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.Einrichtungstyp;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonJahr;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonOrientierungswert;
import de.muenchen.isi.infrastructure.entity.stammdaten.StaedtbaulicherOrientierungwert;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SobonJahrRepository extends JpaRepository<SobonJahr, UUID> {
    Optional<
        SobonOrientierungswert
    > findByJahrAndSobonOrientierungswerteEinrichtungstypAndSobonOrientierungswerteAltersklasseAndSobonOrientierungswerteFoerderArt(
        final Integer jahr,
        final Einrichtungstyp einrichtungstyp,
        final Altersklasse altersklasse,
        final String foerderart
    );

    Optional<StaedtbaulicherOrientierungwert> findByJahrAndStaedtebaulicheOrientierungswerteFoerderArt(
        final Integer jahr,
        final String foerderart
    );

    Optional<SobonJahr> findByJahr(final Integer jahr);
}
