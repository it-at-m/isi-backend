/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper.converter;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.mapper.AbfragevarianteDomainMapper;
import de.muenchen.isi.domain.model.*;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Mapper(
    config = AbfrageConverterMapperConfig.class,
    uses = { AbfrageConverterCommonMapper.class, AbfragevarianteConverterDomainMapper.class }
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
    public abstract BauleitplanverfahrenModel convertWv2BlvModel(final WeiteresVerfahrenModel weiteresVerfahrenModel);

    /**
     * Die Methode führt die Konvertierung einer {@link WeiteresVerfahrenModel} Abfrage in eine {@link BauleitplanverfahrenModel} Abfrage durch
     *
     * @param weiteresVerfahrenModel {@link WeiteresVerfahrenModel}.
     * @param bauleitplanverfahrenModel {@link BauleitplanverfahrenModel}.
     */
    @AfterMapping
    public void afterMapping(
        final WeiteresVerfahrenModel weiteresVerfahrenModel,
        @MappingTarget final BauleitplanverfahrenModel bauleitplanverfahrenModel
    ) {
        bauleitplanverfahrenModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        bauleitplanverfahrenModel.setStart42VerfahrenDatumUnbekannt(false);

        // Abfragevarianten
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

        // Abfragevarianten Sachbearbeitung
        final var abfragevariantenSachbearbeitung = new ArrayList<AbfragevarianteBauleitplanverfahrenModel>();
        CollectionUtils.emptyIfNull(
            weiteresVerfahrenModel.getAbfragevariantenSachbearbeitungWeiteresVerfahren()
        ).forEach(abfragevariante -> {
            final var mappedBauleitplanverfahrenVarianteModel =
                abfragevarianteConverterDomainMapper.convertAbfragevarianteWeiteresVerfahrenModel2AbfragevarianteBauleitplanverfahrenModel(
                    abfragevariante
                );
            abfragevariantenSachbearbeitung.add(mappedBauleitplanverfahrenVarianteModel);
        });
        bauleitplanverfahrenModel.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            abfragevariantenSachbearbeitung
        );
    }

    // BaugenehmigungsverfahrenDto
    @Mapping(target = "fristBearbeitung", ignore = true)
    @Mapping(target = "verfahrensstand", ignore = true)
    @Mapping(target = "verfahrensstandFreieEingabe", ignore = true)
    @Mapping(target = "dokumente", ignore = true)
    @Mapping(target = "abfragevariantenBaugenehmigungsverfahren", ignore = true)
    @Mapping(target = "abfragevariantenSachbearbeitungBaugenehmigungsverfahren", ignore = true)
    @InheritConfiguration(name = "ignoreCommonFields") // MapStruct generiert keinen Code für die AbfrageDto Attribute in diesem Mapper
    public abstract BaugenehmigungsverfahrenModel convertWv2BgvModel(
        final WeiteresVerfahrenModel weiteresVerfahrenModel
    );

    /**
     * Die Methode führt die Konvertierung einer {@link WeiteresVerfahrenModel} Abfrage in eine {@link BaugenehmigungsverfahrenModel} Abfrage durch
     *
     * @param weiteresVerfahrenModel {@link WeiteresVerfahrenModel}.
     * @param baugenehmigungsverfahrenModel {@link BaugenehmigungsverfahrenModel}.
     */
    @AfterMapping
    public void afterMapping(
        final WeiteresVerfahrenModel weiteresVerfahrenModel,
        @MappingTarget final BaugenehmigungsverfahrenModel baugenehmigungsverfahrenModel
    ) {
        baugenehmigungsverfahrenModel.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);

        // Abfragevarianten
        final var abfragevarianten = new ArrayList<AbfragevarianteBaugenehmigungsverfahrenModel>();
        CollectionUtils.emptyIfNull(weiteresVerfahrenModel.getAbfragevariantenWeiteresVerfahren()).forEach(
            abfragevariante -> {
                final var mappedBaugenehmigungsverfahrenVarianteModel =
                    abfragevarianteConverterDomainMapper.convertAbfragevarianteWeiteresVerfahrenModel2AbfragevarianteBaugenehmigungsverfahrenModel(
                        abfragevariante
                    );
                abfragevarianten.add(mappedBaugenehmigungsverfahrenVarianteModel);
            }
        );
        baugenehmigungsverfahrenModel.setAbfragevariantenBaugenehmigungsverfahren(abfragevarianten);

        // Abfragevarianten Sachbearbeitung
        final var abfragevariantenSachbearbeitung = new ArrayList<AbfragevarianteBaugenehmigungsverfahrenModel>();
        CollectionUtils.emptyIfNull(
            weiteresVerfahrenModel.getAbfragevariantenSachbearbeitungWeiteresVerfahren()
        ).forEach(abfragevariante -> {
            final var mappedBaugenehmigungsverfahrenVarianteModel =
                abfragevarianteConverterDomainMapper.convertAbfragevarianteWeiteresVerfahrenModel2AbfragevarianteBaugenehmigungsverfahrenModel(
                    abfragevariante
                );
            abfragevariantenSachbearbeitung.add(mappedBaugenehmigungsverfahrenVarianteModel);
        });
        baugenehmigungsverfahrenModel.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            abfragevariantenSachbearbeitung
        );
    }
}
