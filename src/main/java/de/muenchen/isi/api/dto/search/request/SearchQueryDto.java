package de.muenchen.isi.api.dto.search.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    private List<String> filterGrundschulsprengelNummer;

    private List<String> filterMittelschulsprengelNummer;

    @Positive
    private Integer page;

    @Positive
    private Integer pageSize;
}
