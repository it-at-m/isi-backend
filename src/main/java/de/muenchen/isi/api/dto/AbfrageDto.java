/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto;

import de.muenchen.isi.api.dto.common.AdresseDto;
import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.api.validation.HasAllowedNumberOfDocuments;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class AbfrageDto {

    @HasAllowedNumberOfDocuments
    private List<@Valid DokumentDto> dokumente;

    private String allgemeineOrtsangabe;

    @Valid
    private AdresseDto adresse;

    @NotNull
    private LocalDate fristStellungnahme;

    private String anmerkung;

    @NotNull
    private StatusAbfrage statusAbfrage;

    private String bebauungsplannummer;

    @NotBlank
    private String nameAbfrage;

    @NotNull
    @NotUnspecified
    private StandVorhaben standVorhaben;

    private UUID bauvorhaben;

}
