package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.Einrichtungstyp;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class SobonOrientierungswert {

    @Column(nullable = false)
    private Einrichtungstyp einrichtungstyp;

    @Column(nullable = false)
    private Altersklasse altersklasse;

    @Column(nullable = false)
    private String foerderArt;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr1NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr2NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr3NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr4NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr5NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr6NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr7NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr8NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr9NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr10NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal mittelwertEinwohnerJeWohnung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal faktor1EinwohnerJeWohnung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal faktorEinwohnerJeWohnung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal perzentil75ProzentEinwohnerJeWohnung;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal perzentil75ProzentGerundetEinwohnerJeWohnung;
}
