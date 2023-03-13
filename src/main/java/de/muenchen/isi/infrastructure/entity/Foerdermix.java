/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
public class Foerdermix extends BaseEntity{

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal anteilFreifinanzierterGeschosswohnungsbau;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal anteilGefoerderterMietwohnungsbau;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal anteilMuenchenModell;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal anteilPreisgedaempfterMietwohnungsbau;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal anteilKonzeptionellerMietwohnungsbau;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal anteilBaugemeinschaften;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal anteilEinUndZweifamilienhaeuser;

    @OneToMany(mappedBy = "foerdermix")
    private List<Foerderart> foerderarten;

}
