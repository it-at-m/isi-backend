/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.domain.service.search.SearchPreparationService;
import de.muenchen.isi.infrastructure.adapter.search.IntegerCustomSuggesterBinder;
import de.muenchen.isi.infrastructure.adapter.search.IntegerToStringValueBridge;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UniqueNameAbfragevariantePerAbfrage",
            columnNames = {
                "abfrage_abfragevarianten_id", "abfrage_abfragevarianten_sachbearbeitung_id", "abfragevariantenName",
            }
        ),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Abfragevariante extends BaseEntity {

    @Column(nullable = false)
    private Integer abfragevariantenNr;

    @Column(nullable = false)
    private boolean isRelevant;

    @Column(nullable = false, length = 30)
    private String abfragevariantenName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Planungsrecht planungsrecht;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheWohnen;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheWohnenGenehmigt;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheWohnenFestgesetzt;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheWohnenSoBoNursaechlich;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheWohnenBestandswohnbaurecht;

    @Column(nullable = true)
    private Integer gesamtanzahlWe;

    @Column(nullable = true)
    private Integer anzahlWeBaurechtlichGenehmigt;

    @Column(nullable = true)
    private Integer anzahlWeBaurechtlichFestgesetzt;

    @KeywordField(valueBridge = @ValueBridgeRef(type = IntegerToStringValueBridge.class))
    @NonStandardField(
        name = "realisierungVon" + SearchPreparationService.SUFFIX_ATTRIBUTE_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = IntegerCustomSuggesterBinder.class)
    )
    @Column(nullable = false)
    private Integer realisierungVon; // JJJJ

    @Column(nullable = true)
    private LocalDate satzungsbeschluss;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheGenossenschaftlicheWohnungen;

    @Column(nullable = false)
    private Boolean sonderwohnformen;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheStudentenwohnungen;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheSeniorenwohnungen;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal geschossflaecheSonstiges;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "abfragevariante_id")
    @OrderBy("createdDateTime asc")
    private List<Bauabschnitt> bauabschnitte;

    private AbfragevarianteSachbearbeitung abfragevarianteSachbearbeitung;
}
