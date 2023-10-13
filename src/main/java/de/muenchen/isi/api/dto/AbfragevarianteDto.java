package de.muenchen.isi.api.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
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
    }
)
@EqualsAndHashCode(callSuper = true)
public abstract class AbfragevarianteDto extends BaseEntityDto {

    private Integer abfragevariantenNr;
    private ArtAbfrage artAbfragevariante;

    @NotBlank
    @Size(max = 30, message = "Es sind maximal {max} Zeichen erlaubt")
    private String name;
}
