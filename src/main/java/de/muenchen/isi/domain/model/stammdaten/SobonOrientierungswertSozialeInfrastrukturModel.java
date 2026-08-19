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

/**
 * Die Klasse repräsentiert die SoBon-Orientierungswerte eines Einrichtungstyps für eine Förderart.
 *
 * Die oberen Richtwerte der ersten zehn Jahre werden auf Basis der {@link SobonOrientierungswertSozialeInfrastrukturModel#getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe} ermittelt.
 * Bei den oberen Richtwerten der Jahre elf bis zwanzig findet jährlich eine prozentuale Reduktion des Richtwertes auf Basis des Vorjahres statt.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SobonOrientierungswertSozialeInfrastrukturModel extends BaseEntityModel {

    // 0.99
    public static final BigDecimal FAKTOR_JAHR_11_BIS_20 = BigDecimal.valueOf(99, 2);

    private LocalDate gueltigAb;

    private String jahrBezeichnung;

    private InfrastruktureinrichtungTyp einrichtungstyp;

    private Altersklasse altersklasse;

    private String foerderartBezeichnung;

    private BigDecimal einwohnerJahr1nachErsterstellung;

    private BigDecimal einwohnerJahr2nachErsterstellung;

    private BigDecimal einwohnerJahr3nachErsterstellung;

    private BigDecimal einwohnerJahr4nachErsterstellung;

    private BigDecimal einwohnerJahr5nachErsterstellung;

    private BigDecimal einwohnerJahr6nachErsterstellung;

    private BigDecimal einwohnerJahr7nachErsterstellung;

    private BigDecimal einwohnerJahr8nachErsterstellung;

    private BigDecimal einwohnerJahr9nachErsterstellung;

    private BigDecimal einwohnerJahr10nachErsterstellung;

    private BigDecimal stammwertArbeitsgruppe;

    /**
     * @return den des 10-Jährigen-Mittelwert der Einwohner nach Ersterstellung.
     */
    public BigDecimal getMittelwertEinwohnerNachErsterstellung10Jahre() {
        return einwohnerJahr1nachErsterstellung
            .add(einwohnerJahr2nachErsterstellung)
            .add(einwohnerJahr3nachErsterstellung)
            .add(einwohnerJahr4nachErsterstellung)
            .add(einwohnerJahr5nachErsterstellung)
            .add(einwohnerJahr6nachErsterstellung)
            .add(einwohnerJahr7nachErsterstellung)
            .add(einwohnerJahr8nachErsterstellung)
            .add(einwohnerJahr9nachErsterstellung)
            .add(einwohnerJahr10nachErsterstellung)
            .divide(BigDecimal.TEN, CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * @return das Verhältnis des 10-Jährigen-Mittelwertes zum Attribut Stammwert-Arbeitsgruppe als Faktor zur Ermittlung der oberen Richtwerte.
     */
    public BigDecimal getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe() {
        if (stammwertArbeitsgruppe == null) {
            return BigDecimal.ZERO;
        }
        return stammwertArbeitsgruppe.divide(
            getMittelwertEinwohnerNachErsterstellung10Jahre(),
            CalculationService.DIVISION_SCALE,
            RoundingMode.HALF_UP
        );
    }

    public BigDecimal getObererRichtwertEinwohnerJahr1NachErsterstellung() {
        return einwohnerJahr1nachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr2NachErsterstellung() {
        return einwohnerJahr2nachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr3NachErsterstellung() {
        return einwohnerJahr3nachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr4NachErsterstellung() {
        return einwohnerJahr4nachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr5NachErsterstellung() {
        return einwohnerJahr5nachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr6NachErsterstellung() {
        return einwohnerJahr6nachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr7NachErsterstellung() {
        return einwohnerJahr7nachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr8NachErsterstellung() {
        return einwohnerJahr8nachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr9NachErsterstellung() {
        return einwohnerJahr9nachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr10NachErsterstellung() {
        return einwohnerJahr10nachErsterstellung
            .multiply(this.getRatioOfMittelwertEinwohner10JahreToStammwertArbeitsgruppe())
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr11NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_JAHR_11_BIS_20)
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr12NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_JAHR_11_BIS_20.pow(2))
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr13NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_JAHR_11_BIS_20.pow(3))
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr14NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_JAHR_11_BIS_20.pow(4))
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr15NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_JAHR_11_BIS_20.pow(5))
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr16NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_JAHR_11_BIS_20.pow(6))
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr17NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_JAHR_11_BIS_20.pow(7))
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr18NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_JAHR_11_BIS_20.pow(8))
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr19NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_JAHR_11_BIS_20.pow(9))
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getObererRichtwertEinwohnerJahr20NachErsterstellung() {
        return getObererRichtwertEinwohnerJahr10NachErsterstellung()
            .multiply(FAKTOR_JAHR_11_BIS_20.pow(10))
            .setScale(CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
    }
}
