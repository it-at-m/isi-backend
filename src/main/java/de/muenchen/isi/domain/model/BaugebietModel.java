package de.muenchen.isi.domain.model;

import de.muenchen.isi.infrastructure.entity.enums.lookup.BaugebietTyp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaugebietModel extends BaseEntityModel {

    private String bezeichnung;

    private BaugebietTyp baugebietTyp;

    private Long anzahlWohneinheitenBaurechtlichGenehmigt;

    private Long anzahlWohneinheitenBaurechtlichFestgesetzt;

    private Long geschossflaecheWohnenGenehmigt;

    private Long geschossflaecheWohnenFestgesetzt;

    private List<BaurateModel> bauraten;

}
