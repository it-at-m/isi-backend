package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
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

    @Embedded
    private MultiPolygonGeometry multiPolygon;
}
