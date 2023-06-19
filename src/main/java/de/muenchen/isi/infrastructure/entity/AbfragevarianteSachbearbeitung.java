/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import lombok.Data;
import lombok.ToString;

@Embeddable
@Data
@ToString(callSuper = true)
public class AbfragevarianteSachbearbeitung {

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheWohnenPlanungsursaechlich;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private SobonOrientierungswertJahr soBoNOrientierungswertJahr;

    @Column(nullable = true)
    private String anmerkung;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "abfragevariante_id", referencedColumnName = "id")
    @OrderBy("createdDateTime asc")
    private List<BedarfsmeldungFachreferate> bedarfsmeldungFachreferate;
}
