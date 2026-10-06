package de.muenchen.isi.infrastructure.entity.filter;

import de.muenchen.isi.domain.model.enums.SchnellfilterVorgaenge;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

/**
 * Die gespeicherten Einstellungen einer persönlichen Ansicht.
 * <p>
 * Dieselbe Struktur trägt sowohl echte persönliche Filter als auch die Startseiteneinstellung
 * (siehe {@link PersonalFilter#getIstStartseite()}). Da die Startseiteneinstellung nur
 * {@code schnellfilter}, {@code sortBy} und {@code sortOrder} nutzt, sind die übrigen Felder
 * auf Datenbankebene optional; für echte Filter werden sie über
 * {@code de.muenchen.isi.api.dto.filter.FilterSettingsDto} als Pflichtfelder validiert.
 */
@Embeddable
@Data
public class FilterSettings {

    /**
     * Nur für die Startseiteneinstellung ("Meine Vorgänge") gesetzt, für echte Filter {@code null}.
     */
    @Enumerated(EnumType.STRING)
    private SchnellfilterVorgaenge schnellfilter;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SortAttribute sortBy;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SortOrder sortOrder;

    private Boolean selectBauleitplanverfahren;

    private Boolean selectBaugenehmigungsverfahren;

    private Boolean selectWeiteresVerfahren;

    private Boolean selectBauvorhaben;

    private Boolean selectGrundschule;

    private Boolean selectGsNachmittagBetreuung;

    private Boolean selectHausFuerKinder;

    private Boolean selectKindergarten;

    private Boolean selectKinderkrippe;

    private Boolean selectMittelschule;

    @ElementCollection
    @CollectionTable(
        indexes = { @Index(name = "personal_filter_stadtbezirk_nummer_id_idx", columnList = "personal_filter_id") }
    )
    private List<String> stadtbezirkNummer;

    @ElementCollection
    @CollectionTable(
        indexes = {
            @Index(name = "personal_filter_kitaplanungsbereich_kita_plb_t_id_idx", columnList = "personal_filter_id"),
        }
    )
    private List<String> kitaplanungsbereichKitaPlbT;

    @ElementCollection
    @CollectionTable(
        indexes = {
            @Index(name = "personal_filter_grundschulsprengel_nummer_id_idx", columnList = "personal_filter_id"),
        }
    )
    private List<Long> grundschulsprengelNummer;

    @ElementCollection
    @CollectionTable(
        indexes = {
            @Index(name = "personal_filter_mittelschulsprengel_nummer_id_idx", columnList = "personal_filter_id"),
        }
    )
    private List<Long> mittelschulsprengelNummer;

    private Integer realisierungsbeginnVon;

    private Integer realisierungsbeginnBis;

    private Boolean nurEigeneAbfragen;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        indexes = { @Index(name = "personal_filter_status_abfrage_id_idx", columnList = "personal_filter_id") }
    )
    private List<StatusAbfrage> statusAbfrage;

    @GenericField(name = "sobon_relevant_filter")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) check (sobon_relevant is null or sobon_relevant != 'UNSPECIFIED')")
    private UncertainBoolean sobonRelevant;

    private Integer weGesamtVon;

    private Integer weGesamtBis;

    private BigDecimal gfWohnenGeplantVon;

    private BigDecimal gfWohnenGeplantBis;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        indexes = { @Index(name = "personal_filter_verfahrensstand_id_idx", columnList = "personal_filter_id") }
    )
    private List<Verfahrensstand> verfahrensstand;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        indexes = {
            @Index(name = "personal_filter_infrastruktureinrichtung_status_id_idx", columnList = "personal_filter_id"),
        }
    )
    private List<StatusInfrastruktureinrichtung> infrastruktureinrichtungStatus;
}
