package de.muenchen.isi.domain.model.stammdaten;

import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.Einrichtungstyp;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class SobonOrientierungswertModel {

    private Einrichtungstyp einrichtungstyp;

    private Altersklasse altersklasse;

    private String foerderArt;

    private BigDecimal einwohnerJahr1NachErsterstellung;

    private BigDecimal einwohnerJahr2NachErsterstellung;

    private BigDecimal einwohnerJahr3NachErsterstellung;

    private BigDecimal einwohnerJahr4NachErsterstellung;

    private BigDecimal einwohnerJahr5NachErsterstellung;

    private BigDecimal einwohnerJahr6NachErsterstellung;

    private BigDecimal einwohnerJahr7NachErsterstellung;

    private BigDecimal einwohnerJahr8NachErsterstellung;

    private BigDecimal einwohnerJahr9NachErsterstellung;

    private BigDecimal einwohnerJahr10NachErsterstellung;

    private BigDecimal mittelwertEinwohnerJeWohnung;

    private BigDecimal faktor1EinwohnerJeWohnung;

    private BigDecimal faktorEinwohnerJeWohnung;

    private BigDecimal perzentil75ProzentEinwohnerJeWohnung;

    private BigDecimal perzentil75ProzentGerundetEinwohnerJeWohnung;
}
