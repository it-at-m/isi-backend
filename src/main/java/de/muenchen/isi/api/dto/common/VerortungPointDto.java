package de.muenchen.isi.api.dto.common;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VerortungPointDto extends VerortungDto {

    @Valid
    @NotEmpty
    private PointGeometryDto point;
}
