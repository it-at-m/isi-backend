package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class Flurstueck extends BaseEntity {

    @Column(nullable = true)
    private String nummer;

    @Column(nullable = true)
    private BigDecimal flaecheQm;

    @Column(nullable = true)
    private Long zaehler;

    @Column(nullable = true)
    private Long nenner;

    @Column(nullable = true)
    private Long eigentumsart;

    @Column(nullable = true)
    private String eigentumsartBedeutung;

    @Embedded
    private MultiPolygonGeometry multiPolygon;
}
