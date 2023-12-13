/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.abfrageAngelegt;

import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class AbfragevarianteBaugenehmigungsverfahrenAngelegtModel {

    private UUID id;

    private Long version;

    private ArtAbfrage artAbfragevariante;

    private Integer abfragevariantenNr;

    private String name;

    private List<WesentlicheRechtsgrundlage> wesentlicheRechtsgrundlage;

    private String wesentlicheRechtsgrundlageFreieEingabe;

    private Integer realisierungVon;

    // Geschossfläche Wohnen

    private BigDecimal gfWohnenGesamt;

    private BigDecimal gfWohnenBaurechtlichGenehmigt;

    private BigDecimal gfWohnenBaurechtlichFestgesetzt;

    private BigDecimal gfWohnenBestandswohnbaurecht;

    private Boolean gfWohnenSonderwohnformen;

    private BigDecimal gfWohnenStudentischesWohnen;

    private BigDecimal gfWohnenSeniorinnenWohnen;

    private BigDecimal gfWohnenGenossenschaftlichesWohnen;

    private BigDecimal gfWohnenWeiteresNichtInfrastrukturrelevantesWohnen;

    // Anzahl Wohneinheiten

    private Integer weGesamt;

    private Integer weBaurechtlichGenehmigt;

    private Integer weBaurechtlichFestgesetzt;

    private Boolean weSonderwohnformen;

    private Integer weStudentischesWohnen;

    private Integer weSeniorinnenWohnen;

    private Integer weGenossenschaftlichesWohnen;

    private Integer weWeiteresNichtInfrastrukturrelevantesWohnen;

    private List<BauabschnittModel> bauabschnitte;
}
