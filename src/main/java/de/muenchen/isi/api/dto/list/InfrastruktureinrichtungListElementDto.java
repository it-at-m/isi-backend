package de.muenchen.isi.api.dto.list;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import java.util.UUID;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class InfrastruktureinrichtungListElementDto {

    private UUID id;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String nameEinrichtung;

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;
    //TBD: StadtbezirkDto
}
