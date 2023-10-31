package de.muenchen.isi.domain.model.stammdaten;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class StaedtbaulicherOrientierungswertModel {

    private String foerderArt;

    private Long durchschnittlicheGrundflaeche;

    private BigDecimal belegungsdichte;
}
