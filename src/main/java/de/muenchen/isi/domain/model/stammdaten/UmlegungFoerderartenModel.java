package de.muenchen.isi.domain.model.stammdaten;

import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.infrastructure.entity.Foerderart;
import java.time.LocalDate;
import java.util.Set;
import lombok.Data;

@Data
public class UmlegungFoerderartenModel extends BaseEntityModel {

    private String bezeichnung;

    private LocalDate gueltigAb;

    private Set<Foerderart> umlegungsschluessel;
}
