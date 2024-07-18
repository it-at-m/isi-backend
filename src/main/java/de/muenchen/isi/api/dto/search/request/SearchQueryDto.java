package de.muenchen.isi.api.dto.search.request;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class SearchQueryDto {

    @NotNull
    private String searchQuery;

    @NotNull
    private Boolean selectBauleitplanverfahren;

    @NotNull
    private Boolean selectBaugenehmigungsverfahren;

    @NotNull
    private Boolean selectWeiteresVerfahren;

    @NotNull
    private Boolean selectBauvorhaben;

    @NotNull
    private Boolean selectGrundschule;

    @NotNull
    private Boolean selectGsNachmittagBetreuung;

    @NotNull
    private Boolean selectHausFuerKinder;

    @NotNull
    private Boolean selectKindergarten;

    @NotNull
    private Boolean selectKinderkrippe;

    @NotNull
    private Boolean selectMittelschule;

    private List<String> filterStadtbezirkNummer;

    private List<String> filterKitaplanungsbereichKitaPlbT;

    private List<Long> filterGrundschulsprengelNummer;

    private List<Long> filterMittelschulsprengelNummer;

    private Integer filterRealisierungVon;

    private Integer filterRealisierungBis;

    private Boolean filterNurEigeneAbfragen;

    private List<StatusAbfrage> filterStatusAbfrage;

    private UncertainBoolean filterSobonRelevantAbfrage;

    private Integer filterWeGesamtVon;

    private Integer filterWeGesamtBis;

    private BigDecimal filterGfWohnenGeplantVon;

    private BigDecimal filterGfWohnenGeplantBis;

    private List<StandVerfahren> filterStandVerfahrenAbfrage;

    private List<StatusInfrastruktureinrichtung> filterInfrastruktureinrichtungStatus;

    private List<StandVerfahren> filterStandVerfahrenBauvorhaben;

    private UncertainBoolean filterSobonRelevantBauvorhaben;

    @Positive
    private Integer page;

    @Positive
    private Integer pageSize;
}
