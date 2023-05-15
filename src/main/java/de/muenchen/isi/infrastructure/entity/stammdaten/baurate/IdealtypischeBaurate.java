package de.muenchen.isi.infrastructure.entity.stammdaten.baurate;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@Data
//@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "bezeichnung", "bezeichnungJahr" }) })
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TypeDef(name = "json", typeClass = JsonType.class)
public class IdealtypischeBaurate extends BaseEntity {

    private SelectionRange rangeWohneinheiten;

    private SelectionRange rangeGeschossflaecheWohnen;

    @Type(type = "json")
    @Column(nullable = false, columnDefinition = "json")
    private List<Jahresrate> jahresraten;
}
