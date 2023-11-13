package de.muenchen.isi.infrastructure.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import de.muenchen.isi.infrastructure.adapter.JahrConverter;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class StaedtebaulicheOrientierungswertCsv {

    @CsvCustomBindByName(column = "JAHR", converter = JahrConverter.class, required = true)
    private LocalDate gueltigAb;

    @CsvBindByName(column = "WOHNUNGSTYP", required = true)
    private String foerderartBezeichnung;

    @CsvBindByName(column = "DURCHSCHNITTLICHE_GRUNDFLAECHE", required = true)
    private Long durchschnittlicheGrundflaeche;

    @CsvBindByName(column = "BELEGUNGSDICHTE", required = true)
    private BigDecimal belegungsdichte;
}
