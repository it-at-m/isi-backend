package de.muenchen.isi.domain.model.common;

import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import lombok.Data;

@Data
public class SobonBerechnungModel {

    private Boolean isASobonBerechnung;

    private FoerdermixModel sobonFoerdermix;

    private SobonOrientierungswertJahr sobonOrientierungswertJahrSobonUrsaechlich;
}
