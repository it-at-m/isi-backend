package de.muenchen.isi.api.dto.abfrageAngelegt;

import de.muenchen.isi.api.dto.common.AdresseDto;
import de.muenchen.isi.api.dto.common.VerortungMultiPolygonDto;
import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.api.validation.HasAllowedNumberOfDocuments;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.api.validation.StandVerfahrenWeiteresVerfahrenValid;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WeiteresVerfahrenAngelegtDto extends AbfrageAngelegtDto {

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String aktenzeichenProLbk;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String bebauungsplannummer;

    @NotNull
    @NotUnspecified
    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    @NotNull
    @NotUnspecified
    @StandVerfahrenWeiteresVerfahrenValid
    private StandVerfahren standVerfahren;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String standVerfahrenFreieEingabe;

    @Valid
    private AdresseDto adresse;

    @Valid
    private VerortungMultiPolygonDto verortung;

    @HasAllowedNumberOfDocuments
    private List<@Valid DokumentDto> dokumente;

    @NotNull
    private LocalDate fristBearbeitung;

    @NotNull
    private UncertainBoolean offizielleMitzeichnung;

    @NotEmpty
    @Size(min = 1, max = 5)
    private List<@Valid @NotNull AbfragevarianteWeiteresVerfahrenAngelegtDto> abfragevariantenWeiteresVerfahren;
}
