package de.muenchen.isi.domain.model;

import de.muenchen.isi.infrastructure.entity.enums.lookup.BaugebietArt;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaugebietModel extends BaseEntityModel {

    private String bezeichnung;

    private BaugebietArt baugebietArt;

    private Integer realisierungVon;

    private Integer gesamtanzahlWe;

    private Integer anzahlWohneinheitenBaurechtlichGenehmigt;

    private Integer anzahlWohneinheitenBaurechtlichFestgesetzt;

    private BigDecimal geschossflaecheWohnen;

    private BigDecimal geschossflaecheWohnenGenehmigt;

    private BigDecimal geschossflaecheWohnenFestgesetzt;

    private List<BaurateModel> bauraten;

    private Boolean technical;
}
