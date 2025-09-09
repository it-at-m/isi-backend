package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.GeometryOperationFailedException;
import de.muenchen.isi.domain.model.common.MultiPolygonGeometryModel;
import de.muenchen.isi.domain.model.common.Wgs84Model;
import de.muenchen.isi.domain.model.enums.SearchResultType;
import de.muenchen.isi.domain.model.search.request.AbfrageInfrastruktureinrichtungRecord;
import de.muenchen.isi.domain.model.search.request.AbfrageRecord;
import de.muenchen.isi.domain.model.search.request.AllObjectsRecord;
import de.muenchen.isi.domain.model.search.request.BauvorhabenAbfrageRecord;
import de.muenchen.isi.domain.model.search.request.BauvorhabenInfrastruktureinrichtungRecord;
import de.muenchen.isi.domain.model.search.request.BauvorhabenRecord;
import de.muenchen.isi.domain.model.search.request.InfrastrukturRecord;
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
import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
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
            case AllObjectsRecord allObjectsRecord ->
                mapAllObjectsRecordToSearchResultModel(allObjectsRecord);
            case AbfrageRecord abfrageRecord ->
                getAbfrageSearchResultModel(abfrageRecord);
            case InfrastrukturRecord infrastrukturRecord ->
                getInfrastruktureinrichtungSearchResultModel(infrastrukturRecord);
            case BauvorhabenRecord bauvorhabenRecord ->
                getBauvorhabenSearchResultModel(bauvorhabenRecord);
            case BauvorhabenInfrastruktureinrichtungRecord bauvorhabenInfrastruktureinrichtungRecord ->
                mapBauvorhabenInfrastruktureinrichtungRecordToSearchResultModel(bauvorhabenInfrastruktureinrichtungRecord);
            case BauvorhabenAbfrageRecord bauvorhabenAbfrageRecord ->
                mapBauvorhabenAbfrageRecordToSearchResultModel(bauvorhabenAbfrageRecord);
            case AbfrageInfrastruktureinrichtungRecord abfrageInfrastruktureinrichtungRecord ->
                mapAbfrageInfrastruktureinrichtungRecordToSearchResultModel(abfrageInfrastruktureinrichtungRecord);
            default -> throw new IllegalArgumentException(
                "Projection type: " + projection.getClass().getName() + " nicht implementiert"
            );
    };
}
    
    /**
     * Mappt einen {@link AllObjectsRecord} abhängig vom enthaltenen {@code resultType}
     * auf das passende {@link SearchResultModel}.
     *
     * @param projection Der gemischte Record mit {@code resultType}.
     * @return Das spezifische {@link SearchResultModel}.
     * @throws IllegalArgumentException wenn der {@code resultType} unbekannt ist.
     */
    private SearchResultModel mapAllObjectsRecordToSearchResultModel(AllObjectsRecord projection) {
        switch (projection.resultType()) {
            case "BAUVORHABEN":
                return getBauvorhabenSearchResultModel(projection);
            case "ABFRAGE":
                return getAbfrageSearchResultModel(projection);
            case "INFRASTRUKTUREINRICHTUNG":
                return getInfrastruktureinrichtungSearchResultModel(projection);
            default:
                throw new IllegalArgumentException("Projection type: " + projection.getClass().getName() + " nicht implementiert");
        }
    }
    
    /**
     * Mappt einen {@link BauvorhabenInfrastruktureinrichtungRecord} abhängig vom
     * {@code resultType} auf das passende {@link SearchResultModel}.
     *
     * @param projection Record aus einer kombinierten Suche Bauvorhaben/Einrichtung.
     * @return Das spezifische {@link SearchResultModel}.
     * @throws IllegalArgumentException bei unbekanntem {@code resultType}.
     */
    private SearchResultModel mapBauvorhabenInfrastruktureinrichtungRecordToSearchResultModel(
        BauvorhabenInfrastruktureinrichtungRecord projection
    ) {
        switch (projection.resultType()) {
            case "BAUVORHABEN":
                return getBauvorhabenSearchResultModel(projection);
            case "INFRASTRUKTUREINRICHTUNG":
                return getInfrastruktureinrichtungSearchResultModel(projection);
            default:
                throw new IllegalArgumentException("Projection type: " + projection.getClass().getName() + " nicht implementiert");
        }
    }
    
    /**
     * Mappt einen {@link BauvorhabenAbfrageRecord} abhängig vom {@code resultType}
     * auf das passende {@link SearchResultModel}.
     *
     * @param projection Record aus einer kombinierten Suche Bauvorhaben/Abfrage.
     * @return Das spezifische {@link SearchResultModel}.
     * @throws IllegalArgumentException bei unbekanntem {@code resultType}.
     */
    private SearchResultModel mapBauvorhabenAbfrageRecordToSearchResultModel(BauvorhabenAbfrageRecord projection) {
        switch (projection.resultType()) {
            case "BAUVORHABEN":
                return getBauvorhabenSearchResultModel(projection);
            case "ABFRAGE":
                return getAbfrageSearchResultModel(projection);
            default:
                throw new IllegalArgumentException("Projection type: " + projection.getClass().getName() + " nicht implementiert");
        }
    }
    
    /**
     * Mappt einen {@link AbfrageInfrastruktureinrichtungRecord} abhängig vom
     * {@code resultType} auf das passende {@link SearchResultModel}.
     *
     * @param projection Record aus einer kombinierten Suche Abfrage/Einrichtung.
     * @return Das spezifische {@link SearchResultModel}.
     * @throws IllegalArgumentException bei unbekanntem {@code resultType}.
     */
    private SearchResultModel mapAbfrageInfrastruktureinrichtungRecordToSearchResultModel(
        AbfrageInfrastruktureinrichtungRecord projection
    ) {
        switch (projection.resultType()) {
            case "ABFRAGE":
                return getAbfrageSearchResultModel(projection);
            case "INFRASTRUKTUREINRICHTUNG":
                return getInfrastruktureinrichtungSearchResultModel(projection);
            default:
                throw new IllegalArgumentException("Projection type: " + projection.getClass().getName() + " nicht implementiert");
        }
    }
    
    /**
     * Erstellt ein {@link AbfrageSearchResultModel} aus einem {@link AllObjectsRecord}.
     * <p>
     * Bevorzugt werden vorhandene Adresskoordinaten; andernfalls wird ggf. der Schwerpunkt
     * einer Verortung verwendet (siehe {@code getCoordinateFromAdresseOrVerortung}).
     * </p>
     *
     * @param projection Record mit Abfrage-bezogenen Feldern.
     * @return Gefülltes {@link AbfrageSearchResultModel}.
     */
    private AbfrageSearchResultModel getAbfrageSearchResultModel(AllObjectsRecord projection) {
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
        model.setCoordinate(getCoordinateFromAdresseOrVerortung(projection.adresseJson(), projection.verortungJson()));
        return model;
    }
    
    /**
     * Erstellt ein {@link AbfrageSearchResultModel} aus einem {@link AbfrageRecord}.
     *
     * @param projection Abfrage-Record der gefilterten Ergebnismenge.
     * @return Gefülltes {@link AbfrageSearchResultModel}.
     */
    private AbfrageSearchResultModel getAbfrageSearchResultModel(AbfrageRecord projection) {
        AbfrageSearchResultModel model = new AbfrageSearchResultModel();
        model.setType(SearchResultType.ABFRAGE);
        model.setArtAbfrage(projection.artAbfrage());
        model.setId(projection.id());
        model.setName(projection.name());
        model.setStatusAbfrage(projection.statusAbfrage());
        model.setFristBearbeitung(projection.fristBearbeitung());
        model.setCreatedDateTime(projection.createdDateTime());
        model.setBauvorhaben(projection.bauvorhabenId());
        model.setCoordinate(getCoordinateFromAdresseOrVerortung(projection.adresseJson(), projection.verortungJson()));
        return model;
    }
    
    /**
     * Erstellt ein {@link AbfrageSearchResultModel} aus einem {@link BauvorhabenAbfrageRecord}.
     *
     * @param projection Kombinierter Record aus Bauvorhaben ↔ Abfrage.
     * @return Gefülltes {@link AbfrageSearchResultModel}.
     */
    private AbfrageSearchResultModel getAbfrageSearchResultModel(BauvorhabenAbfrageRecord projection) {
        AbfrageSearchResultModel model = new AbfrageSearchResultModel();
        model.setType(SearchResultType.ABFRAGE);
        model.setArtAbfrage(projection.artAbfrage());
        model.setId(projection.id());
        model.setName(projection.name());
        model.setStatusAbfrage(projection.statusAbfrage());
        model.setFristBearbeitung(projection.fristBearbeitung());
        model.setCreatedDateTime(projection.createdDateTime());
        model.setBauvorhaben(projection.bauvorhabenId());
        model.setCoordinate(getCoordinateFromAdresseOrVerortung(projection.adresseJson(), projection.verortungJson()));
        return model;
    }
    
    /**
     * Erstellt ein {@link AbfrageSearchResultModel} aus einem
     * {@link AbfrageInfrastruktureinrichtungRecord}.
     *
     * @param projection Kombinierter Record aus Abfrage ↔ Infrastruktureinrichtung.
     * @return Gefülltes {@link AbfrageSearchResultModel}.
     */
    private AbfrageSearchResultModel getAbfrageSearchResultModel(AbfrageInfrastruktureinrichtungRecord projection) {
        AbfrageSearchResultModel model = new AbfrageSearchResultModel();
        model.setType(SearchResultType.ABFRAGE);
        model.setArtAbfrage(projection.artAbfrage());
        model.setId(projection.id());
        model.setName(projection.name());
        model.setStatusAbfrage(projection.statusAbfrage());
        model.setFristBearbeitung(projection.fristBearbeitung());
        model.setCreatedDateTime(projection.createdDateTime());
        model.setBauvorhaben(projection.bauvorhabenId());
        model.setCoordinate(getCoordinateFromAdresseOrVerortung(projection.adresseJson(), projection.verortungJson()));
        return model;
    }
    
    /**
     * Erstellt ein {@link InfrastruktureinrichtungSearchResultModel} aus einem
     * {@link AllObjectsRecord}. Koordinaten werden aus Adresse, Verortungspunkt oder als
     * {@code null} gesetzt.
     *
     * @param projection Gemischter Record mit Einrichtungsdaten.
     * @return Gefülltes {@link InfrastruktureinrichtungSearchResultModel}.
     */
    private InfrastruktureinrichtungSearchResultModel getInfrastruktureinrichtungSearchResultModel(
        AllObjectsRecord projection
    ) {
        InfrastruktureinrichtungSearchResultModel model = new InfrastruktureinrichtungSearchResultModel();
        model.setType(SearchResultType.INFRASTRUKTUREINRICHTUNG);
        model.setId(projection.id());
        model.setInfrastruktureinrichtungTyp(projection.infrastruktureinrichtungTyp());
        model.setNameEinrichtung(projection.nameEinrichtung());
        model.setZugehoerigesBauvorhaben(projection.bauvorhabenName());
        if (hasAdressCoordinate(projection.adresseJson())) {
            model.setCoordinate(koordinatenDomainMapper.entity2Model(projection.adresseJson().getCoordinate()));
        } else if (ObjectUtils.isNotEmpty(projection.verortungPointJson())) {
            VerortungPoint verortungPoint = projection.verortungPointJson();
            final var wgs84Model = new Wgs84Model();
            wgs84Model.setLongitude(verortungPoint.getPoint().getCoordinates().get(0).doubleValue());
            wgs84Model.setLatitude(verortungPoint.getPoint().getCoordinates().get(1).doubleValue());
            model.setCoordinate(wgs84Model);
        } else {
            model.setCoordinate(null);
        }
        return model;
    }
    
    /**
     * Erstellt ein {@link InfrastruktureinrichtungSearchResultModel} aus einem
     * {@link InfrastrukturRecord}.
     *
     * @param projection Einrichtungs-Record der gefilterten Ergebnismenge.
     * @return Gefülltes {@link InfrastruktureinrichtungSearchResultModel}.
     */
    private InfrastruktureinrichtungSearchResultModel getInfrastruktureinrichtungSearchResultModel(
        InfrastrukturRecord projection
    ) {
        InfrastruktureinrichtungSearchResultModel model = new InfrastruktureinrichtungSearchResultModel();
        model.setType(SearchResultType.INFRASTRUKTUREINRICHTUNG);
        model.setId(projection.id());
        model.setInfrastruktureinrichtungTyp(projection.infrastruktureinrichtungTyp());
        model.setNameEinrichtung(projection.nameEinrichtung());
        model.setZugehoerigesBauvorhaben(projection.bauvorhabenName());
        if (hasAdressCoordinate(projection.adresseJson())) {
            model.setCoordinate(koordinatenDomainMapper.entity2Model(projection.adresseJson().getCoordinate()));
        } else if (ObjectUtils.isNotEmpty(projection.verortungPointJson())) {
            VerortungPoint verortungPoint = projection.verortungPointJson();
            final var wgs84Model = new Wgs84Model();
            wgs84Model.setLongitude(verortungPoint.getPoint().getCoordinates().get(0).doubleValue());
            wgs84Model.setLatitude(verortungPoint.getPoint().getCoordinates().get(1).doubleValue());
            model.setCoordinate(wgs84Model);
        } else {
            model.setCoordinate(null);
        }
        return model;
    }
    
    /**
     * Erstellt ein {@link InfrastruktureinrichtungSearchResultModel} aus einem
     * {@link BauvorhabenInfrastruktureinrichtungRecord}.
     *
     * @param projection Kombinierter Record aus Bauvorhaben ↔ Infrastruktureinrichtung.
     * @return Gefülltes {@link InfrastruktureinrichtungSearchResultModel}.
     */
    private InfrastruktureinrichtungSearchResultModel getInfrastruktureinrichtungSearchResultModel(
        BauvorhabenInfrastruktureinrichtungRecord projection
    ) {
        InfrastruktureinrichtungSearchResultModel model = new InfrastruktureinrichtungSearchResultModel();
        model.setType(SearchResultType.INFRASTRUKTUREINRICHTUNG);
        model.setId(projection.id());
        model.setInfrastruktureinrichtungTyp(projection.infrastruktureinrichtungTyp());
        model.setNameEinrichtung(projection.nameEinrichtung());
        model.setZugehoerigesBauvorhaben(projection.bauvorhabenName());
        if (hasAdressCoordinate(projection.adresseJson())) {
            model.setCoordinate(koordinatenDomainMapper.entity2Model(projection.adresseJson().getCoordinate()));
        } else if (ObjectUtils.isNotEmpty(projection.verortungPointJson())) {
            VerortungPoint verortungPoint = projection.verortungPointJson();
            final var wgs84Model = new Wgs84Model();
            wgs84Model.setLongitude(verortungPoint.getPoint().getCoordinates().get(0).doubleValue());
            wgs84Model.setLatitude(verortungPoint.getPoint().getCoordinates().get(1).doubleValue());
            model.setCoordinate(wgs84Model);
        } else {
            model.setCoordinate(null);
        }
        return model;
    }
    
    /**
     * Erstellt ein {@link InfrastruktureinrichtungSearchResultModel} aus einem
     * {@link AbfrageInfrastruktureinrichtungRecord}.
     *
     * @param projection Kombinierter Record aus Abfrage ↔ Infrastruktureinrichtung.
     * @return Gefülltes {@link InfrastruktureinrichtungSearchResultModel}.
     */
    private InfrastruktureinrichtungSearchResultModel getInfrastruktureinrichtungSearchResultModel(
        AbfrageInfrastruktureinrichtungRecord projection
    ) {
        InfrastruktureinrichtungSearchResultModel model = new InfrastruktureinrichtungSearchResultModel();
        model.setType(SearchResultType.INFRASTRUKTUREINRICHTUNG);
        model.setId(projection.id());
        model.setInfrastruktureinrichtungTyp(projection.infrastruktureinrichtungTyp());
        model.setNameEinrichtung(projection.nameEinrichtung());
        model.setZugehoerigesBauvorhaben(projection.bauvorhabenName());
        if (hasAdressCoordinate(projection.adresseJson())) {
            model.setCoordinate(koordinatenDomainMapper.entity2Model(projection.adresseJson().getCoordinate()));
        } else if (ObjectUtils.isNotEmpty(projection.verortungPointJson())) {
            VerortungPoint verortungPoint = projection.verortungPointJson();
            final var wgs84Model = new Wgs84Model();
            wgs84Model.setLongitude(verortungPoint.getPoint().getCoordinates().get(0).doubleValue());
            wgs84Model.setLatitude(verortungPoint.getPoint().getCoordinates().get(1).doubleValue());
            model.setCoordinate(wgs84Model);
        } else {
            model.setCoordinate(null);
        }
        return model;
    }
    
    /**
     * Erstellt ein {@link BauvorhabenSearchResultModel} aus einem {@link AllObjectsRecord}.
     * <p>
     * Koordinaten werden (abhängig von Filtern und Datenlage) aus Adresse oder Verortung
     * ermittelt. Weitere Metadaten (z. B. Umgriff, Verfahrensstand) werden direkt übertragen.
     * </p>
     *
     * @param projection Gemischter Record mit Bauvorhaben-Daten.
     * @return Gefülltes {@link BauvorhabenSearchResultModel}.
     */
    private BauvorhabenSearchResultModel getBauvorhabenSearchResultModel(AllObjectsRecord projection) {
        BauvorhabenSearchResultModel model = new BauvorhabenSearchResultModel();
        model.setType(SearchResultType.BAUVORHABEN);
        model.setId(projection.id());
        model.setNameVorhaben(projection.nameVorhaben());
        model.setGrundstuecksgroesse(projection.grundstuecksgroesse());
        model.setUmgriff(entity2Model(projection.umgriff()));
        model.setStandVerfahren(projection.stand_verfahren_filter());
        model.setCoordinate(getCoordinateFromAdresseOrVerortung(projection.adresseJson(), projection.verortungJson()));
        return model;
    }
    
    /**
     * Erstellt ein {@link BauvorhabenSearchResultModel} aus einem {@link BauvorhabenRecord}.
     *
     * @param projection Bauvorhaben-Record der gefilterten Ergebnismenge.
     * @return Gefülltes {@link BauvorhabenSearchResultModel}.
     */
    private BauvorhabenSearchResultModel getBauvorhabenSearchResultModel(BauvorhabenRecord projection) {
        BauvorhabenSearchResultModel model = new BauvorhabenSearchResultModel();
        model.setType(SearchResultType.BAUVORHABEN);
        model.setId(projection.id());
        model.setNameVorhaben(projection.nameVorhaben());
        model.setGrundstuecksgroesse(projection.grundstuecksgroesse());
        model.setUmgriff(entity2Model(projection.umgriff()));
        model.setStandVerfahren(projection.stand_verfahren_filter());
        model.setCoordinate(getCoordinateFromAdresseOrVerortung(projection.adresseJson(), projection.verortungJson()));
        return model;
    }
    
    /**
     * Erstellt ein {@link BauvorhabenSearchResultModel} aus einem
     * {@link BauvorhabenInfrastruktureinrichtungRecord}.
     *
     * @param projection Kombinierter Record aus Bauvorhaben ↔ Infrastruktureinrichtung.
     * @return Gefülltes {@link BauvorhabenSearchResultModel}.
     */
    private BauvorhabenSearchResultModel getBauvorhabenSearchResultModel(
        BauvorhabenInfrastruktureinrichtungRecord projection
    ) {
        BauvorhabenSearchResultModel model = new BauvorhabenSearchResultModel();
        model.setType(SearchResultType.BAUVORHABEN);
        model.setId(projection.id());
        model.setNameVorhaben(projection.nameVorhaben());
        model.setGrundstuecksgroesse(projection.grundstuecksgroesse());
        model.setUmgriff(entity2Model(projection.umgriff()));
        model.setStandVerfahren(projection.stand_verfahren_filter());
        model.setCoordinate(getCoordinateFromAdresseOrVerortung(projection.adresseJson(), projection.verortungJson()));
        return model;
    }
    
    /**
     * Erstellt ein {@link BauvorhabenSearchResultModel} aus einem {@link BauvorhabenAbfrageRecord}.
     *
     * @param projection Kombinierter Record aus Bauvorhaben ↔ Abfrage.
     * @return Gefülltes {@link BauvorhabenSearchResultModel}.
     */
    private BauvorhabenSearchResultModel getBauvorhabenSearchResultModel(BauvorhabenAbfrageRecord projection) {
        BauvorhabenSearchResultModel model = new BauvorhabenSearchResultModel();
        model.setType(SearchResultType.BAUVORHABEN);
        model.setId(projection.id());
        model.setNameVorhaben(projection.nameVorhaben());
        model.setGrundstuecksgroesse(projection.grundstuecksgroesse());
        model.setUmgriff(entity2Model(projection.umgriff()));
        model.setStandVerfahren(projection.stand_verfahren_filter());
        model.setCoordinate(getCoordinateFromAdresseOrVerortung(projection.adresseJson(), projection.verortungJson()));
        return model;
    }
}
    