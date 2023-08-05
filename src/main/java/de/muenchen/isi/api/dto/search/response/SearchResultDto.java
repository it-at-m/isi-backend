package de.muenchen.isi.api.dto.search.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.muenchen.isi.api.dto.list.AbfrageListElementDto;
import de.muenchen.isi.api.dto.list.BauvorhabenListElementDto;
import de.muenchen.isi.api.dto.list.InfrastruktureinrichtungListElementDto;
import de.muenchen.isi.domain.model.enums.SearchResultType;
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
        @JsonSubTypes.Type(value = AbfrageListElementDto.class, name = SearchResultType.Values.INFRASTRUKTURABFRAGE),
        @JsonSubTypes.Type(value = BauvorhabenListElementDto.class, name = SearchResultType.Values.BAUVORHABEN),
        @JsonSubTypes.Type(
            value = InfrastruktureinrichtungListElementDto.class,
            name = SearchResultType.Values.INFRASTRUKTUREINRICHTUNG
        ),
    }
)
public abstract class SearchResultDto {

    private SearchResultType type;
}
