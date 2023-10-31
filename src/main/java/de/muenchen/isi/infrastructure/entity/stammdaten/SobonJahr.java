package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SobonJahr extends BaseEntity {

    @Column(nullable = false)
    private Integer jahr; // JJJJ

    @Column(nullable = false)
    private LocalDate gueltigAb;

    @ElementCollection
    private List<StaedtbaulicherOrientierungwert> staedtebaulicheOrientierungswerte;

    @ElementCollection
    private List<SobonOrientierungswert> sobonOrientierungswerte;
}
