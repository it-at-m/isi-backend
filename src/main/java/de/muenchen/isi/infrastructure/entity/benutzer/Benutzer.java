package de.muenchen.isi.infrastructure.entity.benutzer;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.filter.PersoenlicherFilter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Sämtliche persönlichen Einstellungen eines Nutzers.
 * <p>
 * Je Nutzer (identifiziert über den Keycloak-{@code sub}) existiert maximal ein Datensatz. Die
 * Einstellungen werden ausschließlich vollständig je Nutzer gelesen und geschrieben, es wird nie
 * über ihren Inhalt gesucht. Sie liegen deshalb als JSON vor, statt auf weitere Tabellen verteilt
 * zu werden; weitere Profilattribute können so ohne Datenmigration ergänzt werden.
 */
@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Benutzer extends BaseEntity {

    @NotEmpty
    @Column(unique = true)
    private String personalID;

    /**
     * Die Voreinstellungen für den Startseitenbereich "Meine Vorgänge".
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private StartseitenEinstellung startseitenEinstellung;

    /**
     * Die gespeicherten persönlichen Filter, absteigend nach letzter Änderung.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<PersoenlicherFilter> persoenlicheFilter;
}
