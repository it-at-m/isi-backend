package de.muenchen.isi.infrastructure.entity.bauratendatei;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.calculation.WohneinheitenProFoerderartProJahr;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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

    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "wohneinheiten_pro_foerderart_pro_jahr_id", referencedColumnName = "id")
    private List<WohneinheitenProFoerderartProJahr> wohneinheiten;
}
