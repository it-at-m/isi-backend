package de.muenchen.isi.api.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
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
public abstract class AbfragevarianteDto extends BaseEntityDto {

    private Integer abfragevariantenNr;

    private ArtAbfrage artAbfragevariante;

    private String name;
}
