package de.muenchen.isi.domain.model.stammdaten;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SobonJahrModel extends BaseEntityModel {

    private Integer jahr;

    private LocalDate gueltigAb;

    private List<SobonOrientierungswertModel> sobonOrientierungswerte;

    private List<StaedtbaulicherOrientierungswertModel> staedtebaulicheOrientierungswerte;
}
