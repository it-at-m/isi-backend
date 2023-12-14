package de.muenchen.isi.api.dto.common;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class RelationConnectionDto {

    @NotNull
    private UUID owningSideId;

    @NotNull
    private UUID inverseSideId;
}
