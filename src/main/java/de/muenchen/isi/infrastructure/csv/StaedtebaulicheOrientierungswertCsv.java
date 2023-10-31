package de.muenchen.isi.infrastructure.csv;

import com.opencsv.bean.CsvBindByName;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class StaedtebaulicheOrientierungswertCsv {

    @CsvBindByName(column = "WOHNUNGSTYP", required = true)
    private String foerderArt;

    @CsvBindByName(column = "DURCHSCHNITTLICHE_GRUNDFLAECHE", required = true)
    private Long durchschnittlicheGrundflaeche;

    @CsvBindByName(column = "BELEGUNGSDICHTE", required = true)
    private BigDecimal belegungsdichte;
}
