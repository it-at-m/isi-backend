/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class AbfragevarianteBaugenehmigungsverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel {

    private UUID id;

    private Long version;

    private ArtAbfrage artAbfragevariante;

    private BigDecimal gfWohnenPlanungsursaechlich;

    private String anmerkung;
}
