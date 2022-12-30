package de.muenchen.isi.infrastructure.entity.stammdaten;

import lombok.Data;

import java.util.List;

@Data
public class MimeTypeInformation {
    
    private String mimeType;

    private List<String> fileExtensions;

}
