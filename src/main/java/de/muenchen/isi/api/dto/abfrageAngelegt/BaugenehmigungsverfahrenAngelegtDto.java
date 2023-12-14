package de.muenchen.isi.api.dto.abfrageAngelegt;

import de.muenchen.isi.api.dto.common.AdresseDto;
import de.muenchen.isi.api.dto.common.VerortungDto;
import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.api.validation.HasAllowedNumberOfDocuments;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.api.validation.StandVerfahrenBaugenehmigungsverfahrenValid;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaugenehmigungsverfahrenAngelegtDto extends AbfrageAngelegtDto {

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String aktenzeichenProLbk;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String bebauungsplannummer;

    @NotNull
    @NotUnspecified
    @StandVerfahrenBaugenehmigungsverfahrenValid
    private StandVerfahren standVerfahren;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String standVerfahrenFreieEingabe;

    @Valid
    private AdresseDto adresse;

    @Valid
    private VerortungDto verortung;

    @HasAllowedNumberOfDocuments
    private List<@Valid DokumentDto> dokumente;

    @NotNull
    private LocalDate fristBearbeitung;

    @NotEmpty
    @Size(min = 1, max = 5)
    private List<
        @Valid @NotNull AbfragevarianteBaugenehmigungsverfahrenAngelegtDto
    > abfragevariantenBaugenehmigungsverfahren;
}
