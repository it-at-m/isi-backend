package de.muenchen.isi.domain.model.search.request;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class SearchQueryModel {

    private String searchQuery;

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

    private List<String> filterStadtbezirkNummer;

    private List<String> filterKitaplanungsbereichKitaPlbT;

    private List<Long> filterGrundschulsprengelNummer;

    private List<Long> filterMittelschulsprengelNummer;

    private Integer filterRealisierungsbeginnVon;

    private Integer filterRealisierungsbeginnBis;

    private Boolean filterNurEigeneAbfragen;

    private List<StatusAbfrage> filterStatusAbfrage;

    private UncertainBoolean filterSobonRelevant;

    private Integer filterWeGesamtVon;

    private Integer filterWeGesamtBis;

    private BigDecimal filterGfWohnenGeplantVon;

    private BigDecimal filterGfWohnenGeplantBis;

    private List<StandVerfahren> filterStandVerfahren;

    private List<StatusInfrastruktureinrichtung> filterInfrastruktureinrichtungStatus;

    private Integer page;

    private Integer pageSize;
}
