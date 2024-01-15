package de.muenchen.isi.infrastructure.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class Wgs84 {

    @Column
    private Double latitude;

    @Column
    private Double longitude;
}
