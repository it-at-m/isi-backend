package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.search.SearchQueryForEntitiesModel;
import de.muenchen.isi.domain.model.search.SuchwortSuggestionsModel;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.repository.search.SearchwordSuggesterRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchWordSuggesterService {

    private static final int MAX_NUMBER_OF_SUGGESTION = 20;

    private final SearchPreparationService searchPreparationService;

    private final SearchwordSuggesterRepository searchwordSuggesterRepository;

    /**
     *
     *
     * @param searchQueryInformation mit der Suchquery bestehend aus einem Wort. Es dürfen sich keine Leerzeichen zwischen den einzelnen Buchstaben befinden.
     * @return die Suchwortvorschläge für das im Parameter gegebene Wort.
     */
    public SuchwortSuggestionsModel searchForSearchwordSuggestion(
        final SearchQueryForEntitiesModel searchQueryInformation
    ) throws EntityNotFoundException {
        final List<Class<? extends BaseEntity>> searchableEntities = searchPreparationService.getSearchableEntities(
            searchQueryInformation
        );
        final var foundSuchwortSuggestions =
            this.doSearchForSearchwordSuggestion(searchableEntities, searchQueryInformation.getSearchQuery())
                .collect(Collectors.toList());
        final var model = new SuchwortSuggestionsModel();
        model.setSuchwortSuggestions(foundSuchwortSuggestions);
        return model;
    }

    /**
     * Diese Methode führt die Suche zur Ermittlung der Suchwortvorschläge durch. Die Suche wird für die Entität  durchgeführt.
     *
     * @param singleWordQuery als Query bestehend aus einem Wort. Es dürfen sich keine Leerzeichen zwischen den einzelnen Buchstaben befinden.
     * @return die Suchwortvorschläge für das im Parameter gegebene Wort.
     */
    public Stream<String> doSearchForSearchwordSuggestion(
        final List<Class<? extends BaseEntity>> searchableEntities,
        final String singleWordQuery
    ) {
        final Map<Class<? extends BaseEntity>, List<String>> attributesForSearchableEntity = searchableEntities
            .stream()
            .collect(
                Collectors.toMap(
                    searchableEntity -> searchableEntity,
                    this.searchPreparationService::getNamesOfSearchableAttributesForSearchwordSuggestion
                )
            );
        return searchwordSuggesterRepository.doSearchForSearchwordSuggestion(
            attributesForSearchableEntity,
            singleWordQuery
        );
    }
}
