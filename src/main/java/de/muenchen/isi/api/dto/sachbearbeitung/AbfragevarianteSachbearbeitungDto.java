package de.muenchen.isi.api.dto.sachbearbeitung;

import de.muenchen.isi.api.dto.BaseEntityDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfragevarianteSachbearbeitungDto extends BaseEntityDto {
    private boolean isRelevant;

}
