/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Data;
import lombok.ToString;

@Embeddable
@Data
@ToString(callSuper = true)
public class AbfragevarianteSachbearbeitung {

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal gfWohnenPlanungsursaechlich;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private SobonOrientierungswertJahr sobonOrientierungswertJahr;

    @Column(nullable = true)
    private String anmerkung;
    //@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    //@JoinColumn(name = "abfragevariante_id", referencedColumnName = "id")
    //@OrderBy("createdDateTime asc")
    //private List<BedarfsmeldungFachreferate> bedarfsmeldungFachreferate;
}
