package de.muenchen.isi.api.dto.abfrageBedarfsmeldungInBearbeitungFachreferate;

import de.muenchen.isi.api.dto.BedarfsmeldungFachabteilungenDto;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class AbfragevarianteInBearbeitungFachreferateDto {

    private UUID id;

    private Long version;

    private List<BedarfsmeldungFachabteilungenDto> bedarfsmeldungFachreferate;
}
