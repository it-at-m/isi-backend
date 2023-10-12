package de.muenchen.isi.api.dto;

import java.util.List;
import javax.validation.Valid;
import lombok.Data;

@Data
public class AbfragevarianteFachreferatDto {

    private List<@Valid BedarfsmeldungFachreferateDto> bedarfsmeldungFachreferate;
}
