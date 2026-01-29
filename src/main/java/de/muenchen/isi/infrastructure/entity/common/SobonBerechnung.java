package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.infrastructure.entity.Foerdermix;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.VersorgungsquoteHortSobon;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
@Embeddable
public class SobonBerechnung {

    private Boolean isASobonBerechnung;

    @Embedded
    private Foerdermix sobonFoerdermix;

    @Enumerated(EnumType.STRING)
    private SobonOrientierungswertJahr sobonOrientierungswertJahrSobonUrsaechlich;

    @Enumerated(EnumType.STRING)
    private VersorgungsquoteHortSobon versorgungsquoteHortSobon;
}
