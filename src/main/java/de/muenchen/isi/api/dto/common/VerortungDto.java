package de.muenchen.isi.api.dto.common;

import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public abstract class VerortungDto {

    @NotEmpty
    private Set<@Valid StadtbezirkDto> stadtbezirke;

    @NotEmpty
    private Set<@Valid GemarkungDto> gemarkungen;
}
