package de.muenchen.isi.domain.model.stammdaten;

import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.Einrichtungstyp;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SobonOrientierungswertSozialeInfrastrukturModel extends BaseEntityModel {

    public static final int SCALE = 4;

    private LocalDate gueltigAb;

    private Einrichtungstyp einrichtungstyp;

    private Altersklasse altersklasse;

    private String foerderartBezeichnung;

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

    private BigDecimal stammwertArbeitsgruppe;

    public BigDecimal getMittelwertEinwohner10Jahre() {
        return einwohnerJahr1NachErsterstellung
            .add(einwohnerJahr2NachErsterstellung)
            .add(einwohnerJahr3NachErsterstellung)
            .add(einwohnerJahr4NachErsterstellung)
            .add(einwohnerJahr5NachErsterstellung)
            .add(einwohnerJahr6NachErsterstellung)
            .add(einwohnerJahr7NachErsterstellung)
            .add(einwohnerJahr8NachErsterstellung)
            .add(einwohnerJahr9NachErsterstellung)
            .add(einwohnerJahr10NachErsterstellung)
            .divide(BigDecimal.TEN, SCALE, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getFactorOfDistanceMittelwertEinwohner10JahreToStammwertArbeitsgruppe() {
        return stammwertArbeitsgruppe.divide(getMittelwertEinwohner10Jahre(), SCALE, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getObererRichtwertEinwohnerJahr1NachErsterstellung() {
        return einwohnerJahr1NachErsterstellung
            .multiply(this.getFactorOfDistanceMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getObererRichtwertEinwohnerJahr2NachErsterstellung() {
        return einwohnerJahr2NachErsterstellung
            .multiply(this.getFactorOfDistanceMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getObererRichtwertEinwohnerJahr3NachErsterstellung() {
        return einwohnerJahr3NachErsterstellung
            .multiply(this.getFactorOfDistanceMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getObererRichtwertEinwohnerJahr4NachErsterstellung() {
        return einwohnerJahr4NachErsterstellung
            .multiply(this.getFactorOfDistanceMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getObererRichtwertEinwohnerJahr5NachErsterstellung() {
        return einwohnerJahr5NachErsterstellung
            .multiply(this.getFactorOfDistanceMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getObererRichtwertEinwohnerJahr6NachErsterstellung() {
        return einwohnerJahr6NachErsterstellung
            .multiply(this.getFactorOfDistanceMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getObererRichtwertEinwohnerJahr7NachErsterstellung() {
        return einwohnerJahr7NachErsterstellung
            .multiply(this.getFactorOfDistanceMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getObererRichtwertEinwohnerJahr8NachErsterstellung() {
        return einwohnerJahr8NachErsterstellung
            .multiply(this.getFactorOfDistanceMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getObererRichtwertEinwohnerJahr9NachErsterstellung() {
        return einwohnerJahr9NachErsterstellung
            .multiply(this.getFactorOfDistanceMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getObererRichtwertEinwohnerJahr10NachErsterstellung() {
        return einwohnerJahr10NachErsterstellung
            .multiply(this.getFactorOfDistanceMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_EVEN);
    }
}
