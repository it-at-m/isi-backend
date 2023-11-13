package de.muenchen.isi.api.dto.stammdaten;

import de.muenchen.isi.api.dto.BaseEntityDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StaedtebaulicheOrientierungswertDto extends BaseEntityDto {

    private LocalDate gueltigAb;

    private String foerderartBezeichnung;

    private Long durchschnittlicheGrundflaeche;

    private BigDecimal belegungsdichte;
}
