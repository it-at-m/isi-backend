package de.muenchen.isi.api.dto.filehandling;

import de.muenchen.isi.api.validation.IsFilepathWithoutLeadingPathdivider;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilepathDto {

    @NotBlank
    @Schema(
        description = "Der Dateipfad muss absolut, ohne Angabe des Buckets und ohne führenden Pfadtrenner angegeben werden. Beispiel: outerFolder/innerFolder/thefile.pdf"
    )
    @IsFilepathWithoutLeadingPathdivider
    private String pathToFile;
}
