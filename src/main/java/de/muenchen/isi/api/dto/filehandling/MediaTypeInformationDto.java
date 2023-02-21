package de.muenchen.isi.api.dto.filehandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaTypeInformationDto {

    private String type;

    private String description;

    private String acronym;

}