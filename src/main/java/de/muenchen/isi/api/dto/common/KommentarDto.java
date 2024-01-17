package de.muenchen.isi.api.dto.common;

import de.muenchen.isi.api.dto.BaseEntityDto;
import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.api.validation.HasAllowedNumberOfDocuments;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class KommentarDto extends BaseEntityDto {

    @Size(max = 32, message = "Es sind maximal {max} Zeichen erlaubt")
    private String datum;

    private String text;

    private UUID bauvorhaben;

    private UUID infrastruktureinrichtung;

    @HasAllowedNumberOfDocuments
    private List<@Valid DokumentDto> dokumente;
}
