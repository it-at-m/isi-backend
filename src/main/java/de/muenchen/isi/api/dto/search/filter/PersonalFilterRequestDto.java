package de.muenchen.isi.api.dto.search.filter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonalFilterRequestDto {

    private UUID id;

    @NotNull
    @Valid
    private FilterSettingsDto filterSettings;

    @NotEmpty
    private String filterName;
}
