package de.muenchen.isi.domain.model.stammdaten.baurate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class SelectionRangeModel {

    @Column(nullable = false)
    private Long von;

    @Column(nullable = false)
    private Long bisEinschliesslich;
}
