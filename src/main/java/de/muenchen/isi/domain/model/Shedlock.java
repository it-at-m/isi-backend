package de.muenchen.isi.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;

/**
 * Entitaet fuer den Lock des Schedulers
 */
@Entity
@Table(name = "shedlock")
public class Shedlock {

    @Id
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lock_until", nullable = false)
    private Timestamp lockUntil;

    @Column(name = "locked_at", nullable = false)
    private Timestamp lockedAt;

    @Column(name = "locked_by", nullable = false)
    private String lockedBy;
}
