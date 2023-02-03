package de.muenchen.isi.api.dto.list;

import de.muenchen.isi.domain.model.enums.AbfrageTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import lombok.Data;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class AbfrageListElementDto {

    private UUID id;

    @Size(max = 70, message = "Es sind maximal {max} Zeichen erlaubt")
    private String nameAbfrage;

    private StandVorhaben standVorhaben;

    private StatusAbfrage statusAbfrage;

    private LocalDate fristStellungnahme;

    private AbfrageTyp type;

}
