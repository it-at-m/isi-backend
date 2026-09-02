package de.muenchen.isi.api.dto.search.filter;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonalFilterRequestDto {

    private UUID id;

    private String personalID;

    private FilterSettingsDto filterSettings;

    private String filterName;
}
