package de.muenchen.isi.infrastructure.entity.search.filter;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Index;
import java.math.BigDecimal;
import java.util.List;

@Embeddable
public class FilterSettings {

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
    @CollectionTable(
        indexes = { @Index(name = "personal_filter_status_abfrage_id_idx", columnList = "personal_filter_id") }
    )
    private List<StatusAbfrage> statusAbfrage;

    private UncertainBoolean sobonRelevant;

    private Integer weGesamtVon;

    private Integer weGesamtBis;

    private BigDecimal gfWohnenGeplantVon;

    private BigDecimal gfWohnenGeplantBis;

    @ElementCollection
    @CollectionTable(
        indexes = { @Index(name = "personal_filter_verfahrensstand_id_idx", columnList = "personal_filter_id") }
    )
    private List<Verfahrensstand> verfahrensstand;

    @ElementCollection
    @CollectionTable(
        indexes = {
            @Index(name = "personal_filter_infrastruktureinrichtung_status_id_idx", columnList = "personal_filter_id"),
        }
    )
    private List<StatusInfrastruktureinrichtung> infrastruktureinrichtungStatus;
}
