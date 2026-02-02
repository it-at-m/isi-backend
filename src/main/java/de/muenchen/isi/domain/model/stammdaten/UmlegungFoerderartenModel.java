package de.muenchen.isi.domain.model.stammdaten;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UmlegungFoerderartenModel extends BaseEntityModel {

    private String bezeichnung;

    private LocalDate gueltigAb;

    private UmlegungsschluesselModel umlegungsschluessel;
}
