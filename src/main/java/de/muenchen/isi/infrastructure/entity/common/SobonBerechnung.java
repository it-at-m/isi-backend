package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.infrastructure.entity.Foerdermix;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

@Data
@Embeddable
public class SobonBerechnung {

    private Boolean isASobonBerechnung;

    @Embedded
    private Foerdermix sobonFoerdermix;
}
