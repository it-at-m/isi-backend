package de.muenchen.isi.infrastructure.entity.search.request;

import lombok.Data;

@Data
public class CompletionRequest {

    private String field;

    private int size;

    private FuzzyRequest fuzzy;
}
