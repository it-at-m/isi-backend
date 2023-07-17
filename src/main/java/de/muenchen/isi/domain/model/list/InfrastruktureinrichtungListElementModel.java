package de.muenchen.isi.domain.model.list;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import java.util.UUID;
import lombok.Data;

@Data
public class InfrastruktureinrichtungListElementModel {

    private UUID id;

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    private String nameEinrichtung;
    //TBD: StadtbezirkDto
}
