/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.abfrageAngelegt;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.validation.GeschossflaecheWohnenDistributionBauleitplanverfahrenValid;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.api.validation.RealisierungVonDistributionBauleitplanverfahrenValid;
import de.muenchen.isi.api.validation.TechnicalAttributesValid;
import de.muenchen.isi.api.validation.WesentlicheRechtsgrundlageBauleitplanverfahrenValid;
import de.muenchen.isi.api.validation.WohneinheitenDistributionBauleitplanverfahrenValid;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
@WohneinheitenDistributionBauleitplanverfahrenValid
@GeschossflaecheWohnenDistributionBauleitplanverfahrenValid
@RealisierungVonDistributionBauleitplanverfahrenValid
public class AbfragevarianteBauleitplanverfahrenAngelegtDto {

    private UUID id;

    private Long version;

    @NotUnspecified
    private ArtAbfrage artAbfragevariante;

    @NotNull
    private Integer abfragevariantenNr;

    @NotBlank
    @Size(max = 30, message = "Es sind maximal {max} Zeichen erlaubt")
    private String name;

    private LocalDate satzungsbeschluss;

    @NotEmpty
    private List<@WesentlicheRechtsgrundlageBauleitplanverfahrenValid @NotNull WesentlicheRechtsgrundlage> wesentlicheRechtsgrundlage;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String wesentlicheRechtsgrundlageFreieEingabe;

    @NotNull
    @Min(1900)
    @Max(2100)
    private Integer realisierungVon;

    // Geschossfläche Wohnen

    private BigDecimal gfWohnenGesamt;

    private BigDecimal gfWohnenSobonUrsaechlich;

    private BigDecimal gfWohnenBestandswohnbaurecht;

    @NotNull
    private Boolean gfWohnenSonderwohnformen;

    private BigDecimal gfWohnenStudentischesWohnen;

    private BigDecimal gfWohnenSeniorinnenWohnen;

    private BigDecimal gfWohnenGenossenschaftlichesWohnen;

    private BigDecimal gfWohnenWeiteresNichtInfrastrukturrelevantesWohnen;

    // Anzahl Wohneinheiten

    private Integer weGesamt;

    @NotNull
    private Boolean weSonderwohnformen;

    private Integer weStudentischesWohnen;

    private Integer weSeniorinnenWohnen;

    private Integer weGenossenschaftlichesWohnen;

    private Integer weWeiteresNichtInfrastrukturrelevantesWohnen;

    @TechnicalAttributesValid
    private List<@Valid @NotNull BauabschnittDto> bauabschnitte;
}
