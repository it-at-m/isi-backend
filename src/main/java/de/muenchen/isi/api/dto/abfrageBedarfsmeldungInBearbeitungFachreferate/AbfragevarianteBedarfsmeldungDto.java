package de.muenchen.isi.api.dto.abfrageBedarfsmeldungInBearbeitungFachreferate;

import de.muenchen.isi.api.dto.BedarfsmeldungFachabteilungenDto;
import java.util.List;
import lombok.Data;

@Data
public class AbfragevarianteBedarfsmeldungDto {

    private List<BedarfsmeldungFachabteilungenDto> bedarfsmeldungFachreferate;
}
