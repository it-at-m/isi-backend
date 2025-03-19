package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.GeometryOperationFailedException;
import de.muenchen.isi.domain.model.common.MultiPolygonGeometryModel;
import de.muenchen.isi.domain.model.common.Wgs84Model;
import de.muenchen.isi.domain.model.enums.SearchResultType;
import de.muenchen.isi.domain.model.search.request.CompositeEntityProjection;
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
import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.enums.EntityType;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
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

    @Autowired
    private KoordinatenDomainMapper koordinatenDomainMapper;

    @Autowired
    private KoordinatenService koordinatenService;

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "coordinate", ignore = true)
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
            @Mapping(target = "umgriff", ignore = true),
        }
    )
    public abstract BauvorhabenSearchResultModel entity2SearchResultModel(final Bauvorhaben entity);

    @AfterMapping
    public void afterMappingEntity2SearchResultModel(
        final Bauvorhaben entity,
        @MappingTarget final BauvorhabenSearchResultModel model
    ) {
        model.setCoordinate(this.getCoordinateFromAdresseOrVerortung(entity.getAdresse(), entity.getVerortung()));
        model.setUmgriff(this.getUmgriffFromVerortung(entity.getVerortung()));
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
    public void afterMappingEntity2SearchResultModel(
        final Bauleitplanverfahren entity,
        @MappingTarget final AbfrageSearchResultModel model
    ) {
        model.setCoordinate(this.getCoordinateFromAdresseOrVerortung(entity.getAdresse(), entity.getVerortung()));
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
    public void afterMappingEntity2SearchResultModel(
        final Baugenehmigungsverfahren entity,
        @MappingTarget final AbfrageSearchResultModel model
    ) {
        model.setCoordinate(this.getCoordinateFromAdresseOrVerortung(entity.getAdresse(), entity.getVerortung()));
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
    public void afterMappingEntity2SearchResultModel(
        final WeiteresVerfahren entity,
        @MappingTarget final AbfrageSearchResultModel model
    ) {
        model.setCoordinate(this.getCoordinateFromAdresseOrVerortung(entity.getAdresse(), entity.getVerortung()));
    }

    @Mappings(
        {
            @Mapping(target = "type", constant = SearchResultType.Values.INFRASTRUKTUREINRICHTUNG),
            @Mapping(target = "coordinate", ignore = true),
            @Mapping(target = "zugehoerigesBauvorhaben", ignore = true),
        }
    )
    public abstract InfrastruktureinrichtungSearchResultModel entity2SearchResultModel(
        final Infrastruktureinrichtung entity
    );

    @AfterMapping
    public void afterMappingEntity2SearchResultModel(
        final Infrastruktureinrichtung entity,
        @MappingTarget final InfrastruktureinrichtungSearchResultModel model
    ) {
        if (entity.getBauvorhaben() != null) {
            model.setZugehoerigesBauvorhaben(entity.getBauvorhaben().getNameVorhaben());
        }
        if (hasAdressCoordinate(entity.getAdresse())) {
            model.setCoordinate(this.koordinatenDomainMapper.entity2Model(entity.getAdresse().getCoordinate()));
        } else if (
            ObjectUtils.isNotEmpty(entity.getVerortung()) && ObjectUtils.isNotEmpty(entity.getVerortung().getPoint())
        ) {
            final var wgs84Model = new Wgs84Model();
            wgs84Model.setLongitude(entity.getVerortung().getPoint().getCoordinates().get(0).doubleValue());
            wgs84Model.setLatitude(entity.getVerortung().getPoint().getCoordinates().get(1).doubleValue());
            model.setCoordinate(wgs84Model);
        } else {
            model.setCoordinate(null);
        }
    }

    /*
     * Dispatcher-Methode: Je nach EntityType der Projection wird das passende Mapping angewendet.
     */
    public SearchResultModel projectionToSearchResultModel(CompositeEntityProjection projection) {
        switch (projection.entityType().name()) {
            case EntityType.Values.GRUNDSCHULE:
                return projectionGrundschuleToInfrastruktureinrichtungSearchResultModel(projection);
            case EntityType.Values.MITTELSCHULE:
                return projectionMittelschuleToInfrastruktureinrichtungSearchResultModel(projection);
            case EntityType.Values.GS_NACHMITTAG_BETREUUNG:
                return projectionGsNachmittagsToInfrastruktureinrichtungSearchResultModel(projection);
            case EntityType.Values.HAUS_FUER_KINDER:
                return projectionHausFuerKinderToInfrastruktureinrichtungSearchResultModel(projection);
            case EntityType.Values.KINDERGARTEN:
                return projectionKindergartenToInfrastruktureinrichtungSearchResultModel(projection);
            case EntityType.Values.KINDERKRIPPE:
                return projectionKinderkrippeToInfrastruktureinrichtungSearchResultModel(projection);
            case EntityType.Values.BAUVORHABEN:
                return projectionToBauvorhabenSearchResultModel(projection);
            case EntityType.Values.BAULEITPLANVERFAHREN:
                return projectionBauleitplanverfahrenToAbfrageSearchResultModel(projection);
            case EntityType.Values.BAUGENEHMIGUNGSVERFAHREN:
                return projectionBaugenehmigungsverfahrenToAbfrageSearchResultModel(projection);
            case EntityType.Values.WEITERES_VERFAHREN:
                return projectionWeiteresVerfahrenToAbfrageSearchResultModel(projection);
            default:
                throw new IllegalArgumentException("Unsupported entity type: " + projection.entityType());
        }
    }

    /**
     * Mapping für Infrastruktureinrichtung KINDERGARTEN aus der Projection.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "infrastruktureinrichtungTyp", constant = InfrastruktureinrichtungTyp.Values.KINDERGARTEN)
    @Mapping(target = "type", constant = SearchResultType.Values.INFRASTRUKTUREINRICHTUNG)
    @Mapping(source = "infrastruktureinrichtungCoordinate", target = "coordinate")
    public abstract InfrastruktureinrichtungSearchResultModel projectionKindergartenToInfrastruktureinrichtungSearchResultModel(
        CompositeEntityProjection projection
    );

    /**
     * Mapping für Infrastruktureinrichtung KINDERKRIPPE aus der Projection.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "infrastruktureinrichtungTyp", constant = InfrastruktureinrichtungTyp.Values.KINDERKRIPPE)
    @Mapping(target = "type", constant = SearchResultType.Values.INFRASTRUKTUREINRICHTUNG)
    @Mapping(source = "infrastruktureinrichtungCoordinate", target = "coordinate")
    public abstract InfrastruktureinrichtungSearchResultModel projectionKinderkrippeToInfrastruktureinrichtungSearchResultModel(
        CompositeEntityProjection projection
    );

    /**
     * Mapping für Infrastruktureinrichtung GS_NACHMITTAG_BETREUUNG aus der Projection.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(
        target = "infrastruktureinrichtungTyp",
        constant = InfrastruktureinrichtungTyp.Values.GS_NACHMITTAG_BETREUUNG
    )
    @Mapping(target = "type", constant = SearchResultType.Values.INFRASTRUKTUREINRICHTUNG)
    @Mapping(target = "nameEinrichtung", source = "nameEinrichtung")
    @Mapping(source = "infrastruktureinrichtungCoordinate", target = "coordinate")
    public abstract InfrastruktureinrichtungSearchResultModel projectionGsNachmittagsToInfrastruktureinrichtungSearchResultModel(
        CompositeEntityProjection projection
    );

    /**
     * Mapping für Infrastruktureinrichtung GRUNDSCHULE aus der Projection.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "infrastruktureinrichtungTyp", constant = InfrastruktureinrichtungTyp.Values.GRUNDSCHULE)
    @Mapping(target = "type", constant = SearchResultType.Values.INFRASTRUKTUREINRICHTUNG)
    @Mapping(target = "nameEinrichtung", source = "nameEinrichtung")
    @Mapping(source = "infrastruktureinrichtungCoordinate", target = "coordinate")
    public abstract InfrastruktureinrichtungSearchResultModel projectionGrundschuleToInfrastruktureinrichtungSearchResultModel(
        CompositeEntityProjection projection
    );

    /**
     * Mapping für Infrastruktureinrichtung MITTELSCHULE aus der Projection.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "infrastruktureinrichtungTyp", constant = InfrastruktureinrichtungTyp.Values.MITTELSCHULE)
    @Mapping(target = "type", constant = SearchResultType.Values.INFRASTRUKTUREINRICHTUNG)
    @Mapping(target = "nameEinrichtung", source = "nameEinrichtung")
    @Mapping(source = "infrastruktureinrichtungCoordinate", target = "coordinate")
    public abstract InfrastruktureinrichtungSearchResultModel projectionMittelschuleToInfrastruktureinrichtungSearchResultModel(
        CompositeEntityProjection projection
    );

    /**
     * Mapping für Infrastruktureinrichtung HAUS_FUER_KINDER aus der Projection.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "infrastruktureinrichtungTyp", constant = InfrastruktureinrichtungTyp.Values.HAUS_FUER_KINDER)
    @Mapping(target = "type", constant = SearchResultType.Values.INFRASTRUKTUREINRICHTUNG)
    @Mapping(target = "nameEinrichtung", source = "nameEinrichtung")
    @Mapping(source = "infrastruktureinrichtungCoordinate", target = "coordinate")
    public abstract InfrastruktureinrichtungSearchResultModel projectionHausFuerKinderToInfrastruktureinrichtungSearchResultModel(
        CompositeEntityProjection projection
    );

    /**
     * Mapping für Bauvorhaben aus der Projection.
     */
    @Mapping(target = "type", constant = SearchResultType.Values.BAUVORHABEN)
    @Mapping(source = "stand_verfahren_filter", target = "standVerfahren")
    @Mapping(source = "bauvorhabenCoordinate", target = "coordinate")
    @Mapping(source = "verortung.stadtbezirke", target = "stadtbezirke")
    public abstract BauvorhabenSearchResultModel projectionToBauvorhabenSearchResultModel(
        CompositeEntityProjection projection
    );

    /**
     * Mapping für Abfrage aus der Projection.
     */
    @Mapping(target = "type", constant = SearchResultType.Values.ABFRAGE)
    @Mapping(target = "artAbfrage", constant = ArtAbfrage.Values.BAULEITPLANVERFAHREN)
    @Mapping(source = "abfrageCoordinate", target = "coordinate")
    @Mapping(source = "verortung.stadtbezirke", target = "stadtbezirke")
    @Mapping(source = "stand_verfahren_filter", target = "standVerfahren")
    @Mapping(source = "bauvorhabenId", target = "bauvorhaben")
    public abstract AbfrageSearchResultModel projectionBauleitplanverfahrenToAbfrageSearchResultModel(
        CompositeEntityProjection projection
    );

    /**
     * Mapping für Abfrage aus der Projection.
     */
    @Mapping(target = "type", constant = SearchResultType.Values.ABFRAGE)
    @Mapping(target = "artAbfrage", constant = ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN)
    @Mapping(source = "abfrageCoordinate", target = "coordinate")
    @Mapping(source = "verortung.stadtbezirke", target = "stadtbezirke")
    @Mapping(source = "stand_verfahren_filter", target = "standVerfahren")
    @Mapping(source = "bauvorhabenId", target = "bauvorhaben")
    public abstract AbfrageSearchResultModel projectionBaugenehmigungsverfahrenToAbfrageSearchResultModel(
        CompositeEntityProjection projection
    );

    /**
     * Mapping für Abfrage aus der Projection.
     */
    @Mapping(target = "type", constant = SearchResultType.Values.ABFRAGE)
    @Mapping(target = "artAbfrage", constant = ArtAbfrage.Values.WEITERES_VERFAHREN)
    @Mapping(source = "abfrageCoordinate", target = "coordinate")
    @Mapping(source = "verortung.stadtbezirke", target = "stadtbezirke")
    @Mapping(source = "stand_verfahren_filter", target = "standVerfahren")
    @Mapping(source = "bauvorhabenId", target = "bauvorhaben")
    public abstract AbfrageSearchResultModel projectionWeiteresVerfahrenToAbfrageSearchResultModel(
        CompositeEntityProjection projection
    );

    public UUID map(final Bauvorhaben bauvorhaben) {
        return ObjectUtils.isEmpty(bauvorhaben) ? null : bauvorhaben.getId();
    }

    /**
     * Überprüft, ob die übergebene Adresse eine Koordinate hat.
     *
     * @param adresse Die Adresse, deren Koordinate überprüft werden soll.
     * @return {@code true}, wenn die Adresse eine Koordinate hat, ansonsten {@code false}.
     */
    public boolean hasAdressCoordinate(final Adresse adresse) {
        return ObjectUtils.isNotEmpty(adresse) && ObjectUtils.isNotEmpty(adresse.getCoordinate());
    }

    /**
     * Überprüft, ob die übergebene Verortung ein Mehrfachpolygon mit Koordinaten hat.
     *
     * @param verortung Die Verortung, deren Mehrfachpolygon-Koordinaten überprüft werden sollen.
     * @return {@code true}, wenn die Verortung Koordinaten hat, ansonsten {@code false}.
     */
    public boolean hasVerortungCoordinate(final VerortungMultiPolygon verortung) {
        return ObjectUtils.isNotEmpty(verortung) && ObjectUtils.isNotEmpty(verortung.getMultiPolygon());
    }

    /**
     * Gibt die Koordinaten einer Adresse oder Verortung zurück. Wenn die Adresse Koordinaten hat,
     * wird die Koordinate mithilfe des {@link KoordinatenDomainMapper} extrahiert. Wenn die Verortung Koordinaten hat,
     * wird der Schwerpunkt des Mehrfachpolygons mithilfe des {@link KoordinatenService} ermittelt.
     *
     * @param adresse               Die Adresse, deren Koordinate zurückgegeben werden soll.
     * @param verortungMultiPolygon Die Verortung, deren Koordinate zurückgegeben werden soll.
     * @return Ein WGS84Model-Objekt mit den extrahierten Koordinaten oder {@code null}, wenn keine Koordinaten vorhanden sind.
     */
    public Wgs84Model getCoordinateFromAdresseOrVerortung(
        final Adresse adresse,
        final VerortungMultiPolygon verortungMultiPolygon
    ) {
        if (hasAdressCoordinate(adresse)) {
            return this.koordinatenDomainMapper.entity2Model(adresse.getCoordinate());
        } else if (hasVerortungCoordinate(verortungMultiPolygon)) {
            try {
                final var centroid = koordinatenService.getMultiPolygonCentroid(
                    verortungMultiPolygon.getMultiPolygon()
                );
                return koordinatenDomainMapper.entity2Model(centroid);
            } catch (GeometryOperationFailedException exception) {
                var message = "Ermitteln des Schwerpunktes ist fehlgeschlagen.";
                log.error(message);
            }
        }
        return null;
    }

    /**
     * Gibt den Umgriff der im Parameter gegebenen Verortung zurück.
     *
     * @param verortungMultiPolygon als Umgriff der Verortung.
     * @return den Umgriff als {@link VerortungMultiPolygon#getMultiPolygon()} oder null falls kein Umgriff existiert bzw. die Verortung im Parameter null ist.
     */
    public MultiPolygonGeometryModel getUmgriffFromVerortung(final VerortungMultiPolygon verortungMultiPolygon) {
        return ObjectUtils.isNotEmpty(verortungMultiPolygon)
            ? this.entity2Model(verortungMultiPolygon.getMultiPolygon())
            : null;
    }

    public abstract MultiPolygonGeometryModel entity2Model(final MultiPolygonGeometry entity);
}
