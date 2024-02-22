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
    private Set<@Valid BezirksteilDto> bezirksteile;

    @NotEmpty
    private Set<@Valid ViertelDto> viertel;

    @NotEmpty
    private Set<@Valid GemarkungDto> gemarkungen;

    @NotEmpty
    private Set<@Valid KitaplanungsbereichDto> kitaplanungsbereiche;

    @NotEmpty
    private Set<@Valid GrundschulsprengelDto> grundschulsprengel;

    @NotEmpty
    private Set<@Valid MittelschulsprengelDto> mittelschulsprengel;
}
