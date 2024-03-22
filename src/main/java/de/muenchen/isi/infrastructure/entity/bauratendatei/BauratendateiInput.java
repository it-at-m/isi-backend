package de.muenchen.isi.infrastructure.entity.bauratendatei;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.calculation.WohneinheitenProFoerderartProJahr;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.OrderBy;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
public class BauratendateiInput extends BaseEntity {

    @ElementCollection
    private Set<String> grundschulsprengel;

    @ElementCollection
    private Set<String> mittelschulsprengel;

    @ElementCollection
    private Set<String> viertel;

    @ElementCollection
    @CollectionTable(
        indexes = {
            @Index(
                name = "bauratendatei_input_wohneinheiten_bauratendatei_input_id",
                columnList = "bauratendatei_input_id"
            ),
        }
    )
    @OrderBy("jahr asc, foerderart asc")
    private List<WohneinheitenProFoerderartProJahr> wohneinheiten;
}
