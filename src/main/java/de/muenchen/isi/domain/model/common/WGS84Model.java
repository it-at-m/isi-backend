package de.muenchen.isi.domain.model.common;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class WGS84Model {

    private Double latitude;

    private Double longitude;
}
