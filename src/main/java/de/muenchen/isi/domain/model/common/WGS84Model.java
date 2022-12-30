package de.muenchen.isi.domain.model.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@Embeddable
public class WGS84Model {

    private Double latitude;

    private Double longitude;

}

