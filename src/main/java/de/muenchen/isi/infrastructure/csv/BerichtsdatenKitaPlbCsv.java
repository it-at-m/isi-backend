package de.muenchen.isi.infrastructure.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BerichtsdatenKitaPlbCsv {

    @CsvBindByName(column = "KITA_PLB", required = true)
    private Long kitaPlb;

    @CsvDate(value = "yyyy-MM-dd")
    @CsvBindByName(column = "BERICHTSSTAND", required = true)
    private LocalDate berichtsstand;

    @CsvBindByName(column = "0_BIS_2_JAEHRIGE", required = true)
    private BigDecimal anzahlNullBisZweiJaehrige;

    @CsvBindByName(column = "3_BIS_5_JAEHRIGE_UND_50_PROZENT_DER_6_JAEHRIGEN", required = true)
    private BigDecimal anzahlDreiBisFuenfJaehrigeUndFuenfzigProzentSechsJaehrige;
}
