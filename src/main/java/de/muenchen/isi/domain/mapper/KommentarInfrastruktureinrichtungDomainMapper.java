package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.common.KommentarInfrastruktureinrichtungModel;
import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import de.muenchen.isi.infrastructure.repository.InfrastruktureinrichtungRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapstructConfiguration.class)
@Slf4j
public abstract class KommentarInfrastruktureinrichtungDomainMapper {

    @Autowired
    private InfrastruktureinrichtungRepository infrastruktureinrichtungRepository;

    @Mapping(source = "infrastruktureinrichtung.id", target = "infrastruktureinrichtung")
    public abstract KommentarInfrastruktureinrichtungModel entity2Model(final Kommentar entity);

    @Mapping(target = "bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung", ignore = true)
    public abstract Kommentar model2Entity(final KommentarInfrastruktureinrichtungModel model)
        throws EntityNotFoundException;

    @AfterMapping
    void afterMappingModel2Entity(
        final KommentarInfrastruktureinrichtungModel model,
        @MappingTarget final Kommentar entity
    ) throws EntityNotFoundException {
        if (ObjectUtils.isNotEmpty(model.getInfrastruktureinrichtung())) {
            final var infastruktureinrichtung = infrastruktureinrichtungRepository
                .findById(model.getInfrastruktureinrichtung())
                .orElseThrow(() -> {
                    final var message = "Infrastruktureinrichtung für den Kommentar nicht gefunden";
                    log.error(message);
                    return new EntityNotFoundException(message);
                });
            entity.setInfrastruktureinrichtung(infastruktureinrichtung);
        }
    }
}
