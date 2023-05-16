package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.IdealtypischeBaurate;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdealtypischeBaurateRepository extends JpaRepository<IdealtypischeBaurate, UUID> {
    default Optional<IdealtypischeBaurate> findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
        final Long wohneinheiten
    ) {
        return findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
            wohneinheiten,
            wohneinheiten
        );
    }

    Optional<IdealtypischeBaurate> findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
        final Long wohneinheitenVon,
        final Long wohneinheitenBisEinschliesslich
    );

    default Optional<IdealtypischeBaurate> findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
        final BigDecimal geschossflaecheWohnen
    ) {
        return findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
            geschossflaecheWohnen,
            geschossflaecheWohnen
        );
    }

    Optional<IdealtypischeBaurate> findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
        final BigDecimal geschossflaecheWohnenVon,
        final BigDecimal geschossflaecheWohnenBisEinschliesslich
    );
}
