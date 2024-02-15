package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.BerichtsdatenKitaPlb;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BerichtsdatenKitaPlbRepository extends JpaRepository<BerichtsdatenKitaPlb, UUID> {}
