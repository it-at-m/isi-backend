package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.Data;

@Embeddable
@Data
public class Bearbeitungshistorie {

    @Enumerated(EnumType.STRING)
    @Column
    private StatusAbfrage zielStatus;

    @Column
    private LocalDateTime zeitpunkt;

    @Embedded
    @AttributeOverrides(
        {
            @AttributeOverride(name = "name", column = @Column(name = "bearbeitende_person_name")),
            @AttributeOverride(name = "email", column = @Column(name = "bearbeitende_person_email")),
            @AttributeOverride(
                name = "organisationseinheit",
                column = @Column(name = "bearbeitende_person_organisationseinheit")
            ),
        }
    )
    private BearbeitendePerson bearbeitendePerson;
}
