package de.muenchen.isi.api.dto.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Embeddable
public class UtmDto {
    private String zone;

    @NotNull
    private Double north;

    @NotNull
    private Double east;
}
