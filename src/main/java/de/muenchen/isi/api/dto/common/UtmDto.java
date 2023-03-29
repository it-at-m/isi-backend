package de.muenchen.isi.api.dto.common;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class UtmDto {

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String zone;

    @NotNull
    private Double north;

    @NotNull
    private Double east;
}
