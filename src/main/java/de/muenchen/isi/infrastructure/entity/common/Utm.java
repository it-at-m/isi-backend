package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
