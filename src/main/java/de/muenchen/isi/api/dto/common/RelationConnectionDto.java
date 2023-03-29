package de.muenchen.isi.api.dto.common;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RelationConnectionDto {

    @NotNull
    private UUID owningSideId;

    @NotNull
    private UUID inverseSideId;
}
