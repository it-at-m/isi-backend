package de.muenchen.isi.domain.model.abfrageSachbearbeitungOffenInBearbeitung;

import de.muenchen.isi.domain.model.BaseEntityModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SachbearbeitungAbfragevarianteOffenInBearbeitungModel extends BaseEntityModel {

    private boolean isRelevant;
}
