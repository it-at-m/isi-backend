package de.muenchen.isi.api.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "artAbfrage",
    visible = true
)
@JsonSubTypes(
    { @JsonSubTypes.Type(value = BauleitplanverfahrenDto.class, name = ArtAbfrage.Values.BAULEITPLANVERFAHREN) }
)
public abstract class AbfrageDto extends BaseEntityDto {

    private ArtAbfrage artAbfrage;

    @NotBlank
    private String name;

    private StatusAbfrage statusAbfrage;

    private UUID bauvorhaben;

    private String sub;

    private String displayName;
}
