package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.repository.AbfragevarianteRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Mapper(
    config = MapstructConfiguration.class,
    uses = { DokumentDomainMapper.class, VerortungDomainMapper.class, AdresseDomainMapper.class }
)
public abstract class BauvorhabenDomainMapper {

    @Autowired
    private AbfragevarianteRepository abfragevarianteRepository;

    @Mapping(source = "relevanteAbfragevariante.id", target = "relevanteAbfragevariante")
    public abstract BauvorhabenModel entity2Model(final Bauvorhaben bauvorhaben);

    @Mapping(target = "relevanteAbfragevariante", ignore = true)
    public abstract Bauvorhaben model2Entity(final BauvorhabenModel bauvorhabenModel) throws EntityNotFoundException;

    @AfterMapping
    public void afterMappingModel2Entity(final BauvorhabenModel model, @MappingTarget final Bauvorhaben entity)
        throws EntityNotFoundException {
        if (ObjectUtils.isNotEmpty(model.getRelevanteAbfragevariante())) {
            // Es wird nur eine Referenz gesetzt, um nicht den gesamten Objektgraphen der referenzierten Entität zu laden.
            if (!abfragevarianteRepository.existsById(model.getRelevanteAbfragevariante())) {
                final var message = "Abfragevariante nicht gefunden";
                log.error(message);
                throw new EntityNotFoundException(message);
            }
            entity.setRelevanteAbfragevariante(
                abfragevarianteRepository.getReferenceById(model.getRelevanteAbfragevariante())
            );
        }
    }
}
