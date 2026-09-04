package de.muenchen.isi.api.dto.search.filter;

import de.muenchen.isi.api.dto.BaseEntityDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PersonalFilterResponseDto extends BaseEntityDto {

    private FilterSettingsDto filterSettings;

    private String filterName;
}
