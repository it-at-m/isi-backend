package de.muenchen.isi.api.dto.common;

import de.muenchen.isi.api.dto.BaseEntityDto;
import java.math.BigDecimal;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GemarkungDto extends BaseEntityDto {

    private BigDecimal nummer;

    private String name;

    @NotNull
    private Set<@Valid FlurstueckDto> flurstuecke;

    @Valid
    @NotNull
    private MultiPolygonGeometryDto multiPolygon;
}
