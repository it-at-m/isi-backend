package de.muenchen.isi.api.dto.filehandling;

import lombok.Data;

import java.util.List;

@Data
public class DokumenteDto {

    List<DokumentDto> dokumente;

    private Integer pageNumber;

    private Integer pageSize;

    private Long totalElements;

    private Integer totalPages;

    private Boolean last;

}
