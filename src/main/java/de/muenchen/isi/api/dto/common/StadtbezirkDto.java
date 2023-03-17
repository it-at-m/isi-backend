package de.muenchen.isi.api.dto.common;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class StadtbezirkDto {

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String nummer;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String name;
}
