package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.list.AbfrageListElementDto;
import de.muenchen.isi.api.dto.list.BauvorhabenListElementDto;
import de.muenchen.isi.api.dto.list.InfrastruktureinrichtungListElementDto;
import de.muenchen.isi.api.dto.search.SearchQueryForEntitiesDto;
import de.muenchen.isi.api.dto.search.SearchResultDto;
import de.muenchen.isi.api.dto.search.SearchResultsDto;
import de.muenchen.isi.api.dto.search.SuchwortSuggestionsDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.list.AbfrageListElementModel;
import de.muenchen.isi.domain.model.list.BauvorhabenListElementModel;
import de.muenchen.isi.domain.model.list.InfrastruktureinrichtungListElementModel;
import de.muenchen.isi.domain.model.search.SearchQueryForEntitiesModel;
import de.muenchen.isi.domain.model.search.SearchResultModel;
import de.muenchen.isi.domain.model.search.SearchResultsModel;
import de.muenchen.isi.domain.model.search.SuchwortSuggestionsModel;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;

@Mapper(config = MapstructConfiguration.class)
public interface SearchApiMapper {
    SearchQueryForEntitiesModel dto2Model(final SearchQueryForEntitiesDto dto);

    SuchwortSuggestionsDto model2Dto(final SuchwortSuggestionsModel model);

    SearchResultsDto model2Dto(final SearchResultsModel model);

    @SubclassMapping(
        source = InfrastruktureinrichtungListElementModel.class,
        target = InfrastruktureinrichtungListElementDto.class
    )
    @SubclassMapping(source = BauvorhabenListElementModel.class, target = BauvorhabenListElementDto.class)
    @SubclassMapping(source = AbfrageListElementModel.class, target = AbfrageListElementDto.class)
    SearchResultDto model2Dto(final SearchResultModel model);
}
