package de.muenchen.isi.api.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "artAbfrage",
    visible = true
)
@JsonSubTypes(
    {
        @JsonSubTypes.Type(value = BauleitplanverfahrenDto.class, name = ArtAbfrage.Values.BAULEITPLANVERFAHREN),
        @JsonSubTypes.Type(
            value = BaugenehmigungsverfahrenDto.class,
            name = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN
        ),
        @JsonSubTypes.Type(value = WeiteresVerfahrenDto.class, name = ArtAbfrage.Values.WEITERES_VERFAHREN),
    }
)
@Schema(
    description = "AbfrageDto",
    discriminatorProperty = "artAbfrage",
    discriminatorMapping = {
        @DiscriminatorMapping(value = ArtAbfrage.Values.BAULEITPLANVERFAHREN, schema = BauleitplanverfahrenDto.class),
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN,
            schema = BaugenehmigungsverfahrenDto.class
        ),
        @DiscriminatorMapping(value = ArtAbfrage.Values.WEITERES_VERFAHREN, schema = WeiteresVerfahrenDto.class),
    }
)
public abstract class AbfrageDto extends BaseEntityDto {

    private ArtAbfrage artAbfrage;

    private String name;

    private StatusAbfrage statusAbfrage;

    private String anmerkung;

    private UUID bauvorhaben;

    private String sub;

    private String displayName;
}
