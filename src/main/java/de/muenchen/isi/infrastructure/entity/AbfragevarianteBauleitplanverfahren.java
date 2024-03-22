/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.adapter.search.IntegerSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.IntegerToStringValueBridge;
import de.muenchen.isi.infrastructure.entity.bauratendatei.BauratendateiInput;
import de.muenchen.isi.infrastructure.entity.common.SobonBerechnung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import de.muenchen.isi.infrastructure.repository.search.SearchwordSuggesterRepository;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;

@Entity(name = "abfrgvar_bauleitplnvrfhrn")
@DiscriminatorValue(ArtAbfrage.Values.BAULEITPLANVERFAHREN)
@Table(
    name = "abfrgvar_bauleitplnvrfhrn",
    indexes = {
        @Index(name = "abfrgvar_bauleitplnvrfhrn_id_index", columnList = "abfrgvar_bauleitplnvrfhrn_id"),
        @Index(
            name = "abfrgvar_schbrbtng_bauleitplnvrfhrn_id_index",
            columnList = "abfrgvar_schbrbtng_bauleitplnvrfhrn_id"
        ),
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UniqueNameAbfragevariantePerBauleitplanverfahren",
            columnNames = { "abfrgvar_bauleitplnvrfhrn_id", "abfrgvar_schbrbtng_bauleitplnvrfhrn_id", "name" }
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

    @Column(length = 1000)
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

    @Embedded
    @AttributeOverrides(
        {
            @AttributeOverride(
                name = "isASobonBerechnung",
                column = @Column(name = "is_a_sobon_berechnung", nullable = true)
            ),
            @AttributeOverride(
                name = "sobonFoerdermix.bezeichnung",
                column = @Column(name = "sobon_foerdermix_bezeichnung", nullable = true)
            ),
            @AttributeOverride(
                name = "sobonFoerdermix.bezeichnungJahr",
                column = @Column(name = "sobon_foerdermix_bezeichnung_jahr", nullable = true)
            ),
        }
    )
    private SobonBerechnung sobonBerechnung;

    @Column
    private LocalDate stammdatenGueltigAb;

    @Column(length = 1000)
    private String anmerkung;

    @Column
    private Boolean hasBauratendateiInputs;

    @Column(length = 1000)
    private String anmerkungBauratendateiInputs;

    @OneToOne(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "abfragevariante_bauleitplanverfahren_bauratendatei_basis_id", referencedColumnName = "id")
    private BauratendateiInput bauratendateiInputBasis;

    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "abfragevariante_bauleitplanverfahren_bauratendatei_id", referencedColumnName = "id")
    @OrderBy("createdDateTime asc")
    private List<BauratendateiInput> bauratendateiInputs;

    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "abfrgvar_bauleitplnvrfhrn_fachreferate_id", referencedColumnName = "id")
    @OrderBy("createdDateTime asc")
    private List<Bedarfsmeldung> bedarfsmeldungFachreferate;

    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "abfrgvar_bauleitplnvrfhrn_abfrageersteller_id", referencedColumnName = "id")
    @OrderBy("createdDateTime asc")
    private List<Bedarfsmeldung> bedarfsmeldungAbfrageersteller;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "abfrgvar_bauleitplnvrfhrn_id")
    @OrderBy("createdDateTime asc")
    private List<Bauabschnitt> bauabschnitte;

    // Kindertagesbetreuung

    @Column(name = "ausglstr_bdrf_im_bgbt_brckschtgn_kita")
    private boolean ausgeloesterBedarfImBaugebietBeruecksichtigenKita;

    @Column(name = "ausglstr_bdrf_mtvrsrg_im_bplan_kita")
    private boolean ausgeloesterBedarfMitversorgungImBplanKita;

    @Column(name = "ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_kita")
    private boolean ausgeloesterBedarfMitversorgungInBestEinrichtungenKita;

    @Column(name = "ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_nch_asbau_kita")
    private boolean ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita;

    // Schule

    @Column(name = "ausglstr_bdrf_im_bgbt_brckschtgn_schule")
    private boolean ausgeloesterBedarfImBaugebietBeruecksichtigenSchule;

    @Column(name = "ausglstr_bdrf_mtvrsrg_im_bplan_schule")
    private boolean ausgeloesterBedarfMitversorgungImBplanSchule;

    @Column(name = "ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_schule")
    private boolean ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule;

    @Column(name = "ausglstr_bdrf_mtvrsrg_in_bsthnd_einr_nch_asbau_schule")
    private boolean ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule;

    @Column(length = 1000)
    private String hinweisVersorgung;
}
