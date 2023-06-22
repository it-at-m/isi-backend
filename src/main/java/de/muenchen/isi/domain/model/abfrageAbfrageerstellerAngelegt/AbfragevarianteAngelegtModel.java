package de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt;

import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class AbfragevarianteAngelegtModel {

    private UUID id;

    private Long version;

    private Integer abfragevariantenNr;

    private String abfragevariantenName;

    private Planungsrecht planungsrecht;

    private BigDecimal geschossflaecheWohnen;

    private BigDecimal geschossflaecheWohnenGenehmigt;

    private BigDecimal geschossflaecheWohnenFestgesetzt;

    private BigDecimal geschossflaecheWohnenSoBoNursaechlich;

    private BigDecimal geschossflaecheWohnenBestandswohnbaurecht;

    private Integer gesamtanzahlWe;

    private Integer anzahlWeBaurechtlichGenehmigt;

    private Integer anzahlWeBaurechtlichFestgesetzt;

    private Integer realisierungVon;

    private LocalDate satzungsbeschluss;

    private BigDecimal geschossflaecheGenossenschaftlicheWohnungen;

    private Boolean sonderwohnformen;

    private BigDecimal geschossflaecheStudentenwohnungen;

    private BigDecimal geschossflaecheSeniorenwohnungen;

    private BigDecimal geschossflaecheSonstiges;

    private List<BauabschnittModel> bauabschnitte;
}
