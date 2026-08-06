/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper.converter;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.*;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Mapper(
    componentModel = "spring",
    uses = { AbfragevarianteConverterDomainMapper.class },
    config = AbfrageMapperConfig.class
)
public abstract class AbfrageConverterDomainMapper {

    @Autowired
    private AbfragevarianteConverterDomainMapper abfragevarianteConverterDomainMapper;

    // BauleitplanverfahrenDto
    @Mapping(target = "fristBearbeitung", ignore = true)
    @Mapping(target = "start42Verfahren", ignore = true)
    @Mapping(target = "start42VerfahrenDatumUnbekannt", ignore = true)
    @Mapping(target = "bauratenmethodikVorbelegung", ignore = true)
    @Mapping(target = "verfahrensstand", ignore = true)
    @Mapping(target = "verfahrensstandFreieEingabe", ignore = true)
    @Mapping(target = "dokumente", ignore = true)
    @Mapping(target = "abfragevariantenBauleitplanverfahren", ignore = true)
    @Mapping(target = "abfragevariantenSachbearbeitungBauleitplanverfahren", ignore = true)
    @InheritConfiguration(name = "ignoreCommonFields") // MapStruct generiert keinen Code für die AbfrageDto Attribute in diesem Mapper
    public abstract BauleitplanverfahrenModel convertModel(final WeiteresVerfahrenModel weiteresVerfahrenModel);

    @AfterMapping
    void afterMapping(
        final WeiteresVerfahrenModel weiteresVerfahrenModel,
        @MappingTarget final BauleitplanverfahrenModel bauleitplanverfahrenModel
    ) {
        bauleitplanverfahrenModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);

        final var abfragevarianten = new ArrayList<AbfragevarianteBauleitplanverfahrenModel>();
        CollectionUtils.emptyIfNull(weiteresVerfahrenModel.getAbfragevariantenWeiteresVerfahren()).forEach(
            abfragevariante -> {
                final var mappedBauleitplanverfahrenVarianteModel =
                    abfragevarianteConverterDomainMapper.convertAbfragevarianteWeiteresVerfahrenModel2AbfragevarianteBauleitplanverfahrenModel(
                        abfragevariante
                    );
                abfragevarianten.add(mappedBauleitplanverfahrenVarianteModel);
            }
        );
        bauleitplanverfahrenModel.setAbfragevariantenBauleitplanverfahren(abfragevarianten);
    }
}
