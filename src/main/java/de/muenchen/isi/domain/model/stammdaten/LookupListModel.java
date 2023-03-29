package de.muenchen.isi.domain.model.stammdaten;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LookupListModel {

    private List<LookupEntryModel> list;
}
