package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.enums.lookup.BaugebietTyp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Baugebiet extends BaseEntity {

    @Column(nullable = false)
    private String bezeichnung;

    @Column(nullable = false)
    private BaugebietTyp baugebietTyp;

    @Column(nullable = true)
    private Long anzahlWohneinheitenBaurechtlichGenehmigt;

    @Column(nullable = true)
    private Long anzahlWohneinheitenBaurechtlichFestgesetzt;

    @Column(nullable = true)
    private Long geschossflaecheWohnenGenehmigt;

    @Column(nullable = true)
    private Long geschossflaecheWohnenFestgesetzt;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "baugebiet_id")
    private List<Baurate> bauraten;

}
