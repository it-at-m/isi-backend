/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto;

import de.muenchen.isi.api.dto.common.AdresseDto;
import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class AbfrageResponseDto {

    private List<DokumentDto> dokumente;

    private String allgemeineOrtsangabe;

    private AdresseDto adresse;

    private LocalDate fristStellungnahme;

    private String anmerkung;

    private StatusAbfrage statusAbfrage;

    private String bebauungsplannummer;

    private String nameAbfrage;

    private StandVorhaben standVorhaben;

    private UUID bauvorhaben;
}
