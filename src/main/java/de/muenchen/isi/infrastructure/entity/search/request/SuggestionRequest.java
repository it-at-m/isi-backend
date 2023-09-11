package de.muenchen.isi.infrastructure.entity.search.request;

import lombok.Data;

@Data
public class SuggestionRequest {

    private String text;

    private CompletionRequest completion;
}
