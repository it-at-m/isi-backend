package de.muenchen.isi.domain.model;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtBaulicheNutzung;
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

    private ArtBaulicheNutzung artBaulicheNutzung;

    private Integer realisierungVon;

    private BigDecimal gfWohnenGeplant;

    private BigDecimal gfWohnenBaurechtlichGenehmigt;

    private BigDecimal gfWohnenBaurechtlichFestgesetzt;

    private Integer weGeplant;

    private Integer weBaurechtlichGenehmigt;

    private Integer weBaurechtlichFestgesetzt;

    private List<BaurateModel> bauraten;

    private Boolean technical;
}
