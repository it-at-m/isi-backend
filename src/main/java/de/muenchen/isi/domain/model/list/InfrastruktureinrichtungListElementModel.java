package de.muenchen.isi.domain.model.list;

import de.muenchen.isi.domain.model.search.SearchResultModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InfrastruktureinrichtungListElementModel extends SearchResultModel {

    private UUID id;

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    private String nameEinrichtung;
    //TBD: StadtbezirkDto
}
