package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.infrastructure.entity.Foerdermix;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

@Data
@Embeddable
public class SobonBerechnung {

    @Column(name = "is_a_sobon_berechnung")
    private Boolean isASobonBerechnung;

    @Embedded
    @AttributeOverrides(
        {
            @AttributeOverride(
                name = "bezeichnung",
                column = @Column(name = "sobon_foerdermix_bezeichnung", nullable = true)
            ),
            @AttributeOverride(
                name = "bezeichnungJahr",
                column = @Column(name = "sobon_foerdermix_bezeichnung_jahr", nullable = true)
            ),
        }
    )
    private Foerdermix sobonFoerdermix;
}
