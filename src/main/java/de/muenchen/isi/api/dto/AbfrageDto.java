package de.muenchen.isi.api.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.muenchen.isi.api.dto.common.BearbeitungshistorieDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "AbfrageDto", discriminatorProperty = "artAbfrage")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "artAbfrage",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BauleitplanverfahrenDto.class, name = ArtAbfrage.Values.BAULEITPLANVERFAHREN),
    @JsonSubTypes.Type(value = BaugenehmigungsverfahrenDto.class, name = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN),
    @JsonSubTypes.Type(value = WeiteresVerfahrenDto.class, name = ArtAbfrage.Values.WEITERES_VERFAHREN),
})
public abstract class AbfrageDto extends BaseEntityDto {

    private ArtAbfrage artAbfrage;

    private String name;

    private StatusAbfrage statusAbfrage;

    private String anmerkung;

    private UUID bauvorhaben;

    private String sub;

    private String displayName;

    private String linkEakte;

    private List<BearbeitungshistorieDto> bearbeitungshistorie = new ArrayList<>();
}
