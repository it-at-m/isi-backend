package de.muenchen.isi.infrastructure.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class Infrastrukturplanung {

    @Column(nullable = false)
    private String infrastrukturplanung;

    @Column(nullable = false)
    private String beschlussvorlage;
}
