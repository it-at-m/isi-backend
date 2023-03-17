package de.muenchen.isi.domain.model.common;

import javax.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class WGS84Model {

    private Double latitude;

    private Double longitude;
}
