package de.muenchen.isi.domain.model.list;

import de.muenchen.isi.domain.model.enums.InfrastruktureinrichtungTyp;
import java.util.UUID;
import lombok.Data;

@Data
public class InfrastruktureinrichtungListElementModel {

    private UUID id;

    private String nameEinrichtung;

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;
    //TBD: StadtbezirkDto
}
