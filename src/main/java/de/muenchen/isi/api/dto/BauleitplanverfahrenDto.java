package de.muenchen.isi.api.dto;

import de.muenchen.isi.api.dto.common.AdresseDto;
import de.muenchen.isi.api.dto.common.VerortungDto;
import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.api.validation.StandVerfahrenBauleitplanverfahrenValid;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauleitplanverfahrenDto extends AbfrageDto {

    private String bebauungsplannummer;

    @NotNull
    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    @NotNull
    @NotUnspecified
    @StandVerfahrenBauleitplanverfahrenValid
    private StandVerfahren standVerfahren;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String standVerfahrenFreieEingabe;

    private AdresseDto adresse;

    @Valid
    private VerortungDto verortung;

    private List<@Valid DokumentDto> dokumente;

    @NotNull
    private LocalDate fristBearbeitung;

    @NotNull
    private UncertainBoolean offizielleMitzeichnung;

    private List<@Valid @NotNull AbfragevarianteBauleitplanverfahrenDto> abfragevarianten;

    private List<@Valid @NotNull AbfragevarianteBauleitplanverfahrenDto> abfragevariantenSachbearbeitung;
}
