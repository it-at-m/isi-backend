package de.muenchen.isi.infrastructure.repository.common;

import de.muenchen.isi.infrastructure.entity.common.VerortungGeometrie;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerortungGeometrieRepository extends JpaRepository<VerortungGeometrie, UUID> {}
