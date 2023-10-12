package de.muenchen.isi.api.dto.abfrageAngelegt;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AbfrageAngelegtDto {

    private Long version;

    private ArtAbfrage artAbfrage;

    @NotBlank
    @Size(max = 70, message = "Es sind maximal {max} Zeichen erlaubt")
    private String name;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String anmerkung;

    private UUID bauvorhaben;
}
