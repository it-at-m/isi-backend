package de.muenchen.isi.api.dto.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
