package de.muenchen.isi.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbfragevarianteDto extends BaseEntityDto {

    private Integer abfragevariantenNr;

    @NotBlank
    @Size(max = 30, message = "Es sind maximal {max} Zeichen erlaubt")
    private String name;
}
