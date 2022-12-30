package de.muenchen.isi.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauabschnittDto extends BaseEntityDto {

    @NotBlank
    private String bezeichnung;

    @NotEmpty
    private List<@Valid @NotNull BaugebietDto> baugebiete;

}
