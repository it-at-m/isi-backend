package de.muenchen.isi.domain.model.search.filter;

import de.muenchen.isi.domain.model.enums.SortAttribute;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;

@Data
public class FilterSettingsModel {

    private SortAttribute sortBy;

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

    private List<String> stadtbezirkNummer;

    private List<String> kitaplanungsbereichKitaPlbT;

    private List<Long> grundschulsprengelNummer;

    private List<Long> mittelschulsprengelNummer;

    private Integer realisierungsbeginnVon;

    private Integer realisierungsbeginnBis;

    private Boolean nurEigeneAbfragen;

    private List<StatusAbfrage> statusAbfrage;

    private UncertainBoolean sobonRelevant;

    private Integer weGesamtVon;

    private Integer weGesamtBis;

    private BigDecimal gfWohnenGeplantVon;

    private BigDecimal gfWohnenGeplantBis;

    private List<Verfahrensstand> verfahrensstand;

    private List<StatusInfrastruktureinrichtung> infrastruktureinrichtungStatus;
}
