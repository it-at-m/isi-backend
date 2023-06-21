package de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.validation.GeschossflaecheWohnenDistributionValid;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.api.validation.RealisierungVonDistributionValid;
import de.muenchen.isi.api.validation.WohneinheitenDistributionValid;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@GeschossflaecheWohnenDistributionValid
@WohneinheitenDistributionValid
@RealisierungVonDistributionValid
public class AbfragevarianteAngelegtDto {

    private UUID id;

    private Long version;

    @NotNull
    private Integer abfragevariantenNr;

    @NotBlank
    @Size(max = 30, message = "Es sind maximal {max} Zeichen erlaubt")
    private String abfragevariantenName;

    @NotNull
    @NotUnspecified
    private Planungsrecht planungsrecht;

    private BigDecimal geschossflaecheWohnen;

    private BigDecimal geschossflaecheWohnenGenehmigt;

    private BigDecimal geschossflaecheWohnenFestgesetzt;

    private BigDecimal geschossflaecheWohnenSoBoNursaechlich;

    private BigDecimal geschossflaecheWohnenBestandswohnbaurecht;

    private Integer gesamtanzahlWe;

    private Integer anzahlWeBaurechtlichGenehmigt;

    private Integer anzahlWeBaurechtlichFestgesetzt;

    @NotNull
    @Min(1900)
    @Max(2100)
    private Integer realisierungVon;

    private LocalDate satzungsbeschluss;

    private BigDecimal geschossflaecheGenossenschaftlicheWohnungen;

    @NotNull
    private Boolean sonderwohnformen;

    private BigDecimal geschossflaecheStudentenwohnungen;

    private BigDecimal geschossflaecheSeniorenwohnungen;

    private BigDecimal geschossflaecheSonstiges;

    private List<@Valid @NotNull BauabschnittDto> bauabschnitte;
}
