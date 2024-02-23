package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;

@Data
public abstract class VerortungDto {

    @NotNull
    private Set<@Valid StadtbezirkDto> stadtbezirke;

    @NotNull
    private Set<@Valid BezirksteilDto> bezirksteile;

    @NotNull
    private Set<@Valid ViertelDto> viertel;

    @NotNull
    private Set<@Valid GemarkungDto> gemarkungen;

    @NotNull
    private Set<@Valid KitaplanungsbereichDto> kitaplanungsbereiche;

    @NotNull
    private Set<@Valid GrundschulsprengelDto> grundschulsprengel;

    @NotNull
    private Set<@Valid MittelschulsprengelDto> mittelschulsprengel;
}
