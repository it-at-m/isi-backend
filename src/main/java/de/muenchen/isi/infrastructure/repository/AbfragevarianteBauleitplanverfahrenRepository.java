package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.AbfragevarianteBauleitplanverfahren;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;

public interface AbfragevarianteBauleitplanverfahrenRepository
    extends JpaRepository<AbfragevarianteBauleitplanverfahren, UUID> {
    @Query(
        value = "select abfragevarianten_bauleitplanverfahren_id from abfragevariante_bauleitplanverfahren where id = ?1",
        nativeQuery = true
    )
    Optional<String> findAbfrageIdForAbfragevarianteById(final String id);

    @Async
    default CompletableFuture<Optional<String>> findAbfrageIdForAbfragevarianteByIdAsync(final String id) {
        final var abfrageId = this.findAbfrageIdForAbfragevarianteById(id);
        return CompletableFuture.completedFuture(abfrageId);
    }

    @Query(
        value = "select abfragevarianten_sachbearbeitung_bauleitplanverfahren_id from abfragevariante_bauleitplanverfahren where id = ?1",
        nativeQuery = true
    )
    Optional<String> findAbfrageIdForAbfragevarianteSachbearbeitungById(final String id);

    @Async
    default CompletableFuture<Optional<String>> findAbfrageIdForAbfragevarianteSachbearbeitungByIdAsync(
        final String id
    ) {
        final var abfrageId = this.findAbfrageIdForAbfragevarianteSachbearbeitungById(id);
        return CompletableFuture.completedFuture(abfrageId);
    }
}
