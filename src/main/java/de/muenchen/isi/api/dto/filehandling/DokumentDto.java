package de.muenchen.isi.api.dto.filehandling;

import de.muenchen.isi.api.dto.BaseEntityDto;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtDokument;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DokumentDto extends BaseEntityDto {

    @Valid
    @NotNull
    private FilepathDto filePath;

    @NotNull
    @NotUnspecified
    private ArtDokument artDokument;

    @NotNull
    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String typDokument;

}
