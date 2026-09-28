package de.muenchen.isi.api.dto.common;

import de.muenchen.isi.api.dto.BaseEntityDto;
import de.muenchen.isi.api.dto.common.BearbeitendePersonDto;
import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.api.validation.HasAllowedNumberOfDocuments;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class KommentarDto extends BaseEntityDto {

    private LocalDate erstellungsdatum;

    private String text;

    @HasAllowedNumberOfDocuments
    private List<@Valid DokumentDto> dokumente;

    private BearbeitendePersonDto bearbeitendePerson;
}
