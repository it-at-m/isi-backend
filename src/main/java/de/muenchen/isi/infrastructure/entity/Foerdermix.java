/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Foerdermix {

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
}
