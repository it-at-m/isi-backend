package de.muenchen.isi.infrastructure.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class Utm {

    private String zone;

    @Column(nullable = false)
    private Double east;

    @Column(nullable = false)
    private Double north;
}
