package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import java.util.Set;
import lombok.Data;

@Data
public abstract class VerortungDto {

    private Set<@Valid StadtbezirkDto> stadtbezirke;

    private Set<@Valid BezirksteilDto> bezirksteile;

    private Set<@Valid ViertelDto> viertel;

    private Set<@Valid GemarkungDto> gemarkungen;

    private Set<@Valid KitaplanungsbereichDto> kitaplanungsbereiche;

    private Set<@Valid GrundschulsprengelDto> grundschulsprengel;

    private Set<@Valid MittelschulsprengelDto> mittelschulsprengel;
}
