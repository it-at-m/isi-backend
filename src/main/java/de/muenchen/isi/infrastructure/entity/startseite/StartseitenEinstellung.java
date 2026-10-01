package de.muenchen.isi.infrastructure.entity.startseite;

import de.muenchen.isi.domain.model.enums.SchnellfilterVorgaenge;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;

/**
 * Die persönlichen Voreinstellungen eines Nutzers für den Startseitenbereich "Meine Vorgänge".
 * <p>
 * Je Nutzer (identifiziert über den Keycloak-{@code sub}) existiert maximal ein Datensatz.
 */
@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StartseitenEinstellung extends BaseEntity {

    @NotEmpty
    @Column(unique = true)
    private String personalID;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SchnellfilterVorgaenge schnellfilter;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SortAttribute sortBy;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SortOrder sortOrder;
}
