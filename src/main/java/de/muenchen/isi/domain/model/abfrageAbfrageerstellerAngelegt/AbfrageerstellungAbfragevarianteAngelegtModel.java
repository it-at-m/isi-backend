package de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt;

import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfrageerstellungAbfragevarianteAngelegtModel extends BaseEntityModel {

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

    private Integer realisierungBis;

    private BigDecimal geschossflaecheGenossenschaftlicheWohnungen;

    private Boolean sonderwohnformen;

    private BigDecimal geschossflaecheStudentenwohnungen;

    private BigDecimal geschossflaecheSeniorenwohnungen;

    private BigDecimal geschossflaecheSonstiges;

    private List<BauabschnittModel> bauabschnitte;
}
