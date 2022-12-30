package de.muenchen.isi.api.dto.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class RelationConnectionDto {

    @NotNull
    private UUID owningSideId;

    @NotNull
    private UUID inverseSideId;

}
