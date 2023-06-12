/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model;

import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class AbfragevarianteSachbearbeitungModel {

    private BigDecimal geschossflaecheWohnenPlanungsursaechlich;

    private SobonOrientierungswertJahr soBoNOrientierungswertJahr;

    private String anmerkung;

    private List<BedarfsmeldungFachabteilungenModel> bedarfsmeldungFachreferate;
}
