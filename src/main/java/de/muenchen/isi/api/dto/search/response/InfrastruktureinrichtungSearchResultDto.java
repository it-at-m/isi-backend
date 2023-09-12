package de.muenchen.isi.api.dto.search.response;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InfrastruktureinrichtungSearchResultDto extends SearchResultDto {

    private UUID id;

    private String nameEinrichtung;

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;
    //TBD: StadtbezirkDto
}
