package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Baugebiet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BaugebietRepository extends JpaRepository<Baugebiet, UUID> {
}
