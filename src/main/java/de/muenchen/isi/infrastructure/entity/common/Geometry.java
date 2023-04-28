package de.muenchen.isi.infrastructure.entity.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class Geometry {

    @Column(nullable = false)
    private String type;
}
