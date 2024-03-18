package de.muenchen.isi.domain.model.common;

import de.muenchen.isi.domain.model.FoerdermixModel;
import lombok.Data;

@Data
public class SobonBerechnungModel {

    private Boolean isASobonBerechnung;

    private FoerdermixModel sobonFoerdermix;
}
