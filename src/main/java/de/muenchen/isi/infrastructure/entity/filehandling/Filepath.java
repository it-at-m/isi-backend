package de.muenchen.isi.infrastructure.entity.filehandling;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Filepath {

    @Column(nullable = false)
    private String pathToFile;
}
