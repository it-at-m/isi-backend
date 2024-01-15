package de.muenchen.isi.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
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
