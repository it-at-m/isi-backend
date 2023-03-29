package de.muenchen.isi.infrastructure.entity.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class Stadtbezirk {

    @Column(nullable = false)
    private String nummer;

    @Column(nullable = false)
    private String name;
}
