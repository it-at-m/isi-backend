/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import lombok.Data;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Embeddable
public class Foerdermix {

    @OneToMany
    private List<Foerderart> foerderarten;

}
