package de.muenchen.isi.infrastructure.entity.filehandling;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Filepath {

    @Column(nullable = false)
    private String pathToFile;
}
