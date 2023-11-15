package de.muenchen.isi.domain.model.stammdaten;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StaedtebaulicheOrientierungswertModel extends BaseEntityModel {

    private LocalDate gueltigAb;

    private String foerderartBezeichnung;

    private Long durchschnittlicheGrundflaeche;

    private BigDecimal belegungsdichte;
}
