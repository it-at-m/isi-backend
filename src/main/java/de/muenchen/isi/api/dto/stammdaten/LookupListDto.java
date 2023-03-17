package de.muenchen.isi.api.dto.stammdaten;

import java.util.List;
import lombok.Data;

@Data
public class LookupListDto {

    private List<LookupEntryDto> list;
}
