package de.muenchen.isi.infrastructure.entity.filter;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "personalFilterSettings")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PersonalFilter extends BaseEntity {

    /**
     * Der Filtername der systemeigenen Zeile, welche die Startseiteneinstellung eines Nutzers hält.
     */
    public static final String STARTSEITE_FILTER_NAME = "Startseiteneinstellung";

    @NotEmpty
    private String personalID;

    @NotEmpty
    private String filterName;

    /**
     * Markiert die systemeigene Zeile, welche die Startseiteneinstellung ("Meine Vorgänge") des
     * Nutzers hält. Je Nutzer existiert maximal eine solche Zeile; sie wird von der Filter-API
     * nicht ausgeliefert.
     */
    @NotNull
    private Boolean istStartseite = false;

    @Valid
    @NotNull
    @Embedded
    private FilterSettings filterSettings;
}
