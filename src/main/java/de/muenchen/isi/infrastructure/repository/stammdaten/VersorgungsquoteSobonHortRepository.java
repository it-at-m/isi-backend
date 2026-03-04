package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.VersorgungsquoteSobonHort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersorgungsquoteSobonHortRepository extends JpaRepository<VersorgungsquoteSobonHort, UUID> {
    /**
     * Liefert alle gespeicherten Versorgungsquoten für Horteinrichtungen.
     *
     * @return eine {@link List} mit allen {@link VersorgungsquoteSobonHort}-Einträgen;
     *         die Liste ist leer, falls keine Datensätze vorhanden sind
     */
    List<VersorgungsquoteSobonHort> findAll();
}
