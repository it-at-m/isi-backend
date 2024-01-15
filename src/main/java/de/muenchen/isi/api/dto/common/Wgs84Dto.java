package de.muenchen.isi.api.dto.common;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class Wgs84Dto {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
