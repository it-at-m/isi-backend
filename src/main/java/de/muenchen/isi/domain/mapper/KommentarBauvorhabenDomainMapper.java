package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.common.KommentarBauvorhabenModel;
import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapstructConfiguration.class)
@Slf4j
public abstract class KommentarBauvorhabenDomainMapper {

    @Autowired
    private BauvorhabenRepository bauvorhabenRepository;

    @Mapping(source = "bauvorhaben.id", target = "bauvorhaben")
    public abstract KommentarBauvorhabenModel entity2Model(final Kommentar entity);

    @Mapping(target = "bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung", ignore = true)
    public abstract Kommentar model2Entity(final KommentarBauvorhabenModel model) throws EntityNotFoundException;

    @AfterMapping
    void afterMappingModel2Entity(final KommentarBauvorhabenModel model, @MappingTarget final Kommentar entity)
        throws EntityNotFoundException {
        if (ObjectUtils.isNotEmpty(model.getBauvorhaben())) {
            final var bauvorhaben = bauvorhabenRepository.findById(model.getBauvorhaben()).orElseThrow(() -> {
                final var message = "Bauvorhaben für den Kommentar nicht gefunden";
                log.error(message);
                return new EntityNotFoundException(message);
            });
            entity.setBauvorhaben(bauvorhaben);
        }
    }
}
