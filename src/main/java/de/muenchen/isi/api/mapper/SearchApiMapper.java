package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.search.request.SearchQueryAndSortingDto;
import de.muenchen.isi.api.dto.search.request.SearchQueryDto;
import de.muenchen.isi.api.dto.search.response.AbfrageSearchResultDto;
import de.muenchen.isi.api.dto.search.response.BauvorhabenSearchResultDto;
import de.muenchen.isi.api.dto.search.response.InfrastruktureinrichtungSearchResultDto;
import de.muenchen.isi.api.dto.search.response.SearchResultDto;
import de.muenchen.isi.api.dto.search.response.SearchResultsDto;
import de.muenchen.isi.api.dto.search.response.SuchwortSuggestionsDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.search.request.SearchQueryAndSortingModel;
import de.muenchen.isi.domain.model.search.request.SearchQueryModel;
import de.muenchen.isi.domain.model.search.response.AbfrageSearchResultModel;
import de.muenchen.isi.domain.model.search.response.BauvorhabenSearchResultModel;
import de.muenchen.isi.domain.model.search.response.InfrastruktureinrichtungSearchResultModel;
import de.muenchen.isi.domain.model.search.response.SearchResultModel;
import de.muenchen.isi.domain.model.search.response.SearchResultsModel;
import de.muenchen.isi.domain.model.search.response.SuchwortSuggestionsModel;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;

@Mapper(config = MapstructConfiguration.class)
public interface SearchApiMapper {
    SearchQueryModel dto2Model(final SearchQueryDto dto);

    SearchQueryAndSortingModel dto2Model(final SearchQueryAndSortingDto dto);

    SuchwortSuggestionsDto model2Dto(final SuchwortSuggestionsModel model);

    SearchResultsDto model2Dto(final SearchResultsModel model);

    @SubclassMapping(
        source = InfrastruktureinrichtungSearchResultModel.class,
        target = InfrastruktureinrichtungSearchResultDto.class
    )
    @SubclassMapping(source = BauvorhabenSearchResultModel.class, target = BauvorhabenSearchResultDto.class)
    @SubclassMapping(source = AbfrageSearchResultModel.class, target = AbfrageSearchResultDto.class)
    SearchResultDto model2Dto(final SearchResultModel model);
}
