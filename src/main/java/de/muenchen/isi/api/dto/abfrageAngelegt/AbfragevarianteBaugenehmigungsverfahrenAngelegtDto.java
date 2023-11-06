/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.abfrageAngelegt;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.validation.GeschossflaecheWohnenDistributionBaugenehmigungsverfahrenValid;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.api.validation.RealisierungVonDistributionBaugenehmigungsverfahrenValid;
import de.muenchen.isi.api.validation.TechnicalAttributesValid;
import de.muenchen.isi.api.validation.WesentlicheRechtsgrundlageBaugenehmigungsverfahrenValid;
import de.muenchen.isi.api.validation.WohneinheitenDistributionBaugenehmigungsverfahrenValid;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@WohneinheitenDistributionBaugenehmigungsverfahrenValid
@GeschossflaecheWohnenDistributionBaugenehmigungsverfahrenValid
@RealisierungVonDistributionBaugenehmigungsverfahrenValid
public class AbfragevarianteBaugenehmigungsverfahrenAngelegtDto {

    private UUID id;

    private Long version;

    @NotUnspecified
    private ArtAbfrage artAbfragevariante;

    @NotNull
    private Integer abfragevariantenNr;

    @NotBlank
    @Size(max = 30, message = "Es sind maximal {max} Zeichen erlaubt")
    private String name;

    @NotEmpty
    private List<
        @WesentlicheRechtsgrundlageBaugenehmigungsverfahrenValid @NotNull WesentlicheRechtsgrundlage
    > wesentlicheRechtsgrundlage;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String wesentlicheRechtsgrundlageFreieEingabe;

    @NotNull
    @Min(1900)
    @Max(2100)
    private Integer realisierungVon;

    // Geschossfläche Wohnen

    private BigDecimal gfWohnenGesamt;

    private BigDecimal gfWohnenBaurechtlichGenehmigt;

    private BigDecimal gfWohnenBaurechtlichFestgesetzt;

    private BigDecimal gfWohnenBestandswohnbaurecht;

    @NotNull
    private Boolean gfWohnenSonderwohnformen;

    private BigDecimal gfWohnenStudentischesWohnen;

    private BigDecimal gfWohnenSeniorinnenWohnen;

    private BigDecimal gfWohnenGenossenschaftlichesWohnen;

    private BigDecimal gfWohnenWeiteresNichtInfrastrukturrelevantesWohnen;

    // Anzahl Wohneinheiten

    private Integer weGesamt;

    private Integer weBaurechtlichGenehmigt;

    private Integer weBaurechtlichFestgesetzt;

    @NotNull
    private Boolean weSonderwohnformen;

    private Integer weStudentischesWohnen;

    private Integer weSeniorinnenWohnen;

    private Integer weGenossenschaftlichesWohnen;

    private Integer weWeiteresNichtInfrastrukturrelevantesWohnen;

    @TechnicalAttributesValid
    private List<@Valid @NotNull BauabschnittDto> bauabschnitte;
}