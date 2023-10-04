package de.muenchen.isi.infrastructure.repository.common;

import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KommentarRepository extends JpaRepository<Kommentar, UUID> {
    Stream<Kommentar> findAllByBauvorhabenOrderByCreatedDateTimeDesc(final UUID bauvorhaben);

    Stream<Kommentar> findAllByInfrastruktureinrichtungOrderByCreatedDateTimeDesc(final UUID infrastruktureinrichtung);

    void deleteAllByBauvorhaben(final UUID bauvorhaben);

    void deleteAllByInfrastruktureinrichtung(final UUID infrastruktureinrichtung);
}
