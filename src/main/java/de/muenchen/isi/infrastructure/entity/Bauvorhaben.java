package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.enums.lookup.BaugebietTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ZustaendigeDienststelle;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Bauvorhaben extends BaseEntity {

    @Column(nullable = false)
    private String nameVorhaben;

    @Column(nullable = false)
    private String eigentuemer;

    @Column(nullable = false)
    private Long grundstuecksgroesse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StandVorhaben standVorhaben;

    @Column(nullable = false)
    private String bauvorhabenNummer;

    @Embedded
    private Adresse adresse;

    @Column(nullable = true)
    private String allgemeineOrtsangabe;

    @Column(nullable = true)
    private String bebauungsplannummer;

    @Column(nullable = true)
    private String fisNummer;

    @Column(nullable = true)
    private String anmerkung;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) not null check (sobon_relevant != 'UNSPECIFIED')")
    private UncertainBoolean sobonRelevant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Planungsrecht planungsrecht;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ZustaendigeDienststelle zustaendigkeit;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private List<BaugebietTyp> artFnp;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Dokument> dokumente;

}
