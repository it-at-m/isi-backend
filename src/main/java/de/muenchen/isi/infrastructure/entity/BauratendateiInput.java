package de.muenchen.isi.infrastructure.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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

    @Column
    private String grundschulsprengel;

    @Column
    private String mittelschulsprengel;

    @Column
    private String viertel;

    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "wohneinheiten_pro_jahr_pro_foerderart_id", referencedColumnName = "id")
    private List<WohneinheitenProFoerderartProJahr> wohneinheiten;
}
