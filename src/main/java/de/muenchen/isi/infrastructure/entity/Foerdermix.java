/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Foerdermix extends BaseEntity {

    @ElementCollection
    private List<Foerderart> foerderarten;
}
