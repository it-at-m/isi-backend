package de.muenchen.isi.api.dto.common;

import de.muenchen.isi.api.dto.BaseEntityDto;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VerortungDto extends BaseEntityDto {

    @NotEmpty
    private Set<@Valid StadtbezirkDto> stadtbezirke;

    @NotEmpty
    private Set<@Valid GemarkungDto> gemarkungen;

    @Valid
    @NotNull
    private MultiPolygonGeometryDto multiPolygon;
}
