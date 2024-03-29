/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.infrastruktureinrichtung;

import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.common.BearbeitendePersonModel;
import de.muenchen.isi.domain.model.common.VerortungPointModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class InfrastruktureinrichtungModel extends BaseEntityModel {

    private BearbeitendePersonModel bearbeitendePerson;

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    private Long lfdNr;

    private UUID bauvorhaben;

    private AdresseModel adresse;

    private VerortungPointModel verortung;

    private String nameEinrichtung;

    private Integer fertigstellungsjahr; // JJJJ

    private StatusInfrastruktureinrichtung status;

    private BigDecimal flaecheGesamtgrundstueck;

    private BigDecimal flaecheTeilgrundstueck;
}
