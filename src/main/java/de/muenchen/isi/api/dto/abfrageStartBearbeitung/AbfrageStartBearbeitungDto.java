package de.muenchen.isi.api.dto.abfrageStartBearbeitung;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.util.UUID;
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
            value = BauleitplanverfahrenStartBearbeitungDto.class,
            name = ArtAbfrage.Values.BAULEITPLANVERFAHREN
        ),
        @JsonSubTypes.Type(
            value = BaugenehmigungsverfahrenStartBearbeitungDto.class,
            name = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN
        ),
        @JsonSubTypes.Type(
            value = WeiteresVerfahrenStartBearbeitungDto.class,
            name = ArtAbfrage.Values.WEITERES_VERFAHREN
        ),
    }
)
@Schema(
    description = "AbfrageStartBearbeitungDto",
    discriminatorProperty = "artAbfrage",
    discriminatorMapping = {
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.BAULEITPLANVERFAHREN,
            schema = BauleitplanverfahrenStartBearbeitungDto.class
        ),
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN,
            schema = BaugenehmigungsverfahrenStartBearbeitungDto.class
        ),
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.WEITERES_VERFAHREN,
            schema = WeiteresVerfahrenStartBearbeitungDto.class
        ),
    }
)
public abstract class AbfrageStartBearbeitungDto {

    private Long version;

    @NotUnspecified
    private ArtAbfrage artAbfrage;

    private UUID bauvorhaben;

    @Size(max = 8000, message = "Es sind maximal {max} Zeichen erlaubt")
    private String linkEakte;
}
