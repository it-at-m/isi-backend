package de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
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
    }
)
public class AbfrageInBearbeitungFachreferatDto {

    private Long version;

    private ArtAbfrage artAbfrage;
}
