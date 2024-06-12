package de.muenchen.isi.infrastructure.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class Utm {

    @Column(length = 3)
    private String zone;

    @Column
    private Double east;

    @Column
    private Double north;
}
