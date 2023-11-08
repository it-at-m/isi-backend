package de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat;

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
            value = BauleitplanverfahrenInBearbeitungFachreferatDto.class,
            name = ArtAbfrage.Values.BAULEITPLANVERFAHREN
        ),
        @JsonSubTypes.Type(
            value = BaugenehmigungsverfahrenInBearbeitungFachreferatDto.class,
            name = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN
        ),
        @JsonSubTypes.Type(
            value = WeiteresVerfahrenInBearbeitungFachreferatDto.class,
            name = ArtAbfrage.Values.WEITERES_VERFAHREN
        ),
    }
)
@Schema(
    description = "AbfrageInBearbeitungFachreferatDto",
    discriminatorProperty = "artAbfrage",
    discriminatorMapping = {
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.BAULEITPLANVERFAHREN,
            schema = BauleitplanverfahrenInBearbeitungFachreferatDto.class
        ),
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN,
            schema = BaugenehmigungsverfahrenInBearbeitungFachreferatDto.class
        ),
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.WEITERES_VERFAHREN,
            schema = WeiteresVerfahrenInBearbeitungFachreferatDto.class
        ),
    }
)
public abstract class AbfrageInBearbeitungFachreferatDto {

    private Long version;

    @NotUnspecified
    private ArtAbfrage artAbfrage;
}
