/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.infrastructure.adapter.search.StringSuggestionBinder;
import de.muenchen.isi.infrastructure.repository.search.SearchwordSuggesterRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Adresse implements Cloneable, Serializable {

    @FullTextField
    @NonStandardField(
        name = "strasse" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column
    private String strasse;

    @FullTextField
    @NonStandardField(
        name = "hausnummer" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column
    private String hausnummer;

    @Column
    private String plz;

    @Column
    private String ort;

    @Embedded
    private Wgs84 coordinate;

    @Column
    private String angabeLageErgaenzendeAdressinformation;
}
