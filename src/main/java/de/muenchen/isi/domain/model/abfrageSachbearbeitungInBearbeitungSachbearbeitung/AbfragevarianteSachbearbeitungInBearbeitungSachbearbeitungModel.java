package de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class AbfragevarianteSachbearbeitungInBearbeitungSachbearbeitungModel {

    private UUID id;

    private Long version;

    private BigDecimal gfWohnenPlanungsursaechlich;

    private SobonOrientierungswertJahr sobonOrientierungswertJahr;

    private String anmerkung;
}
