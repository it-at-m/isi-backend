package de.muenchen.isi.domain.model.stammdaten;

import de.muenchen.isi.domain.model.FoerderartModel;
import java.util.Set;
import lombok.Data;

@Data
public class UmlegungsschluesselModel {

    private Set<FoerderartModel> foerderarten;
}
