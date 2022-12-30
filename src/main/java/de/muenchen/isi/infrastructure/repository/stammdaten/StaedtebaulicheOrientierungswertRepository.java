package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.enums.Wohnungstyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.stammdaten.StaedtebaulicheOrientierungswert;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface StaedtebaulicheOrientierungswertRepository extends PagingAndSortingRepository<StaedtebaulicheOrientierungswert, UUID> {

    Optional<StaedtebaulicheOrientierungswert> findByJahrAndWohnungstyp(final SobonVerfahrensgrundsaetzeJahr jahr,
                                                                        final Wohnungstyp wohnungstyp);

}
