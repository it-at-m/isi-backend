package de.muenchen.isi.infrastructure.entity.stammdaten.baurate;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
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
            columnList = "range_wohneinheiten_von DESC, range_wohneinheiten_bis_einschliesslich DESC"
        ),
        @Index(
            name = "range_geschossflaeche_wohnen_index",
            columnList = "range_geschossflaeche_wohnen_von DESC, range_geschossflaeche_wohnen_bis_einschliesslich DESC"
        ),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TypeDef(name = "json", typeClass = JsonType.class)
public class IdealtypischeBaurate extends BaseEntity {

    @AttributeOverrides(
        {
            @AttributeOverride(name = "von", column = @Column(name = "range_wohneinheiten_von", nullable = false)),
            @AttributeOverride(
                name = "bisEinschliesslich",
                column = @Column(name = "range_wohneinheiten_bis_einschliesslich", nullable = false)
            ),
        }
    )
    private SelectionRange rangeWohneinheiten;

    @AttributeOverrides(
        {
            @AttributeOverride(
                name = "von",
                column = @Column(name = "range_geschossflaeche_wohnen_von", nullable = false)
            ),
            @AttributeOverride(
                name = "bisEinschliesslich",
                column = @Column(name = "range_geschossflaeche_wohnen_bis_einschliesslich", nullable = false)
            ),
        }
    )
    private SelectionRange rangeGeschossflaecheWohnen;

    @Type(type = "json")
    @Column(nullable = false, columnDefinition = "json")
    private List<Jahresrate> jahresraten;
}
