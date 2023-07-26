package de.muenchen.isi.domain.model.list;

import java.util.List;
import lombok.Data;

@Data
public class BauvorhabenReferencedElementsModel {

    private List<InfrastruktureinrichtungListElementModel> infrastruktureinrichtungen;

    private List<AbfrageListElementModel> infrastrukturabfragen;
}
