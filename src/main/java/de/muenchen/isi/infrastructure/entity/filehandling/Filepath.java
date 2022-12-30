package de.muenchen.isi.infrastructure.entity.filehandling;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class Filepath {

    @Column(nullable = false)
    private String pathToFile;

}
