package de.muenchen.isi.api.dto.abfrageSachbearbeitungOffenInBearbeitung;

import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.api.validation.HasAllowedNumberOfDocuments;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class SachbearbeitungAbfrageOffenInBearbeitungDto {

    @HasAllowedNumberOfDocuments
    private List<@Valid DokumentDto> dokumente;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String anmerkung;
}
