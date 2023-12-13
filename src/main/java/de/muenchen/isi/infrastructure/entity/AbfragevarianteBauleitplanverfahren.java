/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.adapter.search.IntegerSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.IntegerToStringValueBridge;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import de.muenchen.isi.infrastructure.repository.search.SearchwordSuggesterRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;

@Entity
@DiscriminatorValue(ArtAbfrage.Values.BAULEITPLANVERFAHREN)
@Table(
    indexes = {
        @Index(
            name = "abfragevarianten_bauleitplanverfahren_id_index",
            columnList = "abfragevarianten_bauleitplanverfahren_id"
        ),
        @Index(
            name = "abfragevarianten_sachbearbeitung_bauleitplanverfahren_id_index",
            columnList = "abfragevarianten_sachbearbeitung_bauleitplanverfahren_id"
        ),
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UniqueNameAbfragevariantePerBauleitplanverfahren",
            columnNames = {
                "abfragevarianten_bauleitplanverfahren_id",
                "abfragevarianten_sachbearbeitung_bauleitplanverfahren_id",
                "name",
            }
        ),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfragevarianteBauleitplanverfahren extends Abfragevariante {

    @Column
    private LocalDate satzungsbeschluss;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private List<WesentlicheRechtsgrundlage> wesentlicheRechtsgrundlage;

    @Column
    private String wesentlicheRechtsgrundlageFreieEingabe;

    @KeywordField(valueBridge = @ValueBridgeRef(type = IntegerToStringValueBridge.class))
    @NonStandardField(
        name = "realisierungVon" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = IntegerSuggestionBinder.class)
    )
    @Column(nullable = false)
    private Integer realisierungVon;

    // Geschossfläche Wohnen

    @Column(precision = 10, scale = 2)
    private BigDecimal gfWohnenGesamt;

    @Column(precision = 10, scale = 2)
    private BigDecimal gfWohnenSobonUrsaechlich;

    @Column(precision = 10, scale = 2)
    private BigDecimal gfWohnenBestandswohnbaurecht;

    @Column(nullable = false)
    private Boolean gfWohnenSonderwohnformen;

    @Column(precision = 10, scale = 2)
    private BigDecimal gfWohnenStudentischesWohnen;

    @Column(precision = 10, scale = 2)
    private BigDecimal gfWohnenSeniorinnenWohnen;

    @Column(precision = 10, scale = 2)
    private BigDecimal gfWohnenGenossenschaftlichesWohnen;

    @Column(precision = 10, scale = 2)
    private BigDecimal gfWohnenWeiteresNichtInfrastrukturrelevantesWohnen;

    @Column(precision = 10, scale = 2)
    private BigDecimal gfWohnenPlanungsursaechlich;

    // Anzahl Wohneinheiten

    @Column
    private Integer weGesamt;

    @Column(nullable = false)
    private Boolean weSonderwohnformen;

    @Column
    private Integer weStudentischesWohnen;

    @Column
    private Integer weSeniorinnenWohnen;

    @Column
    private Integer weGenossenschaftlichesWohnen;

    @Column
    private Integer weWeiteresNichtInfrastrukturrelevantesWohnen;

    @Enumerated(EnumType.STRING)
    @Column
    private SobonOrientierungswertJahr sobonOrientierungswertJahr;

    @Column
    private String anmerkung;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "abfragevariante_bauleitplanverfahren_id", referencedColumnName = "id")
    @OrderBy("createdDateTime asc")
    private List<BedarfsmeldungFachreferate> bedarfsmeldungFachreferate;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "abfragevariante_bauleitplanverfahren_id")
    @OrderBy("createdDateTime asc")
    private List<Bauabschnitt> bauabschnitte;

    // Kindertagesbetreuung

    @Column
    private boolean ausgelBedarfImBaugebietBeruecksichtigenKita;

    @Column
    private boolean ausgelBedarfMitversorgungImBplanKita;

    @Column
    private boolean ausgelBedarfMitversorgungInBestEinrichtungenKita;

    @Column
    private boolean ausgelBedarfMitversorgungInBestEinrichtungenNachAusbauKita;

    // Schule

    @Column
    private boolean ausgelBedarfImBaugebietBeruecksichtigenSchule;

    @Column
    private boolean ausgelBedarfMitversorgungImBplanSchule;

    @Column
    private boolean ausgelBedarfMitversorgungInBestEinrichtungenSchule;

    @Column
    private boolean ausgelBedarfMitversorgungInBestEinrichtungenNachAusbauSchule;

    @Column
    private String hinweisVersorgung;
}
