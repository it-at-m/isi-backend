package de.muenchen.isi.domain.model.stammdaten;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.time.LocalDate;
import lombok.Data;

@Data
public class VersorgungsquoteGruppenstaerkeModel extends BaseEntityModel {

    private LocalDate gueltigAb;

    private String bildungseinrichtungBezeichnung;

    private int versorgungsquotePlanungsursaechlich;

    private int versorgungsquoteSobonUrsaechlich;

    private int gruppenstaerke;
}
