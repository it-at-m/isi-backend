package de.muenchen.isi.api.dto.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UtmDto {

    private String zone;

    private Double north;

    private Double east;
}
