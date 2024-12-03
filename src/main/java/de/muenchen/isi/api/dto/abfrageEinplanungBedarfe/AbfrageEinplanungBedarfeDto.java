package de.muenchen.isi.api.dto.abfrageEinplanungBedarfe;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "artAbfrage",
    visible = true
)
@JsonSubTypes(
    {
        @JsonSubTypes.Type(
            value = BauleitplanverfahrenEinplanungBedarfeDto.class,
            name = ArtAbfrage.Values.BAULEITPLANVERFAHREN
        ),
        @JsonSubTypes.Type(
            value = BaugenehmigungsverfahrenEinplanungBedarfeDto.class,
            name = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN
        ),
        @JsonSubTypes.Type(
            value = WeiteresVerfahrenEinplanungBedarfeDto.class,
            name = ArtAbfrage.Values.WEITERES_VERFAHREN
        ),
    }
)
@Schema(
    description = "AbfrageEinplanungBedarfeDto",
    discriminatorProperty = "artAbfrage",
    discriminatorMapping = {
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.BAULEITPLANVERFAHREN,
            schema = BauleitplanverfahrenEinplanungBedarfeDto.class
        ),
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN,
            schema = BaugenehmigungsverfahrenEinplanungBedarfeDto.class
        ),
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.WEITERES_VERFAHREN,
            schema = WeiteresVerfahrenEinplanungBedarfeDto.class
        ),
    }
)
public abstract class AbfrageEinplanungBedarfeDto {

    private Long version;

    @NotUnspecified
    private ArtAbfrage artAbfrage;
}
