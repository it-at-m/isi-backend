package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.search.SuchwortSuggestionsDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.search.SuchwortSuggestionsModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface SearchApiMapper {
    SuchwortSuggestionsDto model2Dto(final SuchwortSuggestionsModel model);
}
