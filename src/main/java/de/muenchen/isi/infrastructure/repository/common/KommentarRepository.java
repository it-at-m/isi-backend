package de.muenchen.isi.infrastructure.repository.common;

import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KommentarRepository extends JpaRepository<Kommentar, UUID> {
    Stream<Kommentar> findAllByBauvorhabenIdOrderByCreatedDateTimeAsc(final UUID bauvorhabenId);

    Stream<Kommentar> findAllByInfrastruktureinrichtungIdOrderByCreatedDateTimeAsc(
        final UUID infrastruktureinrichtungId
    );

    void deleteAllByBauvorhabenId(final UUID bauvorhabenId);

    void deleteAllByInfrastruktureinrichtungId(final UUID infrastruktureinrichtungId);
}
