package de.muenchen.isi.domain.model.stammdaten;

import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.infrastructure.entity.enums.Bildungseinrichtung;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class VersorgungsquoteGruppenstaerkeModel extends BaseEntityModel {

    private LocalDate gueltigAb;

    private Bildungseinrichtung bildungseinrichtung;

    private BigDecimal versorgungsquotePlanungsursaechlich;

    private BigDecimal versorgungsquoteSobonUrsaechlich;

    private int gruppenstaerke;
}
