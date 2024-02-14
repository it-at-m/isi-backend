package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.PrognoseKitaPlb;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrognoseKitaPlbRepository extends JpaRepository<PrognoseKitaPlb, UUID> {}
