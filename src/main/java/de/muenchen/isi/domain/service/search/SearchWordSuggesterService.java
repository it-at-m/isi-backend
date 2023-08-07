package de.muenchen.isi.domain.service.search;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.search.request.SearchQueryModel;
import de.muenchen.isi.domain.model.search.response.SuchwortSuggestionsModel;
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

    private final SearchPreparationService searchPreparationService;

    private final SearchwordSuggesterRepository searchwordSuggesterRepository;

    /**
     * Diese Methode führt die Entitätssuche für die im Methodenparameter gegebenen Informationen durch.
     *
     * @param searchQueryInformation mit der Suchquery als einzelnes Suchwort und den zu durchsuchenden Entitäten.
     *                               Es dürfen sich keine Leerzeichen zwischen den einzelnen Buchstaben befinden.
     * @return die Suchwortvorschläge für das im Parameter gegebene Wort.
     * @throws EntityNotFoundException falls keine zu durchsuchende Entitat im Methodenparameter gewählt ist.
     */
    public SuchwortSuggestionsModel searchForSearchwordSuggestion(final SearchQueryModel searchQueryInformation)
        throws EntityNotFoundException {
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
     * Diese Methode führt die Entitätssuche für die im Methodenparameter gegebenen Informationen durch.
     *
     * @param searchableEntities als Entitäten für welche die Suchwortvorschlage ermittelt werden sollen.
     * @param singleWordQuery als Suchquery. Es dürfen sich keine Leerzeichen zwischen den einzelnen Buchstaben befinden.
     * @return die Suchwortvorschläge für das im Parameter gegebene Wort.
     */
    public Stream<String> doSearchForSearchwordSuggestion(
        final List<Class<? extends BaseEntity>> searchableEntities,
        final String singleWordQuery
    ) {
        final Map<Class<? extends BaseEntity>, List<String>> attributesForSearchableEntities = searchableEntities
            .stream()
            .collect(
                Collectors.toMap(
                    searchableEntity -> searchableEntity,
                    this.searchPreparationService::getNamesOfSearchableAttributesForSearchwordSuggestion
                )
            );
        return searchwordSuggesterRepository.doSearchForSearchwordSuggestion(
            attributesForSearchableEntities,
            singleWordQuery
        );
    }
}
