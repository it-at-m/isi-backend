/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.SubclassMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Mapper(config = MapstructConfiguration.class, uses = { DokumentDomainMapper.class })
public abstract class AbfrageDomainMapper {

    @Autowired
    private BauvorhabenRepository bauvorhabenRepository;

    @SubclassMapping(source = Bauleitplanverfahren.class, target = BauleitplanverfahrenModel.class)
    @Mapping(source = "bauvorhaben.id", target = "bauvorhaben")
    public abstract AbfrageModel entity2Model(final Abfrage entity);

    @SubclassMapping(source = BauleitplanverfahrenModel.class, target = Bauleitplanverfahren.class)
    @Mapping(target = "bauvorhaben", ignore = true)
    public abstract Abfrage model2Entity(final AbfrageModel model) throws EntityNotFoundException;

    @AfterMapping
    public void afterMappingModel2Entity(final AbfrageModel model, @MappingTarget final Abfrage entity)
        throws EntityNotFoundException {
        final var bauvorhaben = bauvorhabenRepository
            .findById(model.getBauvorhaben())
            .orElseThrow(() -> {
                final var message = "Bauvorhaben nicht gefunden";
                log.error(message);
                return new EntityNotFoundException(message);
            });
        entity.setBauvorhaben(bauvorhaben);
    }

    @AfterMapping
    public void afterMappingModel2EntityBauleitplanverfahren(
        final BauleitplanverfahrenModel model,
        @MappingTarget final Bauleitplanverfahren entity
    ) {
        entity.setName(model.getName());
    }
}
