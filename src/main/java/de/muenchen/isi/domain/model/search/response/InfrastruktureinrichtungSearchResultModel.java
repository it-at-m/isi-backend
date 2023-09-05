package de.muenchen.isi.domain.model.search.response;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InfrastruktureinrichtungSearchResultModel extends SearchResultModel {

    private UUID id;

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    private String nameEinrichtung;
    //TBD: StadtbezirkDto
}
