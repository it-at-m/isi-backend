package de.muenchen.isi.api.dto;

import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class AbfragevarianteSachbearbeitungDto {

    private BigDecimal geschossflaecheWohnenPlanungsursaechlich;

    private SobonOrientierungswertJahr soBoNOrientierungswertJahr;

    private String anmerkung;

    private List<BedarfsmeldungFachabteilungenDto> bedarfsmeldungFachreferate;
}
