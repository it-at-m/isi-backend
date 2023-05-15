package de.muenchen.isi.infrastructure.entity.stammdaten.baurate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class SelectionRange {

    @Column(nullable = false)
    private Long von;

    @Column(nullable = false)
    private Long bisEinschliesslich;
}
