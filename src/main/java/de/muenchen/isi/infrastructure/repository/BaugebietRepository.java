package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Baugebiet;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaugebietRepository extends JpaRepository<Baugebiet, UUID> {}
