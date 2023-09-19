package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Kommentar extends BaseEntity {

    @Column(nullable = true, length = 32)
    private String datum;

    // Hibernate 6: @Column(nullable = true, length = Length.LONG32) ohne "@Type(type = "org.hibernate.type.TextType")"
    @Type(type = "org.hibernate.type.TextType")
    @Column(nullable = true)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bauvorhaben_id")
    private Bauvorhaben bauvorhaben;
}
