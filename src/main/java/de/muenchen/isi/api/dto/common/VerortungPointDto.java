package de.muenchen.isi.api.dto.common;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VerortungPointDto extends VerortungDto {

    @Valid
    @NotNull
    private PointGeometryDto point;
}
