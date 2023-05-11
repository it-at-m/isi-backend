package de.muenchen.isi.api.dto.abfrageAngelegt;

import de.muenchen.isi.api.dto.BaseEntityDto;
import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfragevarianteRequestDto extends BaseEntityDto {

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

    @NotNull
    @Min(1900)
    @Max(2100)
    private Integer realisierungBis;

    private BigDecimal geschossflaecheGenossenschaftlicheWohnungen;

    @NotNull
    private Boolean sonderwohnformen;

    private BigDecimal geschossflaecheStudentenwohnungen;

    private BigDecimal geschossflaecheSeniorenwohnungen;

    private BigDecimal geschossflaecheSonstiges;

    private List<@Valid @NotNull BauabschnittDto> bauabschnitte;
}
