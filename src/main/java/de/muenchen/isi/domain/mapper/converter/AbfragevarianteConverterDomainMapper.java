/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper.converter;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.mapper.BauabschnittDomainMapper;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteWeiteresVerfahrenModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import org.mapstruct.*;

@Mapper(config = MapstructConfiguration.class, uses = { BauabschnittDomainMapper.class })
public interface AbfragevarianteConverterDomainMapper {
    @Mappings(
        {
            @Mapping(target = "planart", ignore = true),
            @Mapping(target = "planartFreieEingabe", ignore = true),
            @Mapping(target = "sobonBerechnung", ignore = true),
            @Mapping(target = "stammdatenGueltigAb", ignore = true),
            @Mapping(target = "hasBauratendateiInput", ignore = true),
            @Mapping(target = "anmerkungBauratendateiInput", ignore = true),
            @Mapping(target = "bauratendateiInputBasis", ignore = true),
            @Mapping(target = "bauratendateiInput", ignore = true),
            @Mapping(target = "bedarfsmeldungFachreferate", ignore = true),
            @Mapping(target = "bedarfsmeldungDokumenteFachreferate", ignore = true),
            @Mapping(target = "bedarfsmeldungAbfrageersteller", ignore = true),
            @Mapping(target = "bedarfsmeldungDokumenteAbfrageersteller", ignore = true),
            @Mapping(target = "anmerkungFachreferate", ignore = true),
            @Mapping(target = "anmerkungAbfrageersteller", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauKita", ignore = true),
            @Mapping(target = "ausgeloesterBedarfImBaugebietBeruecksichtigenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungImBplanSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenSchule", ignore = true),
            @Mapping(target = "ausgeloesterBedarfMitversorgungInBestEinrichtungenNachAusbauSchule", ignore = true),
        }
    )
    AbfragevarianteBauleitplanverfahrenModel convertAbfragevarianteWeiteresVerfahrenModel2AbfragevarianteBauleitplanverfahrenModel(
        final AbfragevarianteWeiteresVerfahrenModel abfragevarianteWeiteresVerfahrenModel
    );

    @AfterMapping
    default void afterMapping(
        final AbfragevarianteWeiteresVerfahrenModel abfragevarianteWeiteresVerfahrenModel,
        @MappingTarget final AbfragevarianteBauleitplanverfahrenModel abfragevarianteBauleitplanverfahrenModel
    ) {
        abfragevarianteBauleitplanverfahrenModel.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
    }
}
