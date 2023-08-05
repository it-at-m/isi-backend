package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.list.AbfrageListElementDto;
import de.muenchen.isi.api.dto.list.BauvorhabenListElementDto;
import de.muenchen.isi.api.dto.list.InfrastruktureinrichtungListElementDto;
import de.muenchen.isi.api.dto.search.request.SearchQueryAndSortingDto;
import de.muenchen.isi.api.dto.search.request.SearchQueryDto;
import de.muenchen.isi.api.dto.search.response.SearchResultDto;
import de.muenchen.isi.api.dto.search.response.SearchResultsDto;
import de.muenchen.isi.api.dto.search.response.SuchwortSuggestionsDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.list.AbfrageListElementModel;
import de.muenchen.isi.domain.model.list.BauvorhabenListElementModel;
import de.muenchen.isi.domain.model.list.InfrastruktureinrichtungListElementModel;
import de.muenchen.isi.domain.model.search.request.SearchQueryAndSortingModel;
import de.muenchen.isi.domain.model.search.request.SearchQueryModel;
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
        source = InfrastruktureinrichtungListElementModel.class,
        target = InfrastruktureinrichtungListElementDto.class
    )
    @SubclassMapping(source = BauvorhabenListElementModel.class, target = BauvorhabenListElementDto.class)
    @SubclassMapping(source = AbfrageListElementModel.class, target = AbfrageListElementDto.class)
    SearchResultDto model2Dto(final SearchResultModel model);
}
