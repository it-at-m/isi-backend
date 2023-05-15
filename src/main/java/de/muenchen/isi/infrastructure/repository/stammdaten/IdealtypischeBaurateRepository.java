package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.IdealtypischeBaurate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdealtypischeBaurateRepository extends JpaRepository<IdealtypischeBaurate, UUID> {
    Optional<IdealtypischeBaurate> findByRangeWohneinheitenVonGreaterThanEqualAndRangeWohneinheitenBisEinschliesslichLessThanEqual(
        final Long wohneinheiten
    );

    Optional<IdealtypischeBaurate> findByRangeGeschossflaecheWohnenVonGreaterThanEqualAndRangeWohneinheitenBisEinschliesslichLessThanEqual(
        final Long geschossflaecheWohnen
    );
}
