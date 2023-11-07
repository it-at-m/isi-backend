package de.muenchen.isi.domain.model.stammdaten;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UmlegungModel extends BaseEntityModel {

    private String bezeichnung;

    private LocalDate gueltigAb;

    private String bezeichnungFoerderart1;

    private BigDecimal prozentAnteilFoerderart1;

    private String bezeichnungFoerderart2;

    private BigDecimal prozentAnteilFoerderart2;
}
