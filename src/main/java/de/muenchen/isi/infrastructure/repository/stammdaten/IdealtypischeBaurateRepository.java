package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.IdealtypischeBaurate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdealtypischeBaurateRepository extends JpaRepository<IdealtypischeBaurate, UUID> {
    default Optional<IdealtypischeBaurate> findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
        final Long wohneinheiten
    ) {
        return findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
            wohneinheiten,
            wohneinheiten
        );
    }

    Optional<IdealtypischeBaurate> findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
        final Long wohneinheitenVon,
        final Long wohneinheitenBisEinschliesslich
    );

    default Optional<IdealtypischeBaurate> findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
        final Long geschossflaecheWohnen
    ) {
        return findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
            geschossflaecheWohnen,
            geschossflaecheWohnen
        );
    }

    Optional<IdealtypischeBaurate> findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
        final Long geschossflaecheWohnenVon,
        final Long geschossflaecheWohnenBisEinschliesslich
    );
}
