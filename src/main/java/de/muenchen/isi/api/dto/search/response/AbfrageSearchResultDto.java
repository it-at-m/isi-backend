package de.muenchen.isi.api.dto.search.response;

import de.muenchen.isi.api.dto.common.StadtbezirkDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
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

    private String nameAbfrage;

    private Set<StadtbezirkDto> stadtbezirke;

    private StatusAbfrage statusAbfrage;

    private LocalDate fristStellungnahme;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    private StandVorhaben standVorhaben;

    private LocalDateTime createdDateTime;
}
