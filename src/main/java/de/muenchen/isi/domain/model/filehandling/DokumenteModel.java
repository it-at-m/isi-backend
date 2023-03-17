package de.muenchen.isi.domain.model.filehandling;

import java.util.List;
import lombok.Data;

@Data
public class DokumenteModel {

    List<DokumentModel> dokumente;

    private Integer pageNumber;

    private Integer pageSize;

    private Long totalElements;

    private Integer totalPages;

    private Boolean last;
}
