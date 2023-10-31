package de.muenchen.isi.infrastructure.entity.stammdaten;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class StaedtbaulicherOrientierungwert {

    @Column(nullable = false)
    private String foerderArt;

    @Column(nullable = false)
    private Long durchschnittlicheGrundflaeche;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal belegungsdichte;
}
