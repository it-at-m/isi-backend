/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model;

import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InfrastrukturabfrageModel extends BaseEntityModel {

    private AbfrageModel abfrage;

    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    private List<AbfragevarianteModel> abfragevarianten;

    private String aktenzeichenProLbk;

    private UncertainBoolean offiziellerVerfahrensschritt;

    /**
     * @return der zusammengesetzte Name der Abfrage
     */
    public String getDisplayName() {
        final var displayName = new StringBuilder(this.abfrage.getDisplayName());
        if (!StringUtils.isEmpty(this.getAktenzeichenProLbk())) {
            displayName.append(String.format(" - AZ: %s", this.getAktenzeichenProLbk()));
        }
        return displayName.toString();
    }

}