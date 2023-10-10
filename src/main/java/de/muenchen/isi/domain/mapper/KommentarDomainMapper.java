package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.common.KommentarModel;
import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
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
public abstract class KommentarDomainMapper {

    @Autowired
    private BauvorhabenRepository bauvorhabenRepository;

    @Autowired
    private InfrastruktureinrichtungRepository infrastruktureinrichtungRepository;

    @Mapping(source = "bauvorhaben.id", target = "bauvorhaben")
    @Mapping(source = "infrastruktureinrichtung.id", target = "infrastruktureinrichtung")
    public abstract KommentarModel entity2Model(final Kommentar entity);

    @Mapping(target = "bauvorhaben", ignore = true)
    @Mapping(target = "infrastruktureinrichtung", ignore = true)
    public abstract Kommentar model2Entity(final KommentarModel model) throws EntityNotFoundException;

    @AfterMapping
    void afterMappingModel2Entity(final KommentarModel model, final @MappingTarget Kommentar entity)
        throws EntityNotFoundException {
        if (ObjectUtils.isNotEmpty(model.getBauvorhaben())) {
            final var bauvorhaben = bauvorhabenRepository
                .findById(model.getBauvorhaben())
                .orElseThrow(() -> {
                    final var message = "Bauvorhaben für den Kommentar nicht gefunden";
                    log.error(message);
                    return new EntityNotFoundException(message);
                });
            entity.setBauvorhaben(bauvorhaben);
        } else if (ObjectUtils.isNotEmpty(model.getInfrastruktureinrichtung())) {
            final var infastruktureinrichtung = infrastruktureinrichtungRepository
                .findById(model.getInfrastruktureinrichtung())
                .orElseThrow(() -> {
                    final var message = "Infrastruktureinrichtung für den Kommentar nicht gefunden";
                    log.error(message);
                    return new EntityNotFoundException(message);
                });
            entity.setInfrastruktureinrichtung(infastruktureinrichtung);
        } else {
            final var message = "Der Kommentar referenziert weder ein Bauvorhaben noch eine Infrastruktureinrichtung";
            log.error(message);
            throw new EntityNotFoundException(message);
        }
    }
}
