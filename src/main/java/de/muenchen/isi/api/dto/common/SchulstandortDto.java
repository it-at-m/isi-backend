package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SchulstandortDto {

    private Long schulnummer;

    private String schulname;

    @Valid
    @NotNull
    private MultiPolygonGeometryDto multiPolygon;
}
