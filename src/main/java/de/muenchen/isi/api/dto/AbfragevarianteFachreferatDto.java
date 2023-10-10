package de.muenchen.isi.api.dto;

import java.util.List;
import lombok.Data;

@Data
public class AbfragevarianteFachreferatDto {

    private List<BedarfsmeldungFachreferateDto> bedarfsmeldungFachreferate;
}
