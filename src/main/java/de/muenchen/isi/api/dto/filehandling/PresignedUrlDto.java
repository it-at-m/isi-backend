package de.muenchen.isi.api.dto.filehandling;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PresignedUrlDto {

    @Schema(description = "Die HTTP-Methode für den Request der Presigned-Url")
    private String httpMethodToUse;

    @Schema(description = "Die Presigned-Url")
    private String url;
}
