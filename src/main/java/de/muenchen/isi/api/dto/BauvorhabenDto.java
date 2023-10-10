package de.muenchen.isi.api.dto;

import de.muenchen.isi.api.dto.common.AdresseDto;
import de.muenchen.isi.api.dto.common.VerortungDto;
import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.api.validation.HasAllowedNumberOfDocuments;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtBaulicheNutzung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class BauvorhabenDto extends BaseEntityDto {

    @NotEmpty
    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String nameVorhaben;

    private BigDecimal grundstuecksgroesse;

    @NotNull
    @NotUnspecified
    private StandVorhaben standVorhaben;

    private String bauvorhabenNummer;

    @Valid
    private AdresseDto adresse;

    @Valid
    private VerortungDto verortung;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String allgemeineOrtsangabe;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String bebauungsplannummer;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String fisNummer;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String anmerkung;

    @NotNull
    @NotUnspecified
    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    @NotNull
    @NotUnspecified
    private Planungsrecht planungsrecht;

    @NotEmpty
    private List<ArtBaulicheNutzung> artFnp;

    @HasAllowedNumberOfDocuments
    private List<@Valid DokumentDto> dokumente;

    private AbfragevarianteDto relevanteAbfragevariante;
}
