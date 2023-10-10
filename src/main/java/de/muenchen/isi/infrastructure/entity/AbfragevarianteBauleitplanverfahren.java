/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlageBauleitplanverfahren;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UniqueNameAbfragevariantePerBauleitplanverfahren",
            columnNames = {
                "bauleitplanverfahren_abfragevarianten_id",
                "bauleitplanverfahren_abfragevarianten_sachbearbeitung_id",
                "name",
            }
        ),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfragevarianteBauleitplanverfahren extends BaseEntity {

    private Integer abfragevariantenNr;

    private String name;

    private LocalDate satzungsbeschlussBaugenehmigung;

    private List<WesentlicheRechtsgrundlageBauleitplanverfahren> wesentlicheRechtsgrundlage;

    private String wesentlicheRechtsgrundlageFreieEingabe;

    private Integer realisierungVon;

    // Geschossfläche Wohnen

    private BigDecimal gfWohnenGesamt;

    private BigDecimal gfWohnenSobonUrsaechlich;

    private BigDecimal gfWohnenBestandswohnbaurecht;

    private Boolean gfWohnenSonderwohnformen;

    private BigDecimal gfWohnenStudentischesWohnen;

    private BigDecimal gfWohnenSeniorinnenWohnen;

    private BigDecimal gfWohnenGenossenschaftlichesWohnen;

    private BigDecimal gfWohnenWeiteresNichtInfrastrukturrelevantesWohnen;

    // Anzahl Wohneinheiten

    private Integer weGesamt;

    private Boolean weSonderwohnformen;

    private Integer weStudentischesWohnen;

    private Integer weSeniorinnenWohnen;

    private Integer weGenossenschaftlichesWohnen;

    private Integer weWeiteresNichtInfrastrukturrelevantesWohnen;

    private AbfragevarianteSachbearbeitung abfragevarianteSachbearbeitung;

    private AbfragevarianteFachreferat abfragevarianteFachreferat;

    private List<Bauabschnitt> bauabschnitte;
}
