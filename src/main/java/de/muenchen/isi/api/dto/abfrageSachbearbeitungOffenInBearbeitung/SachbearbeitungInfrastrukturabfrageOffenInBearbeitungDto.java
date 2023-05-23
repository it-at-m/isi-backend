package de.muenchen.isi.api.dto.abfrageSachbearbeitungOffenInBearbeitung;

import de.muenchen.isi.api.validation.UniqueRelevantAbfragevarianteValid;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
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
public class SachbearbeitungInfrastrukturabfrageOffenInBearbeitungDto extends BaseEntity {

    @Valid
    @NotNull
    private SachbearbeitungAbfrageOffenInBearbeitungDto abfrage;

    @NotEmpty
    @Size(min = 1, max = 5)
    @UniqueRelevantAbfragevarianteValid
    private List<@Valid @NotNull SachbearbeitungAbfragevarianteOffenInBearbeitungDto> abfragevarianten;
}
