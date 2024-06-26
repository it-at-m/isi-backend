package de.muenchen.isi.domain.model.search.request;

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

    private Integer page;

    private Integer pageSize;
}
