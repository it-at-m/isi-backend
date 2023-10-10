/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto;

import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlageBauleitplanverfahren;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfragevarianteBauleitplanverfahrenDto extends BaseEntityDto {

    private Integer abfragevariantenNr;

    @NotBlank
    private String name;

    private LocalDate satzungsbeschlussBaugenehmigung;

    @NotEmpty
    private List<WesentlicheRechtsgrundlageBauleitplanverfahren> wesentlicheRechtsgrundlage;

    private String wesentlicheRechtsgrundlageFreieEingabe;

    private Integer realisierungVon;

    // Geschossfläche Wohnen

    private BigDecimal gfWohnenGesamt;

    private BigDecimal gfWohnenSobonUrsaechlich;

    private BigDecimal gfWohnenBestandswohnbaurecht;

    private Boolean gfWohnenSonderwohnformen;

    private BigDecimal gfWohnenStudentischesWohnen;

    private BigDecimal gfWohnenSeniorinnenWohnen;

    private BigDecimal gfWohnenGenossenschaftlichesWohnen;

    private BigDecimal gfWohnenWeiteresNichtInfrastrukturrelevantesWohnen;

    // Anzahl Wohneinheiten

    private Integer weGesamt;

    private Boolean weSonderwohnformen;

    private Integer weStudentischesWohnen;

    private Integer weSeniorinnenWohnen;

    private Integer weGenossenschaftlichesWohnen;

    private Integer weWeiteresNichtInfrastrukturrelevantesWohnen;

    @Valid
    private AbfragevarianteSachbearbeitungDto abfragevarianteSachbearbeitung;

    @Valid
    private AbfragevarianteFachreferatDto abfragevarianteFachreferat;

    private List<@Valid @NotNull BauabschnittDto> bauabschnitte;
}
