package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.GeometryOperationFailedException;
import de.muenchen.isi.domain.model.common.MultiPolygonGeometryModel;
import de.muenchen.isi.domain.model.common.StadtbezirkModel;
import de.muenchen.isi.domain.model.common.Wgs84Model;
import de.muenchen.isi.domain.model.enums.SearchResultType;
import de.muenchen.isi.domain.model.search.request.AbfrageInfrastruktureinrichtungRecord;
import de.muenchen.isi.domain.model.search.request.AllObjectsRecord;
import de.muenchen.isi.domain.model.search.request.BauvorhabenAbfrageRecord;
import de.muenchen.isi.domain.model.search.request.BauvorhabenInfrastruktureinrichtungRecord;
import de.muenchen.isi.domain.model.search.request.projection.AbfrageProjection;
import de.muenchen.isi.domain.model.search.request.projection.BauvorhabenProjection;
import de.muenchen.isi.domain.model.search.request.projection.InfrastruktureinrichtungProjection;
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
import de.muenchen.isi.infrastructure.entity.common.Stadtbezirk;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ResultType;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import java.util.HashSet;
import java.util.Set;
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
            model.setCoordinate(koordinatenDomainMapper.entity2Model(entity.getAdresse().getCoordinate()));
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
     * Ermittelt die Koordinaten einer Adresse oder – falls diese nicht vorhanden sind –
     * den Schwerpunkt (Centroid) einer übergebenen Verortung (Mehrfachpolygon).
     *
     * <p>
     * Die Methode geht wie folgt vor:
     * <ul>
     *   <li>Falls die Adresse gültige Koordinaten enthält, werden diese zurückgegeben.</li>
     *   <li>Falls die Adresse keine Koordinaten hat, aber die Verortung ein Mehrfachpolygon
     *       mit Koordinaten enthält, wird der geometrische Schwerpunkt des Polygons berechnet
     *       und zurückgegeben.</li>
     *   <li>Falls keine Koordinaten ermittelt werden können oder die Schwerpunktberechnung
     *       fehlschlägt, wird {@code null} zurückgegeben.</li>
     * </ul>
     * </p>
     *
     * @param adresse Die Adresse, deren Koordinaten bevorzugt zurückgegeben werden sollen.
     * @param verortungMultiPolygon Die Verortung, deren Polygon-Schwerpunkt verwendet wird,
     *                              falls die Adresse keine Koordinaten hat.
     * @return Ein {@link Wgs84Model} mit den ermittelten Koordinaten oder {@code null},
     *         wenn keine Koordinaten bestimmt werden können.
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

    public abstract StadtbezirkModel entity2Model(final Stadtbezirk entity);

    /**
     * Mappt ein Projektion-Objekt (Record) auf ein {@link SearchResultModel}.
     * <p>
     * Abhängig von den gesetzten Filtern in der Suche werden nur bestimmte Objekttypen
     * (z. B. Bauvorhaben, Abfrage, Infrastruktureinrichtung) zurückgeliefert. Diese
     * Methode erkennt zur Laufzeit den konkreten Record-Typ und delegiert an die
     * passende Mapping-Methode.
     * </p>
     *
     * @param projection Das Projektion-Objekt (Record) eines unterstützten Typs.
     * @return Ein {@link SearchResultModel} für den erkannten Typ.
     * @throws IllegalArgumentException wenn der Record-Typ nicht unterstützt ist.
     */
    public SearchResultModel mapProjectionRecordToSearchResultModel(Object projection) {
        return switch (projection) {
            case AllObjectsRecord allObjectsRecord -> switch (allObjectsRecord.resultType()) {
                case ResultType.BAUVORHABEN -> toBauvorhabenModel(allObjectsRecord);
                case ResultType.ABFRAGE -> toAbfrageModel(allObjectsRecord);
                case ResultType.INFRASTRUKTUREINRICHTUNG -> toInfrastrukturModel(allObjectsRecord);
                default -> throw new IllegalArgumentException(
                    "Unbekannter resultType in AllObjectsRecord: " + allObjectsRecord.resultType()
                );
            };
            case AbfrageInfrastruktureinrichtungRecord abfrageInfrastruktureinrichtungRecord -> switch (
                abfrageInfrastruktureinrichtungRecord.resultType()
            ) {
                case ResultType.ABFRAGE -> toAbfrageModel(abfrageInfrastruktureinrichtungRecord);
                case ResultType.INFRASTRUKTUREINRICHTUNG -> toInfrastrukturModel(abfrageInfrastruktureinrichtungRecord);
                default -> throw new IllegalArgumentException(
                    "Unbekannter resultType in AbfrageInfrastruktureinrichtungRecord: " +
                    abfrageInfrastruktureinrichtungRecord.resultType()
                );
            };
            case BauvorhabenInfrastruktureinrichtungRecord bauvorhabenInfrastruktureinrichtungRecord -> switch (
                bauvorhabenInfrastruktureinrichtungRecord.resultType()
            ) {
                case ResultType.BAUVORHABEN -> toBauvorhabenModel(bauvorhabenInfrastruktureinrichtungRecord);
                case ResultType.INFRASTRUKTUREINRICHTUNG -> toInfrastrukturModel(
                    bauvorhabenInfrastruktureinrichtungRecord
                );
                default -> throw new IllegalArgumentException(
                    "Unbekannter resultType in BauvorhabenInfrastruktureinrichtungRecord: " +
                    bauvorhabenInfrastruktureinrichtungRecord.resultType()
                );
            };
            case BauvorhabenAbfrageRecord bauvorhabenAbfrageRecord -> switch (bauvorhabenAbfrageRecord.resultType()) {
                case ResultType.BAUVORHABEN -> toBauvorhabenModel(bauvorhabenAbfrageRecord);
                case ResultType.ABFRAGE -> toAbfrageModel(bauvorhabenAbfrageRecord);
                default -> throw new IllegalArgumentException(
                    "Unbekannter resultType in BauvorhabenInfrastruktureinrichtungRecord: " +
                    bauvorhabenAbfrageRecord.resultType()
                );
            };
            case AbfrageProjection abfrageProjection -> toAbfrageModel(abfrageProjection);
            case BauvorhabenProjection bauvorhabenProjection -> toBauvorhabenModel(bauvorhabenProjection);
            case InfrastruktureinrichtungProjection infrastruktureinrichtungProjection -> toInfrastrukturModel(
                infrastruktureinrichtungProjection
            );
            default -> throw new IllegalArgumentException(
                "Projection type: " + projection.getClass().getName() + " nicht unterstützt"
            );
        };
    }

    @Mappings(
        {
            @Mapping(target = "type", constant = SearchResultType.Values.ABFRAGE),
            @Mapping(target = "coordinate", ignore = true),
            @Mapping(target = "stadtbezirke", ignore = true),
            @Mapping(target = "bauvorhaben", source = "bauvorhabenId"),
        }
    )
    public abstract AbfrageSearchResultModel toAbfrageModel(AbfrageProjection projection);

    @AfterMapping
    protected void setAbfrageCoordinate(AbfrageProjection projection, @MappingTarget AbfrageSearchResultModel model) {
        model.setCoordinate(getCoordinateFromAdresseOrVerortung(projection.getAdresse(), projection.getVerortung()));
        if (projection.getVerortung() != null) {
            Set<StadtbezirkModel> stadtbezirke = new HashSet<>();
            projection
                .getVerortung()
                .getStadtbezirke()
                .forEach(stadtbezirk -> {
                    stadtbezirke.add(entity2Model(stadtbezirk));
                });
            model.setStadtbezirke(stadtbezirke);
        }
    }

    // -------- BAUVORHABEN --------
    @Mappings(
        {
            @Mapping(target = "type", constant = SearchResultType.Values.BAUVORHABEN),
            @Mapping(target = "coordinate", ignore = true),
            @Mapping(target = "stadtbezirke", ignore = true),
            @Mapping(target = "umgriff", ignore = true),
        }
    )
    public abstract BauvorhabenSearchResultModel toBauvorhabenModel(BauvorhabenProjection projection);

    @AfterMapping
    protected void setBauvorhabenExtras(
        BauvorhabenProjection projection,
        @MappingTarget BauvorhabenSearchResultModel model
    ) {
        model.setCoordinate(getCoordinateFromAdresseOrVerortung(projection.getAdresse(), projection.getVerortung()));
        if (projection.getUmgriff() != null) {
            model.setUmgriff(entity2Model(projection.getUmgriff()));
        }
        if (projection.getVerortung() != null) {
            Set<StadtbezirkModel> stadtbezirke = new HashSet<>();
            projection
                .getVerortung()
                .getStadtbezirke()
                .forEach(stadtbezirk -> {
                    stadtbezirke.add(entity2Model(stadtbezirk));
                });
            model.setStadtbezirke(stadtbezirke);
        }
    }

    // -------- INFRASTRUKTUR --------
    @Mappings(
        {
            @Mapping(target = "type", constant = SearchResultType.Values.INFRASTRUKTUREINRICHTUNG),
            @Mapping(target = "coordinate", ignore = true),
            @Mapping(target = "zugehoerigesBauvorhaben", source = "bauvorhabenName"),
        }
    )
    public abstract InfrastruktureinrichtungSearchResultModel toInfrastrukturModel(
        InfrastruktureinrichtungProjection projection
    );

    @AfterMapping
    protected void setInfrastrukturCoordinate(
        InfrastruktureinrichtungProjection projection,
        @MappingTarget InfrastruktureinrichtungSearchResultModel model
    ) {
        if (hasAdressCoordinate(projection.getAdresse())) {
            model.setCoordinate(koordinatenDomainMapper.entity2Model(projection.getAdresse().getCoordinate()));
        } else if (ObjectUtils.isNotEmpty(projection.getVerortungPoint())) {
            VerortungPoint verortungPoint = projection.getVerortungPoint();
            Wgs84Model wgs84 = new Wgs84Model();
            wgs84.setLongitude(verortungPoint.getPoint().getCoordinates().get(0).doubleValue());
            wgs84.setLatitude(verortungPoint.getPoint().getCoordinates().get(1).doubleValue());
            model.setCoordinate(wgs84);
        } else {
            model.setCoordinate(null);
        }
    }
}
