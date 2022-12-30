package de.muenchen.isi.api.dto.stammdaten;

import de.muenchen.isi.api.dto.BaseEntityDto;
import de.muenchen.isi.infrastructure.entity.enums.Wohnungstyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StaedtebaulicheOrientierungswertDto extends BaseEntityDto {

    private SobonVerfahrensgrundsaetzeJahr jahr;

    private Wohnungstyp wohnungstyp;

    private Long durchschnittlicheGrundflaeche;

    private BigDecimal belegungsdichte;

}
