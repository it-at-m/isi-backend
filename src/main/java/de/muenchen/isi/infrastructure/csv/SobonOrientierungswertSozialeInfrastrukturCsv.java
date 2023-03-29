package de.muenchen.isi.infrastructure.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import de.muenchen.isi.infrastructure.adapter.AltersklasseConverter;
import de.muenchen.isi.infrastructure.adapter.EinrichtungstypConverter;
import de.muenchen.isi.infrastructure.adapter.SobonVerfahrensgrundsaetzeJahrConverter;
import de.muenchen.isi.infrastructure.adapter.WohnungstypConverter;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.Einrichtungstyp;
import de.muenchen.isi.infrastructure.entity.enums.Wohnungstyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class SobonOrientierungswertSozialeInfrastrukturCsv {

    @CsvCustomBindByName(column = "JAHR", converter = SobonVerfahrensgrundsaetzeJahrConverter.class, required = true)
    private SobonVerfahrensgrundsaetzeJahr jahr;

    @CsvCustomBindByName(column = "EINRICHTUNGSTYP", converter = EinrichtungstypConverter.class, required = true)
    private Einrichtungstyp einrichtungstyp;

    @CsvCustomBindByName(column = "ALTERSKLASSE", converter = AltersklasseConverter.class, required = true)
    private Altersklasse altersklasse;

    @CsvCustomBindByName(column = "WOHNUNGSTYP", converter = WohnungstypConverter.class, required = true)
    private Wohnungstyp wohnungstyp;

    @CsvBindByName(column = "JAHR_1_NACH_ERSTELLUNG", required = true)
    private BigDecimal einwohnerJahr1NachErsterstellung;

    @CsvBindByName(column = "JAHR_2_NACH_ERSTELLUNG", required = true)
    private BigDecimal einwohnerJahr2NachErsterstellung;

    @CsvBindByName(column = "JAHR_3_NACH_ERSTELLUNG", required = true)
    private BigDecimal einwohnerJahr3NachErsterstellung;

    @CsvBindByName(column = "JAHR_4_NACH_ERSTELLUNG", required = true)
    private BigDecimal einwohnerJahr4NachErsterstellung;

    @CsvBindByName(column = "JAHR_5_NACH_ERSTELLUNG", required = true)
    private BigDecimal einwohnerJahr5NachErsterstellung;

    @CsvBindByName(column = "JAHR_6_NACH_ERSTELLUNG", required = true)
    private BigDecimal einwohnerJahr6NachErsterstellung;

    @CsvBindByName(column = "JAHR_7_NACH_ERSTELLUNG", required = true)
    private BigDecimal einwohnerJahr7NachErsterstellung;

    @CsvBindByName(column = "JAHR_8_NACH_ERSTELLUNG", required = true)
    private BigDecimal einwohnerJahr8NachErsterstellung;

    @CsvBindByName(column = "JAHR_9_NACH_ERSTELLUNG", required = true)
    private BigDecimal einwohnerJahr9NachErsterstellung;

    @CsvBindByName(column = "JAHR_10_NACH_ERSTELLUNG", required = true)
    private BigDecimal einwohnerJahr10NachErsterstellung;

    @CsvBindByName(column = "MITTELWERT", required = true)
    private BigDecimal mittelwertEinwohnerJeWohnung;

    @CsvBindByName(column = "FAKTOR1", required = true)
    private BigDecimal faktor1EinwohnerJeWohnung;

    @CsvBindByName(column = "FAKTOR", required = true)
    private BigDecimal faktorEinwohnerJeWohnung;

    @CsvBindByName(column = "PERZENTIL75", required = true)
    private BigDecimal perzentil75ProzentEinwohnerJeWohnung;

    @CsvBindByName(column = "PERZENTIL_GERUNDET", required = true)
    private BigDecimal perzentil75ProzentGerundetEinwohnerJeWohnung;
}
