package de.muenchen.isi.api.dto.list;

import java.util.List;
import lombok.Data;

@Data
public class BauvorhabenReferencedElementsDto {

    private List<InfrastruktureinrichtungListElementDto> infrastruktureinrichtungen;

    private List<AbfrageListElementDto> infrastrukturabfragen;
}
