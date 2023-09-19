package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Kommentar extends BaseEntity {

    @Column(nullable = true)
    private String datum;

    // Hibernate 6: @Column(nullable = true, length = Length.LONG32) ohne "@Type(type = "org.hibernate.type.TextType")"
    @Type(type = "org.hibernate.type.TextType")
    @Column(nullable = true)
    private String text;
}
