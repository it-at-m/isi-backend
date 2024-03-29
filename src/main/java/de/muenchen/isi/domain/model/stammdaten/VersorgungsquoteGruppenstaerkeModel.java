package de.muenchen.isi.domain.model.stammdaten;

import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VersorgungsquoteGruppenstaerkeModel extends BaseEntityModel {

    private LocalDate gueltigAb;

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    private BigDecimal versorgungsquotePlanungsursaechlich;

    private BigDecimal versorgungsquoteSobonUrsaechlich;

    private int gruppenstaerke;
}
