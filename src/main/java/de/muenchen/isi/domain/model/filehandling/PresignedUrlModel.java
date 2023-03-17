package de.muenchen.isi.domain.model.filehandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PresignedUrlModel {

    private String httpMethodToUse;

    private String url;
}
