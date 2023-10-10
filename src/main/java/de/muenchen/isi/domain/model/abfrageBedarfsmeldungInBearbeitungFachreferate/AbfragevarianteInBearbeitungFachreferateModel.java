package de.muenchen.isi.domain.model.abfrageBedarfsmeldungInBearbeitungFachreferate;

import de.muenchen.isi.domain.model.BedarfsmeldungFachreferateModel;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class AbfragevarianteInBearbeitungFachreferateModel {

    private UUID id;

    private Long version;

    private List<BedarfsmeldungFachreferateModel> bedarfsmeldungFachreferate;
}
