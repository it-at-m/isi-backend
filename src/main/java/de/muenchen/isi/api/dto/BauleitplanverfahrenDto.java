package de.muenchen.isi.api.dto;

import de.muenchen.isi.api.dto.common.AdresseDto;
import de.muenchen.isi.api.dto.common.VerortungDto;
import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahrenBauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauleitplanverfahrenDto extends BaseEntityDto {

    public ArtAbfrage getArtAbfrage() {
        return ArtAbfrage.BAULEITPLANVERFAHREN;
    }

    @NotBlank
    private String name;

    private String bebauungsplannummer;

    private UUID bauvorhaben;

    @NotNull
    private UncertainBoolean sobonRelevant;

    @NotNull
    private StandVerfahrenBauleitplanverfahren standVerfahren;

    private String standVerfahrenFreieEingabe;

    private AdresseDto adresse;

    @Valid
    private VerortungDto verortung;

    private List<@Valid DokumentDto> dokumente;

    @NotNull
    private LocalDate fristBearbeitung;

    @NotNull
    private UncertainBoolean offizielleMitzeichnung;

    private String anmerkung;

    private List<@Valid AbfragevarianteBauleitplanverfahrenDto> abfragevarianten;

    private List<@Valid AbfragevarianteBauleitplanverfahrenDto> abfragevariantenSachbearbeitung;
}
