package de.muenchen.isi.domain.model.stammdaten;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LookupEntryModel {

    private String key;

    private String value;
}
