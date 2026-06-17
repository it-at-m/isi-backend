package de.muenchen.isi.api.dto.common;

import de.muenchen.isi.api.dto.FoerdermixDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Bauratenmethodik;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class SobonBerechnungDto {

    private Boolean isASobonBerechnung;

    private FoerdermixDto sobonFoerdermix;

    private SobonOrientierungswertJahr sobonOrientierungswertJahrSobonUrsaechlich;

    private BigDecimal versorgungsquoteHortSobon;

    private Bauratenmethodik bauratenmethodik;
}
