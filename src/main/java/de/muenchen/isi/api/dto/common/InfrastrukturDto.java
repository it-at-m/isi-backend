package de.muenchen.isi.api.dto.common;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class InfrastrukturDto {

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String infrastrukturplanung;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String beschlussvorlage;
}
