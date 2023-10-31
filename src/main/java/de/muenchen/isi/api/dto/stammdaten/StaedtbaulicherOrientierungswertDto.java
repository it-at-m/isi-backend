package de.muenchen.isi.api.dto.stammdaten;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class StaedtbaulicherOrientierungswertDto {

    private String foerderArt;

    private Long durchschnittlicheGrundflaeche;

    private BigDecimal belegungsdichte;
}
