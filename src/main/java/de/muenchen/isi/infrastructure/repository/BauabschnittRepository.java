package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Bauabschnitt;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BauabschnittRepository extends JpaRepository<Bauabschnitt, UUID> {}
