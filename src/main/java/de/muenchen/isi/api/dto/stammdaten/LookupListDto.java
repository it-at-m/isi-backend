package de.muenchen.isi.api.dto.stammdaten;

import lombok.Data;

import java.util.List;

@Data
public class LookupListDto {

    private List<LookupEntryDto> list;

}
