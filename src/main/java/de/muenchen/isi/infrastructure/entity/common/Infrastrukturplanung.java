package de.muenchen.isi.infrastructure.entity.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
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
