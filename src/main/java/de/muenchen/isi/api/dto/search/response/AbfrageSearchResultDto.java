package de.muenchen.isi.api.dto.search.response;

import de.muenchen.isi.domain.model.common.StadtbezirkModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfrageSearchResultDto extends SearchResultDto {

    private UUID id;

    private ArtAbfrage artAbfrage;

    private String name;

    private Set<StadtbezirkModel> stadtbezirke;

    private StatusAbfrage statusAbfrage;

    private LocalDate fristBearbeitung;

    private StandVerfahren standVerfahren;

    private LocalDateTime createdDateTime;

    private UUID bauvorhaben;
}
