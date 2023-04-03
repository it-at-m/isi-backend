package de.muenchen.isi.domain.model.stammdaten;

import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.infrastructure.entity.enums.Wohnungstyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StaedtebaulicheOrientierungswertModel extends BaseEntityModel {

    private SobonVerfahrensgrundsaetzeJahr jahr;

    private Wohnungstyp wohnungstyp;

    private Long durchschnittlicheGrundflaeche;

    private BigDecimal belegungsdichte;
}
