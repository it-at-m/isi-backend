package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.enums.Wohnungstyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.stammdaten.StaedtebaulicheOrientierungswert;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaedtebaulicheOrientierungswertRepository
    extends JpaRepository<StaedtebaulicheOrientierungswert, UUID> {
    Optional<StaedtebaulicheOrientierungswert> findByJahrAndWohnungstyp(
        final SobonVerfahrensgrundsaetzeJahr jahr,
        final Wohnungstyp wohnungstyp
    );
}
