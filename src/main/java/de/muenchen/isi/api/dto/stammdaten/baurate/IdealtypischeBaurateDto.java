package de.muenchen.isi.api.dto.stammdaten.baurate;

import de.muenchen.isi.api.dto.BaseEntityDto;
import de.muenchen.isi.api.validation.JahresratenValid;
import de.muenchen.isi.api.validation.RangeIdealtypischeBaurateValid;
import de.muenchen.isi.infrastructure.entity.enums.IdealtypischeBaurateTyp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@RangeIdealtypischeBaurateValid
public class IdealtypischeBaurateDto extends BaseEntityDto {

    @NotNull
    private BigDecimal von;

    @NotNull
    private BigDecimal bisExklusiv;

    @NotNull
    private IdealtypischeBaurateTyp typ;

    @Schema(description = "Die Summe der einzelnen prozentzahligen Raten muss insgesamt den Wert 1 ergeben.")
    @NotEmpty
    @JahresratenValid
    private List<@Valid JahresrateDto> jahresraten;
}
