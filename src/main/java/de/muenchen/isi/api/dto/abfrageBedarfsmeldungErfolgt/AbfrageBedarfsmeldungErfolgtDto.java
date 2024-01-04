package de.muenchen.isi.api.dto.abfrageBedarfsmeldungErfolgt;

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
            value = BauleitplanverfahrenBedarfsmeldungErfolgtDto.class,
            name = ArtAbfrage.Values.BAULEITPLANVERFAHREN
        ),
        @JsonSubTypes.Type(
            value = BaugenehmigungsverfahrenBedarfsmeldungErfolgtDto.class,
            name = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN
        ),
        @JsonSubTypes.Type(
            value = WeiteresVerfahrenBedarfsmeldungErfolgtDto.class,
            name = ArtAbfrage.Values.WEITERES_VERFAHREN
        ),
    }
)
@Schema(
    description = "AbfrageBedarfsmeldungErfolgtDto",
    discriminatorProperty = "artAbfrage",
    discriminatorMapping = {
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.BAULEITPLANVERFAHREN,
            schema = BauleitplanverfahrenBedarfsmeldungErfolgtDto.class
        ),
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN,
            schema = BaugenehmigungsverfahrenBedarfsmeldungErfolgtDto.class
        ),
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.WEITERES_VERFAHREN,
            schema = WeiteresVerfahrenBedarfsmeldungErfolgtDto.class
        ),
    }
)
public abstract class AbfrageBedarfsmeldungErfolgtDto {

    private Long version;

    @NotUnspecified
    private ArtAbfrage artAbfrage;
}
