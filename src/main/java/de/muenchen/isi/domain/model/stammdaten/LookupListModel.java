package de.muenchen.isi.domain.model.stammdaten;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LookupListModel {

    private List<LookupEntryModel> list;

}
