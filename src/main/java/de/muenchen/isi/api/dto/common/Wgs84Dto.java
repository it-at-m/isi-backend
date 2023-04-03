package de.muenchen.isi.api.dto.common;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
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
