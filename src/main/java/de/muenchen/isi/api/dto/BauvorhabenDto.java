package de.muenchen.isi.api.dto;

import de.muenchen.isi.api.dto.common.AdresseDto;
import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.api.validation.HasAllowedNumberOfDocuments;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.BaugebietTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ZustaendigeDienststelle;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class BauvorhabenDto extends BaseEntityDto {

    @NotEmpty
    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String nameVorhaben;

    @NotEmpty
    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String eigentuemer;

    @NotNull
    private Long grundstuecksgroesse;

    @NotNull
    @NotUnspecified
    private StandVorhaben standVorhaben;

    @NotEmpty
    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String bauvorhabenNummer;

    @Valid
    private AdresseDto adresse;

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

    @NotNull
    @NotUnspecified
    private Planungsrecht planungsrecht;

    @NotNull
    @NotUnspecified
    private ZustaendigeDienststelle zustaendigkeit;

    @NotEmpty
    private List<BaugebietTyp> artFnp;

    @HasAllowedNumberOfDocuments
    private List<@Valid DokumentDto> dokumente;

}

