package de.muenchen.isi.domain.model.list;

import de.muenchen.isi.domain.model.enums.InfrastruktureinrichtungTyp;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class InfrastruktureinrichtungListElementModel {

    private UUID id;

    private String nameEinrichtung;

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    //TBD: Stadtbezirk
}
