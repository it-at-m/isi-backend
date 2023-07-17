package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.Verortung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.BaugebietTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(indexes = { @Index(name = "name_vorhaben_index", columnList = "nameVorhaben") })
@Indexed
@TypeDef(name = "json", typeClass = JsonType.class)
public class Bauvorhaben extends BaseEntity {

    @FullTextField(analyzer = "entity_analyzer_string_field")
    @Column(nullable = false, unique = true)
    private String nameVorhaben;

    @Column(nullable = false)
    private String eigentuemer;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal grundstuecksgroesse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StandVorhaben standVorhaben;

    @Column(nullable = false)
    private String bauvorhabenNummer;

    @Embedded
    private Adresse adresse;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Verortung verortung;

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
    @Column(nullable = true)
    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Planungsrecht planungsrecht;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private List<BaugebietTyp> artFnp;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "bauvorhaben_id")
    private List<Dokument> dokumente;
}
