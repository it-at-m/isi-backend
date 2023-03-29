package de.muenchen.isi.api.dto;

import de.muenchen.isi.infrastructure.entity.enums.lookup.BaugebietTyp;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaugebietDto extends BaseEntityDto {

    @NotBlank
    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String bezeichnung;

    @NotNull
    private BaugebietTyp baugebietTyp;

    private Long anzahlWohneinheitenBaurechtlichGenehmigt;

    private Long anzahlWohneinheitenBaurechtlichFestgesetzt;

    private Long geschossflaecheWohnenGenehmigt;

    private Long geschossflaecheWohnenFestgesetzt;

    @NotEmpty
    private List<@Valid @NotNull BaurateDto> bauraten;
}
