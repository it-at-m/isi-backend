/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.OrderBy;
import lombok.Data;

@Data
@Embeddable
public class Foerdermix {

    @ElementCollection
    @OrderBy("bezeichnung asc")
    private List<Foerderart> foerderarten;
}
