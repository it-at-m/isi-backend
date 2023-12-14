package de.muenchen.isi.infrastructure.entity.stammdaten.baurate;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.IdealtypischeBaurateTyp;
import de.muenchen.isi.infrastructure.repository.stammdaten.IdealtypischeBaurateRepository;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

@Entity
@Table(
    /**
     * Zur Beschleunigung der DB-Queries ausgeführt durch
     * {@link IdealtypischeBaurateRepository#findByTypAndVonLessThanEqualAndBisExklusivGreaterThan}.
     */
    indexes = { @Index(name = "idealtypische_baurate_range_index", columnList = "typ ASC, von ASC, bisExklusiv ASC") },
    uniqueConstraints = { @UniqueConstraint(columnNames = { "von", "bisExklusiv", "typ" }) }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IdealtypischeBaurate extends BaseEntity {

    @Column(nullable = false)
    private BigDecimal von;

    @Column(nullable = false)
    private BigDecimal bisExklusiv;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdealtypischeBaurateTyp typ;

    @Type(JsonBinaryType.class)
    @Column(nullable = false, columnDefinition = "jsonb")
    private List<Jahresrate> jahresraten;
}
