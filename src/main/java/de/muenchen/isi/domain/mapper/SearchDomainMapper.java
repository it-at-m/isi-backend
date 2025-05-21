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

    public SearchResultModel mapProjectionToSearchResultModel(CompositeEntityProjection projection) {
        if ("BAUVORHABEN".equals(projection.resultType())) {
            BauvorhabenSearchResultModel model = new BauvorhabenSearchResultModel();
            model.setType(SearchResultType.BAUVORHABEN);
            model.setId(projection.id());
            model.setNameVorhaben(projection.nameVorhaben());
            model.setGrundstuecksgroesse(projection.grundstuecksgroesse());
            model.setUmgriff(entity2Model(projection.umgriff()));
            model.setStandVerfahren(projection.stand_verfahren_filter());
            return model;
        } else if ("ABFRAGE".equals(projection.resultType())) {
            AbfrageSearchResultModel model = new AbfrageSearchResultModel();
            model.setType(SearchResultType.ABFRAGE);
            model.setArtAbfrage(projection.artAbfrage());
            model.setId(projection.id());
            model.setName(projection.name());
            model.setStatusAbfrage(projection.statusAbfrage());
            model.setFristBearbeitung(projection.fristBearbeitung());
            model.setCreatedDateTime(projection.createdDateTime());
            model.setStandVerfahren(projection.stand_verfahren_filter());
            model.setBauvorhaben(projection.bauvorhabenId());
            return model;
        } else if ("INFRASTRUKTUREINRICHTUNG".equals(projection.resultType())) {
            InfrastruktureinrichtungSearchResultModel model = new InfrastruktureinrichtungSearchResultModel();
            model.setType(SearchResultType.INFRASTRUKTUREINRICHTUNG);
            model.setId(projection.id());
            model.setInfrastruktureinrichtungTyp(projection.infrastruktureinrichtungTyp());
            model.setNameEinrichtung(projection.nameEinrichtung());
            model.setZugehoerigesBauvorhaben(projection.bauvorhabenName());
            return model;
        }
        throw new IllegalArgumentException("Unbekannter resultType: " + projection.resultType());
    }

    private ArtAbfrage findOutArtAbfrage(String projectionString) {
        if ("BAULEITPLANVERFAHREN".equals(projectionString)) {
            return ArtAbfrage.BAULEITPLANVERFAHREN;
        }
        if ("BAUGENEHMIGUNGSVERFAHREN".equals(projectionString)) {
            return ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN;
        }
        if ("WEITERES_VERFAHREN".equals(projectionString)) {
            return ArtAbfrage.WEITERES_VERFAHREN;
        }
        return ArtAbfrage.UNSPECIFIED;
    }

    private InfrastruktureinrichtungTyp findOutInfrastruktureinrichtung(String projectionString) {
        switch (projectionString) {
            case "KINDERKRIPPE":
                return InfrastruktureinrichtungTyp.KINDERKRIPPE;
            case "KINDERGARTEN":
                return InfrastruktureinrichtungTyp.KINDERGARTEN;
            case "GS_NACHMITTAG_BETREUUNG":
                return InfrastruktureinrichtungTyp.GS_NACHMITTAG_BETREUUNG;
            case "HAUS_FUER_KINDER":
                return InfrastruktureinrichtungTyp.HAUS_FUER_KINDER;
            case "GRUNDSCHULE":
                return InfrastruktureinrichtungTyp.GRUNDSCHULE;
            case "MITTELSCHULE":
                return InfrastruktureinrichtungTyp.MITTELSCHULE;
            default:
                return InfrastruktureinrichtungTyp.UNSPECIFIED;
        }
    }
}
