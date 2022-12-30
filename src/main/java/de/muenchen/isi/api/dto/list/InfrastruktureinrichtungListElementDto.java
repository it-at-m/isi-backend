package de.muenchen.isi.api.dto.list;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import lombok.Data;

import java.util.UUID;

@Data
public class InfrastruktureinrichtungListElementDto {

    private UUID id;

    private String nameEinrichtung;

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    //TBD: Stadtbezirk
}
