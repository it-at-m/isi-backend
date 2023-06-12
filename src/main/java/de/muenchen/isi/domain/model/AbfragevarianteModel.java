/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model;

import de.muenchen.isi.infrastructure.entity.AbfragevarianteSachbearbeitung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfragevarianteModel extends BaseEntityModel {

    private Integer abfragevariantenNr;

    private boolean isRelevant;

    private String abfragevariantenName;

    private Planungsrecht planungsrecht;

    private BigDecimal geschossflaecheWohnen;

    private BigDecimal geschossflaecheWohnenGenehmigt;

    private BigDecimal geschossflaecheWohnenFestgesetzt;

    private BigDecimal geschossflaecheWohnenSoBoNursaechlich;

    private BigDecimal geschossflaecheWohnenBestandswohnbaurecht;

    private Integer gesamtanzahlWe;

    private Integer anzahlWeBaurechtlichGenehmigt;

    private Integer anzahlWeBaurechtlichFestgesetzt;

    private Integer realisierungVon;

    private Integer realisierungBis;

    private BigDecimal geschossflaecheGenossenschaftlicheWohnungen;

    private Boolean sonderwohnformen;

    private BigDecimal geschossflaecheStudentenwohnungen;

    private BigDecimal geschossflaecheSeniorenwohnungen;

    private BigDecimal geschossflaecheSonstiges;

    private List<BauabschnittModel> bauabschnitte;

    private AbfragevarianteSachbearbeitungModel abfragevarianteSachbearbeitung;
}
