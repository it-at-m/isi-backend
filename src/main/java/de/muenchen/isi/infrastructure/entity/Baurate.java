/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(indexes = { @Index(name = "baurate_baugebiet_id_index", columnList = "baugebiet_id") })
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Baurate extends BaseEntity {

    @Column(nullable = false)
    private Integer jahr; // JJJJ

    @Column
    private Integer weGeplant;

    @Column(precision = 10, scale = 2)
    private BigDecimal gfWohnenGeplant;

    @Embedded
    private Foerdermix foerdermix;
}
