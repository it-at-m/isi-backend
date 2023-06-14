package de.muenchen.isi.api.dto;

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
public class BauabschnittDto extends BaseEntityDto {

    @NotBlank
    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String bezeichnung;

    @NotEmpty
    private List<@Valid @NotNull BaugebietDto> baugebiete;

    /**
     * Ein Bauabschnitt wird als technisch markiert, sobald die Abfragevariante fachlich keinen Bauabschnitt besitzt.
     * Ein technischer Bauabschnitt fungiert somit als Dummy, um die im Datenmodell modellierte Hierarchie "Abfragevariante -> Bauabschnitt -> Baugebiet -> Baurate" sicherzustellen.
     */
    @NotNull
    private Boolean technical;
}
