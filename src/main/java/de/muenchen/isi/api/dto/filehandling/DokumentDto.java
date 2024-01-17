package de.muenchen.isi.api.dto.filehandling;

import de.muenchen.isi.api.dto.BaseEntityDto;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtDokument;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
    @Min(0)
    private Long sizeInBytes;

    @NotNull
    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String typDokument;
}
