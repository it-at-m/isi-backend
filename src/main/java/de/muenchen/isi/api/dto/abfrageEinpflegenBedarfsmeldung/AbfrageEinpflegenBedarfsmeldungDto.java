package de.muenchen.isi.api.dto.abfrageEinpflegenBedarfsmeldung;

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
@JsonSubTypes({
    @JsonSubTypes.Type(
        value = BauleitplanverfahrenEinpflegenBedarfsmeldungDto.class,
        name = ArtAbfrage.Values.BAULEITPLANVERFAHREN
    ),
    @JsonSubTypes.Type(
        value = BaugenehmigungsverfahrenEinpflegenBedarfsmeldungDto.class,
        name = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN
    ),
    @JsonSubTypes.Type(
        value = WeiteresVerfahrenEinpflegenBedarfsmeldungDto.class,
        name = ArtAbfrage.Values.WEITERES_VERFAHREN
    ),
})
@Schema(
    description = "AbfrageEinpflegenBedarfsmeldungDto",
    discriminatorProperty = "artAbfrage",
    discriminatorMapping = {
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.BAULEITPLANVERFAHREN,
            schema = BauleitplanverfahrenEinpflegenBedarfsmeldungDto.class
        ),
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN,
            schema = BaugenehmigungsverfahrenEinpflegenBedarfsmeldungDto.class
        ),
        @DiscriminatorMapping(
            value = ArtAbfrage.Values.WEITERES_VERFAHREN,
            schema = WeiteresVerfahrenEinpflegenBedarfsmeldungDto.class
        ),
    }
)
public abstract class AbfrageEinpflegenBedarfsmeldungDto {

    private Long version;

    @NotUnspecified
    private ArtAbfrage artAbfrage;
}
