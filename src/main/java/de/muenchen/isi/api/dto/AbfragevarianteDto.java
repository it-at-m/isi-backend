package de.muenchen.isi.api.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "artAbfragevariante",
    visible = true
)
@JsonSubTypes(
    {
        @JsonSubTypes.Type(
            value = AbfragevarianteBauleitplanverfahrenDto.class,
            name = ArtAbfrage.Values.BAULEITPLANVERFAHREN
        ),
        @JsonSubTypes.Type(
            value = AbfragevarianteBaugenehmigungsverfahrenDto.class,
            name = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN
        ),
        @JsonSubTypes.Type(
            value = AbfragevarianteWeiteresVerfahrenDto.class,
            name = ArtAbfrage.Values.WEITERES_VERFAHREN
        ),
    }
)
@Schema(
    description = "AbfragevarianteDto",
    discriminatorProperty = "artAbfragevariante",
    discriminatorMapping = {
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.BAULEITPLANVERFAHREN,
            schema = AbfragevarianteBauleitplanverfahrenDto.class
        ),
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN,
            schema = AbfragevarianteBaugenehmigungsverfahrenDto.class
        ),
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.WEITERES_VERFAHREN,
            schema = AbfragevarianteWeiteresVerfahrenDto.class
        ),
    }
)
public abstract class AbfragevarianteDto extends BaseEntityDto {

    private Integer abfragevariantenNr;

    private ArtAbfrage artAbfragevariante;

    private String name;
}
