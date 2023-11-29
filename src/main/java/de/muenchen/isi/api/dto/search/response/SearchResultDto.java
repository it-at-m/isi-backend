package de.muenchen.isi.api.dto.search.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.muenchen.isi.domain.model.enums.SearchResultType;
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
        @JsonSubTypes.Type(value = AbfrageSearchResultDto.class, name = SearchResultType.Values.ABFRAGE),
        @JsonSubTypes.Type(value = BauvorhabenSearchResultDto.class, name = SearchResultType.Values.BAUVORHABEN),
        @JsonSubTypes.Type(
            value = InfrastruktureinrichtungSearchResultDto.class,
            name = SearchResultType.Values.INFRASTRUKTUREINRICHTUNG
        ),
    }
)
@Schema(
    description = "SearchResultDto",
    discriminatorProperty = "type",
    discriminatorMapping = {
        @DiscriminatorMapping(value = SearchResultType.Values.ABFRAGE, schema = AbfrageSearchResultDto.class),
        @DiscriminatorMapping(value = SearchResultType.Values.BAUVORHABEN, schema = BauvorhabenSearchResultDto.class),
        @DiscriminatorMapping(
            value = SearchResultType.Values.INFRASTRUKTUREINRICHTUNG,
            schema = InfrastruktureinrichtungSearchResultDto.class
        ),
    }
)
public abstract class SearchResultDto {

    private SearchResultType type;
}
