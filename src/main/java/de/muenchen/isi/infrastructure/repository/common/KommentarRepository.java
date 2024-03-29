package de.muenchen.isi.infrastructure.repository.common;

import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KommentarRepository extends JpaRepository<Kommentar, UUID> {
    Stream<Kommentar> findAllByBauvorhabenIdOrderByCreatedDateTimeDesc(final UUID bauvorhaben);

    Stream<Kommentar> findAllByInfrastruktureinrichtungIdOrderByCreatedDateTimeDesc(
        final UUID infrastruktureinrichtung
    );

    void deleteAllByBauvorhabenId(final UUID bauvorhaben);

    void deleteAllByInfrastruktureinrichtungId(final UUID infrastruktureinrichtung);
}
