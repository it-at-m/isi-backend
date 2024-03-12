package de.muenchen.isi.infrastructure.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class BearbeitendePerson {

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String organisationseinheit;
}
