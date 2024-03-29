package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.enums.IdealtypischeBaurateTyp;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.IdealtypischeBaurate;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdealtypischeBaurateRepository extends JpaRepository<IdealtypischeBaurate, UUID> {
    default Optional<IdealtypischeBaurate> findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
        final IdealtypischeBaurateTyp typ,
        final BigDecimal wert
    ) {
        return findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(typ, wert, wert);
    }

    Optional<IdealtypischeBaurate> findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
        final IdealtypischeBaurateTyp typ,
        final BigDecimal von,
        final BigDecimal bisExklusiv
    );
}
