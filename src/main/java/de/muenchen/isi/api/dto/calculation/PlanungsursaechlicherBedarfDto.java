package de.muenchen.isi.api.dto.calculation;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlanungsursaechlicherBedarfDto {

    @NotNull
    private List<WohneinheitenBedarfDto> wohneinheitenBedarfe;
}
