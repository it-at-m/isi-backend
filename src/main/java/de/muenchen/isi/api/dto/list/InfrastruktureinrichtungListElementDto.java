package de.muenchen.isi.api.dto.list;

import de.muenchen.isi.api.dto.search.response.SearchResultDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InfrastruktureinrichtungListElementDto extends SearchResultDto {

    private UUID id;

    private String nameEinrichtung;

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;
    //TBD: StadtbezirkDto
}
