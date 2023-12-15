package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import lombok.Data;

@Data
public abstract class VerortungDto {

    @NotEmpty
    private Set<@Valid StadtbezirkDto> stadtbezirke;

    @NotEmpty
    private Set<@Valid GemarkungDto> gemarkungen;
}
