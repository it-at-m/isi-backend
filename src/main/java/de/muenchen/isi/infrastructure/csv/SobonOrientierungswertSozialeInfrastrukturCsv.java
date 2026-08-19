package de.muenchen.isi.infrastructure.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import de.muenchen.isi.infrastructure.adapter.JahrConverter;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class SobonOrientierungswertSozialeInfrastrukturCsv {

    @CsvCustomBindByName(column = "JAHR", converter = JahrConverter.class, required = true)
    private LocalDate gueltigAb;

    @CsvBindByName(column = "JAHR_BEZEICHNUNG", required = true)
    private String jahrBezeichnung;

    @CsvBindByName(column = "EINRICHTUNGSTYP", required = true)
    private InfrastruktureinrichtungTyp einrichtungstyp;

    @CsvBindByName(column = "ALTERSKLASSE", required = true)
    private Altersklasse altersklasse;

    @CsvBindByName(column = "FOERDERART_BEZEICHNUNG", required = true)
    private String foerderartBezeichnung;

    @CsvBindByName(column = "JAHR_1_NACH_ERSTELLUNG", required = false)
    private BigDecimal einwohnerJahr1nachErsterstellung;

    @CsvBindByName(column = "JAHR_2_NACH_ERSTELLUNG", required = false)
    private BigDecimal einwohnerJahr2nachErsterstellung;

    @CsvBindByName(column = "JAHR_3_NACH_ERSTELLUNG", required = false)
    private BigDecimal einwohnerJahr3nachErsterstellung;

    @CsvBindByName(column = "JAHR_4_NACH_ERSTELLUNG", required = false)
    private BigDecimal einwohnerJahr4nachErsterstellung;

    @CsvBindByName(column = "JAHR_5_NACH_ERSTELLUNG", required = false)
    private BigDecimal einwohnerJahr5nachErsterstellung;

    @CsvBindByName(column = "JAHR_6_NACH_ERSTELLUNG", required = false)
    private BigDecimal einwohnerJahr6nachErsterstellung;

    @CsvBindByName(column = "JAHR_7_NACH_ERSTELLUNG", required = false)
    private BigDecimal einwohnerJahr7nachErsterstellung;

    @CsvBindByName(column = "JAHR_8_NACH_ERSTELLUNG", required = false)
    private BigDecimal einwohnerJahr8nachErsterstellung;

    @CsvBindByName(column = "JAHR_9_NACH_ERSTELLUNG", required = false)
    private BigDecimal einwohnerJahr9nachErsterstellung;

    @CsvBindByName(column = "JAHR_10_NACH_ERSTELLUNG", required = false)
    private BigDecimal einwohnerJahr10nachErsterstellung;

    @CsvBindByName(column = "STAMMWERT_ARBEITSGRUPPE", required = false)
    private BigDecimal stammwertArbeitsgruppe;
}
