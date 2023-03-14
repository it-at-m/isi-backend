package de.muenchen.isi.infrastructure.entity.stammdaten;

import java.util.List;
import lombok.Data;

@Data
public class MimeTypeInformation {

    private String mimeType;

    private List<String> fileExtensions;
}
