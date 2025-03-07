package de.muenchen.isi.api.dto.search.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.muenchen.isi.api.dto.common.Wgs84Dto;
import de.muenchen.isi.infrastructure.entity.enums.EntityType;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes(
    {
        @JsonSubTypes.Type(value = AbfrageSearchResultDto.class, name = EntityType.Values.ABFRAGE),
        @JsonSubTypes.Type(value = BauvorhabenSearchResultDto.class, name = EntityType.Values.BAUVORHABEN),
        @JsonSubTypes.Type(
            value = InfrastruktureinrichtungSearchResultDto.class,
            name = EntityType.Values.INFRASTRUKTUREINRICHTUNG
        ),
    }
)
@Schema(
    description = "SearchResultDto",
    discriminatorProperty = "type",
    discriminatorMapping = {
        @DiscriminatorMapping(value = EntityType.Values.ABFRAGE, schema = AbfrageSearchResultDto.class),
        @DiscriminatorMapping(value = EntityType.Values.BAUVORHABEN, schema = BauvorhabenSearchResultDto.class),
        @DiscriminatorMapping(
            value = EntityType.Values.INFRASTRUKTUREINRICHTUNG,
            schema = InfrastruktureinrichtungSearchResultDto.class
        ),
    }
)
public abstract class SearchResultDto {

    private EntityType type;

    private Wgs84Dto coordinate;
}
