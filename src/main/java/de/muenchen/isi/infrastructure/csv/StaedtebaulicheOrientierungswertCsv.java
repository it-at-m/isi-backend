package de.muenchen.isi.infrastructure.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import de.muenchen.isi.infrastructure.adapter.SobonVerfahrensgrundsaetzeJahrConverter;
import de.muenchen.isi.infrastructure.adapter.WohnungstypConverter;
import de.muenchen.isi.infrastructure.entity.enums.Wohnungstyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StaedtebaulicheOrientierungswertCsv {

    @CsvCustomBindByName(column = "JAHR", converter = SobonVerfahrensgrundsaetzeJahrConverter.class, required = true)
    private SobonVerfahrensgrundsaetzeJahr jahr;

    @CsvCustomBindByName(column = "WOHNUNGSTYP", converter = WohnungstypConverter.class, required = true)
    private Wohnungstyp wohnungstyp;

    @CsvBindByName(column = "DURCHSCHNITTLICHE_GRUNDFLAECHE", required = true)
    private Long durchschnittlicheGrundflaeche;

    @CsvBindByName(column = "BELEGUNGSDICHTE", required = true)
    private BigDecimal belegungsdichte;

}
