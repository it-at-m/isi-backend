package de.muenchen.isi.api.dto.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Embeddable
public class Wgs84Dto {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

}
