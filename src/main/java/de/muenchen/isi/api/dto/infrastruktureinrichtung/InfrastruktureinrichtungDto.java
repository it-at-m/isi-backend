/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.infrastruktureinrichtung;

import de.muenchen.isi.api.dto.common.AdresseDto;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class InfrastruktureinrichtungDto {

    private Long lfdNr;

    private UUID bauvorhaben;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String allgemeineOrtsangabe;

    @Valid
    private AdresseDto adresse;

    @NotBlank
    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String nameEinrichtung;

    @NotNull
    @Min(1900)
    @Max(2100)
    private Integer fertigstellungsjahr; // JJJJ

    @NotNull
    @NotUnspecified
    private StatusInfrastruktureinrichtung status;

    @NotNull
    private Einrichtungstraeger einrichtungstraeger;

    private BigDecimal flaecheGesamtgrundstueck;

    private BigDecimal flaecheTeilgrundstueck;

    private UUID zugeordnetesBaugebiet;

}
