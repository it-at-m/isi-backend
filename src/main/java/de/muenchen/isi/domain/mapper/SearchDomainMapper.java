package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.GeometryOperationFailedException;
import de.muenchen.isi.domain.model.common.WGS84Model;
import de.muenchen.isi.domain.model.enums.SearchResultType;
import de.muenchen.isi.domain.model.search.response.AbfrageSearchResultModel;
import de.muenchen.isi.domain.model.search.response.BauvorhabenSearchResultModel;
import de.muenchen.isi.domain.model.search.response.InfrastruktureinrichtungSearchResultModel;
import de.muenchen.isi.domain.model.search.response.SearchResultModel;
import de.muenchen.isi.domain.service.KoordinatenService;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Baugenehmigungsverfahren;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.WeiteresVerfahren;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.SubclassMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Mapper(
    config = MapstructConfiguration.class,
    uses = {
        AbfrageDomainMapper.class,
        InfrastruktureinrichtungDomainMapper.class,
        BauvorhabenDomainMapper.class,
        KoordinatenDomainMapper.class,
    }
)
public abstract class SearchDomainMapper {

    private KoordinatenDomainMapper koordinatenDomainMapper = new KoordinatenDomainMapperImpl();

    @Autowired
    private KoordinatenService koordinatenService;

    @Mapping(target = "type", ignore = true)
    @SubclassMapping(source = Infrastruktureinrichtung.class, target = InfrastruktureinrichtungSearchResultModel.class)
    @SubclassMapping(source = Bauvorhaben.class, target = BauvorhabenSearchResultModel.class)
    @SubclassMapping(source = Bauleitplanverfahren.class, target = AbfrageSearchResultModel.class)
    @SubclassMapping(source = Baugenehmigungsverfahren.class, target = AbfrageSearchResultModel.class)
    @SubclassMapping(source = WeiteresVerfahren.class, target = AbfrageSearchResultModel.class)
    public abstract SearchResultModel entity2SearchResultModel(final BaseEntity entity);

    @Mappings(
        {
            @Mapping(target = "type", constant = SearchResultType.Values.BAUVORHABEN),
            @Mapping(source = "verortung.stadtbezirke", target = "stadtbezirke"),
            @Mapping(target = "coordinate", ignore = true),
        }
    )
    public abstract BauvorhabenSearchResultModel entity2SearchResultModel(final Bauvorhaben entity);

    @AfterMapping
    public void afterEntity2SearchResultModel(
        final Bauvorhaben entity,
        @MappingTarget final BauvorhabenSearchResultModel model
    ) {
        if (hasAdressCoordinate(entity.getAdresse())) {
            model.setCoordinate(koordinatenDomainMapper.entity2Model(entity.getAdresse().getCoordinate()));
        } else if (hasVerortungCoordinate(entity.getVerortung())) {
            try {
                model.setCoordinate(
                    koordinatenService.getMultiPolygonCentroid(entity.getVerortung().getMultiPolygon())
                );
            } catch (GeometryOperationFailedException exception) {
                var message = "Ermitteln des Schwerpunktes ist fehlgeschlagen.";
                log.error(message);
            }
        } else {
            model.setCoordinate(null);
        }
    }

    @Mappings(
        {
            @Mapping(target = "type", constant = SearchResultType.Values.ABFRAGE),
            @Mapping(source = "verortung.stadtbezirke", target = "stadtbezirke"),
            @Mapping(target = "coordinate", ignore = true),
        }
    )
    public abstract AbfrageSearchResultModel entity2SearchResultModel(final Bauleitplanverfahren entity);

    @AfterMapping
    public void afterEntity2SearchResultModel(
        final Bauleitplanverfahren entity,
        @MappingTarget final AbfrageSearchResultModel model
    ) {
        if (hasAdressCoordinate(entity.getAdresse())) {
            model.setCoordinate(koordinatenDomainMapper.entity2Model(entity.getAdresse().getCoordinate()));
        } else if (hasVerortungCoordinate(entity.getVerortung())) {
            try {
                model.setCoordinate(
                    koordinatenService.getMultiPolygonCentroid(entity.getVerortung().getMultiPolygon())
                );
            } catch (GeometryOperationFailedException exception) {
                var message = "Ermitteln des Schwerpunktes ist fehlgeschlagen.";
                log.error(message);
            }
        } else {
            model.setCoordinate(null);
        }
    }

    @Mappings(
        {
            @Mapping(target = "type", constant = SearchResultType.Values.ABFRAGE),
            @Mapping(source = "verortung.stadtbezirke", target = "stadtbezirke"),
            @Mapping(target = "coordinate", ignore = true),
        }
    )
    public abstract AbfrageSearchResultModel entity2SearchResultModel(final Baugenehmigungsverfahren entity);

    @AfterMapping
    public void afterEntity2SearchResultModel(
        final Baugenehmigungsverfahren entity,
        @MappingTarget final AbfrageSearchResultModel model
    ) {
        if (hasAdressCoordinate(entity.getAdresse())) {
            model.setCoordinate(koordinatenDomainMapper.entity2Model(entity.getAdresse().getCoordinate()));
        } else if (hasVerortungCoordinate(entity.getVerortung())) {
            try {
                model.setCoordinate(
                    koordinatenService.getMultiPolygonCentroid(entity.getVerortung().getMultiPolygon())
                );
            } catch (GeometryOperationFailedException exception) {
                var message = "Ermitteln des Schwerpunktes ist fehlgeschlagen.";
                log.error(message);
            }
        } else {
            model.setCoordinate(null);
        }
    }

    @Mappings(
        {
            @Mapping(target = "type", constant = SearchResultType.Values.ABFRAGE),
            @Mapping(source = "verortung.stadtbezirke", target = "stadtbezirke"),
            @Mapping(target = "coordinate", ignore = true),
        }
    )
    public abstract AbfrageSearchResultModel entity2SearchResultModel(final WeiteresVerfahren entity);

    @AfterMapping
    public void afterEntity2SearchResultModel(
        final WeiteresVerfahren entity,
        @MappingTarget final AbfrageSearchResultModel model
    ) {
        if (hasAdressCoordinate(entity.getAdresse())) {
            model.setCoordinate(koordinatenDomainMapper.entity2Model(entity.getAdresse().getCoordinate()));
        } else if (hasVerortungCoordinate(entity.getVerortung())) {
            try {
                model.setCoordinate(
                    koordinatenService.getMultiPolygonCentroid(entity.getVerortung().getMultiPolygon())
                );
            } catch (GeometryOperationFailedException exception) {
                var message = "Ermitteln des Schwerpunktes ist fehlgeschlagen.";
                log.error(message);
            }
        } else {
            model.setCoordinate(null);
        }
    }

    @Mappings(
        {
            @Mapping(target = "type", constant = SearchResultType.Values.INFRASTRUKTUREINRICHTUNG),
            @Mapping(target = "coordinate", ignore = true),
        }
    )
    public abstract InfrastruktureinrichtungSearchResultModel entity2SearchResultModel(
        final Infrastruktureinrichtung entity
    );

    @AfterMapping
    public void afterEntity2SearchResultModel(
        final Infrastruktureinrichtung entity,
        @MappingTarget final InfrastruktureinrichtungSearchResultModel model
    ) {
        if (hasAdressCoordinate(entity.getAdresse())) {
            model.setCoordinate(koordinatenDomainMapper.entity2Model(entity.getAdresse().getCoordinate()));
        } else if (ObjectUtils.isNotEmpty(entity.getVerortung())) {
            if (ObjectUtils.isNotEmpty(entity.getVerortung().getPoint())) {
                WGS84Model wgs84Model = new WGS84Model();
                wgs84Model.setLongitude(entity.getVerortung().getPoint().getCoordinates().get(0).doubleValue());
                wgs84Model.setLatitude(entity.getVerortung().getPoint().getCoordinates().get(1).doubleValue());
                model.setCoordinate(wgs84Model);
            }
        } else {
            model.setCoordinate(null);
        }
    }

    public UUID map(final Bauvorhaben bauvorhaben) {
        return ObjectUtils.isEmpty(bauvorhaben) ? null : bauvorhaben.getId();
    }

    public boolean hasAdressCoordinate(final Adresse adresse) {
        if (ObjectUtils.isNotEmpty(adresse)) {
            if (ObjectUtils.isNotEmpty(adresse.getCoordinate())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasVerortungCoordinate(final VerortungMultiPolygon verortung) {
        if (ObjectUtils.isNotEmpty(verortung)) {
            if (ObjectUtils.isNotEmpty(verortung.getMultiPolygon())) {
                return true;
            }
        }
        return false;
    }
}
