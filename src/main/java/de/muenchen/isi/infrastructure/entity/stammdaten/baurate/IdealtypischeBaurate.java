package de.muenchen.isi.infrastructure.entity.stammdaten.baurate;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@Table(
    indexes = {
        @Index(
            name = "range_wohneinheiten_index",
            columnList = "wohneinheitenVon ASC, wohneinheitenBisEinschliesslich ASC"
        ),
        @Index(
            name = "range_geschossflaeche_wohnen_index",
            columnList = "geschossflaecheWohnenVon ASC, geschossflaecheWohnenBisEinschliesslich ASC"
        ),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TypeDef(name = "json", typeClass = JsonType.class)
public class IdealtypischeBaurate extends BaseEntity {

    @Column(nullable = false)
    private Long wohneinheitenVon;

    @Column(nullable = false)
    private Long wohneinheitenBisEinschliesslich;

    @Column(nullable = false)
    private BigDecimal geschossflaecheWohnenVon;

    @Column(nullable = false)
    private BigDecimal geschossflaecheWohnenBisEinschliesslich;

    @Type(type = "json")
    @Column(nullable = false, columnDefinition = "json")
    private List<Jahresrate> jahresraten;
}
