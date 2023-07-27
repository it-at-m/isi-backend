package de.muenchen.isi.api.dto.list;

import de.muenchen.isi.api.dto.common.StadtbezirkDto;
import de.muenchen.isi.api.dto.search.SearchResultDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfrageListElementDto extends SearchResultDto {

    private UUID id;

    private String nameAbfrage;

    private Set<StadtbezirkDto> stadtbezirke;

    private StatusAbfrage statusAbfrage;

    private LocalDate fristStellungnahme;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;
}
