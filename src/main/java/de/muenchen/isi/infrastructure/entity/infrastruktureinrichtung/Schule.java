/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.adapter.search.AnlassPlanungSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.AnlassPlanungValueBridge;
import de.muenchen.isi.infrastructure.entity.enums.lookup.AnlassPlanung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.repository.search.SearchwordSuggesterRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;

@Embeddable
@Data
public class Schule {

    @Column(nullable = false)
    private Integer anzahlKlassen;

    @Column(nullable = false)
    private Integer anzahlPlaetze;

    @Enumerated(EnumType.STRING)
    @Column
    private Einrichtungstraeger einrichtungstraeger;

    @FullTextField(valueBridge = @ValueBridgeRef(type = AnlassPlanungValueBridge.class))
    @NonStandardField(
        name = "anlassPlanung" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = AnlassPlanungSuggestionBinder.class)
    )
    @GenericField(name = "anlass_planung_filter")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnlassPlanung anlassPlanung;
}
