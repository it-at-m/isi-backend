package de.muenchen.isi.infrastructure.entity.bauratendatei;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.calculation.WohneinheitenProFoerderartProJahr;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    indexes = {
        @Index(name = "bauratendatei_input_abfrgvar_baugnhmgsverfhrn_id", columnList = "abfrgvar_baugnhmgsverfhrn_id"),
        @Index(name = "bauratendatei_input_abfrgvar_bauleitplnvrfhrn_id", columnList = "abfrgvar_bauleitplnvrfhrn_id"),
        @Index(name = "bauratendatei_input_abfrgvar_weitrs_vrfhrn_id", columnList = "abfrgvar_weitrs_vrfhrn_id"),
    }
)
public class BauratendateiInput extends BaseEntity {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Set<String> grundschulsprengel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Set<String> mittelschulsprengel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Set<String> viertel;

    @ElementCollection
    @CollectionTable(
        indexes = { @Index(name = "bauratendatei_input_wohneinheiten_id_index", columnList = "bauratendatei_input_id") }
    )
    @OrderBy("jahr asc, foerderart asc")
    private List<WohneinheitenProFoerderartProJahr> wohneinheiten;
}
