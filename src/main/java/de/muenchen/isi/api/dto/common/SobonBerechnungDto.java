package de.muenchen.isi.api.dto.common;

import de.muenchen.isi.api.dto.FoerdermixDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.VersorgungsquoteHortSobon;
import lombok.Data;

@Data
public class SobonBerechnungDto {

    private Boolean isASobonBerechnung;

    private FoerdermixDto sobonFoerdermix;

    private SobonOrientierungswertJahr sobonOrientierungswertJahrSobonUrsaechlich;

    private VersorgungsquoteHortSobon versorgungsquoteHortSobon;
}
