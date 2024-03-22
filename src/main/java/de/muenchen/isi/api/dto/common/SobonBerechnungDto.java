package de.muenchen.isi.api.dto.common;

import de.muenchen.isi.api.dto.FoerdermixDto;
import lombok.Data;

@Data
public class SobonBerechnungDto {

    private Boolean isASobonBerechnung;

    private FoerdermixDto sobonFoerdermix;
}
