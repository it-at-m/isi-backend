package de.muenchen.isi.infrastructure.entity.stammdaten.baurate;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.IdealtypischeBaurateTyp;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@Table(
    indexes = { @Index(name = "range_index", columnList = "typ ASC, von ASC, bisExklusiv ASC") },
    uniqueConstraints = { @UniqueConstraint(columnNames = { "von", "bisExklusiv", "typ" }) }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TypeDef(name = "json", typeClass = JsonType.class)
public class IdealtypischeBaurate extends BaseEntity {

    @Column(nullable = false)
    private BigDecimal von;

    @Column(nullable = false)
    private BigDecimal bisExklusiv;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdealtypischeBaurateTyp typ;

    @Type(type = "json")
    @Column(nullable = false, columnDefinition = "jsonb")
    private List<Jahresrate> jahresraten;
}
