package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(
    indexes = {
        @Index(name = "bauvorhaben_id_index", columnList = "bauvorhaben"),
        @Index(name = "infrastruktureinrichtung_id_index", columnList = "infrastruktureinrichtung"),
    }
)
public class Kommentar extends BaseEntity {

    @Column(nullable = true, length = 32)
    private String datum;

    // Hibernate 6: @Column(nullable = true, length = Length.LONG32) ohne "@Type(type = "org.hibernate.type.TextType")"
    @Type(type = "org.hibernate.type.TextType")
    @Column(nullable = true)
    private String text;

    @Column(nullable = true, length = 36, updatable = false)
    @Type(type = "uuid-char")
    private UUID bauvorhaben;

    @Column(nullable = true, length = 36, updatable = false)
    @Type(type = "uuid-char")
    private UUID infrastruktureinrichtung;
}
