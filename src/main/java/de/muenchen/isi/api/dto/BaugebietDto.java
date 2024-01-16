package de.muenchen.isi.api.dto;

import de.muenchen.isi.api.validation.JahrDistributionValid;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtBaulicheNutzung;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JahrDistributionValid
public class BaugebietDto extends BaseEntityDto {

    @NotBlank
    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String bezeichnung;

    @NotNull
    private ArtBaulicheNutzung artBaulicheNutzung;

    @NotNull
    @Min(1900)
    @Max(2100)
    private Integer realisierungVon;

    private BigDecimal gfWohnenGeplant;

    private BigDecimal gfWohnenBaurechtlichGenehmigt;

    private BigDecimal gfWohnenBaurechtlichFestgesetzt;

    private Integer weGeplant;

    private Integer weBaurechtlichGenehmigt;

    private Integer weBaurechtlichFestgesetzt;

    @NotEmpty
    private List<@Valid @NotNull BaurateDto> bauraten;

    /**
     * Eine Baugebiet wird als technisch markiert, sobald die Abfragevariante fachlich keinen Bauabschnitt und kein Baugebiet besitzt.
     * Die Bauraten gruppieren sich fachlich somit direkt unter der Abfragevariante.
     * Ein technisches Baugebiet fungiert somit als Dummy, um die im Datenmodell modellierte Hierarchie "Abfragevariante -> Bauabschnitt -> Baugebiet -> Baurate" sicherzustellen.
     **/
    @NotNull
    private Boolean technical;
}
