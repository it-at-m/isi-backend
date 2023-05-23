package de.muenchen.isi.api.dto.abfrageSachbearbeitungOffenInBearbeitung;

import de.muenchen.isi.api.dto.BaseEntityDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SachbearbeitungAbfragevarianteOffenInBearbeitungDto extends BaseEntityDto {

    private boolean isRelevant;
}
