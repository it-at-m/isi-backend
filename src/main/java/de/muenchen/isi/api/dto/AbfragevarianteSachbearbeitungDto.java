package de.muenchen.isi.api.dto;

import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class AbfragevarianteSachbearbeitungDto {

    private BigDecimal gfWohnenPlanungsursaechlich;

    private SobonOrientierungswertJahr sobonOrientierungswertJahr;

    private String anmerkung;
}
