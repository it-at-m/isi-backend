package de.muenchen.isi.infrastructure.repository.common;

import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KommentarRepository extends JpaRepository<Kommentar, UUID> {
    Stream<Kommentar> findAllByBauvorhabenIdOrderByCreatedDateTimeDesc(final UUID bauvorhabenId);

    Stream<Kommentar> findAllByInfrastruktureinrichtungIdOrderByCreatedDateTimeDesc(
        final UUID infrastruktureinrichtungId
    );

    void deleteAllByBauvorhabenId(final UUID bauvorhabenId);

    void deleteAllByInfrastruktureinrichtungId(final UUID infrastruktureinrichtungId);
}
