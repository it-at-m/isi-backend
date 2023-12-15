package de.muenchen.isi.domain.model.stammdaten;

import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.domain.service.calculation.CalculationService;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
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

    public static final int SCALE = CalculationService.DIVISION_SCALE;

    // 0.99
    public static final BigDecimal FAKTOR_DISTANCE_JAHR_11_BIS_20 = BigDecimal.valueOf(99, 2);

    private LocalDate gueltigAb;

    private InfrastruktureinrichtungTyp einrichtungstyp;

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

    /**
     * @return den des 10-Jährigen-Mittelwert der Einwohner nach Ersterstellung.
     */
    public BigDecimal getMittelwertEinwohnerNachErsterstellung10Jahre() {
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
            .divide(BigDecimal.TEN, SCALE, RoundingMode.HALF_UP);
    }

    /**
     * @return das Verhältnis des 10-Jährigen-Mittelwertes zum Attribut Stammwert-Arbeitsgruppe als Faktor zur Ermittlung der oberen Richtwerte.
     */
    public BigDecimal getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe() {
        return stammwertArbeitsgruppe.divide(
            getMittelwertEinwohnerNachErsterstellung10Jahre(),
            SCALE,
            RoundingMode.HALF_UP
        );
    }

    public BigDecimal getObererRichtwertEinwohnerJahr1NachErsterstellung() {
        return einwohnerJahr1NachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr2NachErsterstellung() {
        return einwohnerJahr2NachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr3NachErsterstellung() {
        return einwohnerJahr3NachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr4NachErsterstellung() {
        return einwohnerJahr4NachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr5NachErsterstellung() {
        return einwohnerJahr5NachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr6NachErsterstellung() {
        return einwohnerJahr6NachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr7NachErsterstellung() {
        return einwohnerJahr7NachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr8NachErsterstellung() {
        return einwohnerJahr8NachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr9NachErsterstellung() {
        return einwohnerJahr9NachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr10NachErsterstellung() {
        return einwohnerJahr10NachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr11NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_DISTANCE_JAHR_11_BIS_20)
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr12NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_DISTANCE_JAHR_11_BIS_20.pow(2))
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr13NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_DISTANCE_JAHR_11_BIS_20.pow(3))
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr14NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_DISTANCE_JAHR_11_BIS_20.pow(4))
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr15NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_DISTANCE_JAHR_11_BIS_20.pow(5))
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr16NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_DISTANCE_JAHR_11_BIS_20.pow(6))
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr17NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_DISTANCE_JAHR_11_BIS_20.pow(7))
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr18NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_DISTANCE_JAHR_11_BIS_20.pow(8))
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr19NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_DISTANCE_JAHR_11_BIS_20.pow(9))
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr20NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_DISTANCE_JAHR_11_BIS_20.pow(10))
            .setScale(SCALE, RoundingMode.HALF_UP);
    }
}
