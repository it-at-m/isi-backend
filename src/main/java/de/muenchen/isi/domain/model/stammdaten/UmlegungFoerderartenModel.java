package de.muenchen.isi.domain.model.stammdaten;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class UmlegungFoerderartenModel extends BaseEntityModel {

    private String bezeichnung;

    private LocalDate gueltigAb;

    private UmlegungsschluesselModel umlegungsschluessel;
}
