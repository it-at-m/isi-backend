/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageerstellungAbfragevarianteAngelegtDto;
import de.muenchen.isi.api.dto.abfrageSachbearbeitungOffenInBearbeitung.SachbearbeitungAbfragevarianteOffenInBearbeitungDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(config = MapstructConfiguration.class, uses = { BauabschnittApiMapper.class })
public interface AbfragevarianteApiMapper {
    AbfragevarianteDto model2Dto(final AbfragevarianteModel model);

    AbfragevarianteModel dto2Model(final AbfragevarianteDto dto);

    @Mapping(target = "relevant", ignore = true)
    AbfragevarianteDto request2Response(
        final AbfrageerstellungAbfragevarianteAngelegtDto dto,
        @MappingTarget AbfragevarianteDto response
    );

    @Mappings(
        {
            @Mapping(target = "abfragevariantenNr", ignore = true),
            @Mapping(target = "abfragevariantenName", ignore = true),
            @Mapping(target = "realisierungVon", ignore = true),
            @Mapping(target = "realisierungBis", ignore = true),
            @Mapping(target = "planungsrecht", ignore = true),
            @Mapping(target = "geschossflaecheWohnen", ignore = true),
            @Mapping(target = "geschossflaecheWohnenGenehmigt", ignore = true),
            @Mapping(target = "geschossflaecheWohnenFestgesetzt", ignore = true),
            @Mapping(target = "geschossflaecheWohnenSoBoNursaechlich", ignore = true),
            @Mapping(target = "geschossflaecheWohnenBestandswohnbaurecht", ignore = true),
            @Mapping(target = "geschossflaecheGenossenschaftlicheWohnungen", ignore = true),
            @Mapping(target = "geschossflaecheStudentenwohnungen", ignore = true),
            @Mapping(target = "geschossflaecheSeniorenwohnungen", ignore = true),
            @Mapping(target = "geschossflaecheSonstiges", ignore = true),
            @Mapping(target = "anzahlWeBaurechtlichGenehmigt", ignore = true),
            @Mapping(target = "anzahlWeBaurechtlichFestgesetzt", ignore = true),
            @Mapping(target = "bauabschnitte", ignore = true),
            @Mapping(target = "gesamtanzahlWe", ignore = true),
            @Mapping(target = "sonderwohnformen", ignore = true),
        }
    )
    AbfragevarianteDto request2Response(
        final SachbearbeitungAbfragevarianteOffenInBearbeitungDto dto,
        @MappingTarget AbfragevarianteDto response
    );
}
