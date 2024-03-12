package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.calculation.BauratendateiWohneinheiten;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
public class BauratendateiInput extends BaseEntity {

    @ElementCollection
    private List<String> grundschulsprengel;

    @ElementCollection
    private List<String> mittelschulsprengel;

    @ElementCollection
    private List<String> viertel;

    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "wohneinheiten_pro_jahr_pro_foerderart_id", referencedColumnName = "id")
    private List<BauratendateiWohneinheiten> wohneinheiten;
}
