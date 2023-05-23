package de.muenchen.isi.api.dto.common;

import de.muenchen.isi.api.dto.BaseEntityDto;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StadtbezirkDto extends BaseEntityDto {

    private String nummer;

    private String name;

    @Valid
    @NotNull
    private MultiPolygonGeometryDto multiPolygon;
}
