/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.muenchen.isi.api.dto.common.AdresseDto;
import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.api.validation.HasAllowedNumberOfDocuments;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AbfrageDto {

    @HasAllowedNumberOfDocuments
    private List<@Valid DokumentDto> dokumente;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String allgemeineOrtsangabe;

    @Valid
    private AdresseDto adresse;

    @NotNull
    private LocalDate fristStellungnahme;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String anmerkung;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StatusAbfrage statusAbfrage;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String bebauungsplannummer;

    @NotBlank
    @Size(max = 70, message = "Es sind maximal {max} Zeichen erlaubt")
    private String nameAbfrage;

    @NotNull
    @NotUnspecified
    private StandVorhaben standVorhaben;

    private UUID bauvorhaben;
}
