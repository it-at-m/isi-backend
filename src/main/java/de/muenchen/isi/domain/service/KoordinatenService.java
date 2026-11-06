package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.GeometryOperationFailedException;
import de.muenchen.isi.domain.exception.KoordinatenException;
import de.muenchen.isi.domain.model.common.MultiPolygonGeometryModel;
import de.muenchen.isi.domain.model.common.UtmModel;
import de.muenchen.isi.domain.model.common.Wgs84Model;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.json.JsonMapper;

@Service
@Slf4j
@NoArgsConstructor
public class KoordinatenService {

    public static final CoordinateReferenceSystem WGS84;

    /**
     * ETRS89 / UTM Zone 32N. Die Umrechnung aus WGS84 ist eine Nullverschiebung (EPSG-Transformation 1149).
     */
    public static final CoordinateReferenceSystem UTM32;

    private static final int NUMBER_GEO_JSON_DECIMALS = 25;

    private static final int SRID_WGS84 = 4326;

    private static final String TYPE_MULTI_POLYGON = "MultiPolygon";

    private static final ObjectWriter OBJECT_WRITER = new JsonMapper().writer();

    private static final MathTransform WGS84_TO_UTM32;

    private static final MathTransform UTM32_TO_WGS84;

    static {
        try {
            WGS84 = CRS.decode("EPSG:4326");
            UTM32 = CRS.decode("EPSG:25832");
            WGS84_TO_UTM32 = CRS.findMathTransform(WGS84, UTM32);
            UTM32_TO_WGS84 = CRS.findMathTransform(UTM32, WGS84);
        } catch (FactoryException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Rechnet Wgs84 zu UTM32 um
     *
     * @param wgs84 Koordinate die umgerechnet werden soll
     * @return String in UTM32
     * @throws FactoryException
     * @throws TransformException
     */
    public UtmModel wgs84ToUtm32(final Wgs84Model wgs84) throws KoordinatenException {
        final UtmModel utm = new UtmModel();
        try {
            final Coordinate in = new Coordinate(wgs84.getLatitude(), wgs84.getLongitude());
            final Coordinate out = in;
            final Coordinate result = JTS.transform(in, out, WGS84_TO_UTM32);

            utm.setZone("32U");
            utm.setEast(result.getX());
            utm.setNorth(result.getY());
        } catch (TransformException exception) {
            log.error(exception.getMessage());
            throw new KoordinatenException(
                "Bei der Transformation der Koordinate ist ein Fehler aufgetreten.",
                exception
            );
        }

        log.debug(
            "WGS84 {},{} nach UTM {},{} transformiert.",
            wgs84.getLatitude(),
            wgs84.getLongitude(),
            utm.getEast(),
            utm.getNorth()
        );
        return utm;
    }

    /**
     * Rechnet UTM32 zu Wgs84 um
     *
     * @param utm Koordinate die umgerechnet werden soll
     * @return String in Wgs84
     * @throws FactoryException
     * @throws TransformException
     */
    public Wgs84Model utm32ToWgs84(final UtmModel utm) throws KoordinatenException {
        final Wgs84Model wgs84 = new Wgs84Model();

        try {
            final Coordinate in = new Coordinate(utm.getEast(), utm.getNorth());
            final Coordinate out = in;
            final Coordinate result = JTS.transform(in, out, UTM32_TO_WGS84);

            wgs84.setLatitude(result.getX());
            wgs84.setLongitude(result.getY());
        } catch (TransformException exception) {
            log.error(exception.getMessage());
            throw new KoordinatenException(
                "Bei der Transformation der Koordinate ist ein Fehler aufgetreten.",
                exception
            );
        }

        log.debug(
            "UTM {},{} nach WGS84 {},{} transformiert.",
            utm.getEast(),
            utm.getNorth(),
            wgs84.getLatitude(),
            wgs84.getLongitude()
        );
        return wgs84;
    }

    /**
     * Erstellt ein JTS MultiPolygon-Objekt in WGS84 aus dem GeoJSON-Model.
     *
     * @param multiPolygonGeometry Das Model, das in ein JTS MultiPolygon umgewandelt werden soll.
     * @return Ein JTS MultiPolygon-Objekt mit der SRID 4326.
     * @throws GeometryOperationFailedException Wenn die Geometrieoperation fehlschlägt oder das MultiPolygon nicht verarbeitet werden kann.
     */
    public MultiPolygon createMultiPolygon(final MultiPolygonGeometryModel multiPolygonGeometry)
        throws GeometryOperationFailedException {
        final GeometryJSON jsonGeometry = new GeometryJSON(NUMBER_GEO_JSON_DECIMALS);
        try {
            final String geoJsonMultiPolygon = OBJECT_WRITER.writeValueAsString(multiPolygonGeometry);
            final var multiPolygon = (MultiPolygon) jsonGeometry.read(geoJsonMultiPolygon);
            multiPolygon.setSRID(SRID_WGS84);
            return multiPolygon;
        } catch (final JacksonException | IOException exception) {
            final var message = "Das übergebene Multipolygon konnte nicht verarbeitet werden.";
            log.error(message);
            throw new GeometryOperationFailedException(message, exception);
        }
    }

    /**
     * Wandelt ein JTS MultiPolygon in das GeoJSON-Model um.
     *
     * @param multiPolygon Die Geometrie in WGS84.
     * @return Das Model mit Längengrad und Breitengrad je Koordinate, Löcher und Teilflächen bleiben erhalten.
     */
    public MultiPolygonGeometryModel createMultiPolygonModel(final MultiPolygon multiPolygon) {
        final List<List<List<List<BigDecimal>>>> polygone = new ArrayList<>(multiPolygon.getNumGeometries());
        for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
            final var polygon = (Polygon) multiPolygon.getGeometryN(i);
            final List<List<List<BigDecimal>>> ringe = new ArrayList<>(polygon.getNumInteriorRing() + 1);
            ringe.add(toKoordinaten(polygon.getExteriorRing()));
            for (int j = 0; j < polygon.getNumInteriorRing(); j++) {
                ringe.add(toKoordinaten(polygon.getInteriorRingN(j)));
            }
            polygone.add(ringe);
        }
        final var model = new MultiPolygonGeometryModel();
        model.setType(TYPE_MULTI_POLYGON);
        model.setCoordinates(polygone);
        return model;
    }

    private static List<List<BigDecimal>> toKoordinaten(final LineString ring) {
        final List<List<BigDecimal>> koordinaten = new ArrayList<>(ring.getNumPoints());
        for (final Coordinate koordinate : ring.getCoordinates()) {
            koordinaten.add(List.of(BigDecimal.valueOf(koordinate.getX()), BigDecimal.valueOf(koordinate.getY())));
        }
        return koordinaten;
    }

    /**
     * Ermittelt einen Punkt, der garantiert in der Fläche liegt (wie ST_PointOnSurface). Der geometrische Schwerpunkt
     * läge zum Beispiel bei einer ringförmigen oder mehrteiligen Fläche außerhalb.
     *
     * @param multiPolygonGeometry Die Fläche.
     * @return Die Koordinaten des Punktes.
     * @throws GeometryOperationFailedException Wenn die Geometrieoperation fehlschlägt oder das MultiPolygon nicht verarbeitet werden kann.
     */
    public Wgs84 getMultiPolygonPointOnSurface(final MultiPolygonGeometryModel multiPolygonGeometry)
        throws GeometryOperationFailedException {
        final Point punkt = createMultiPolygon(multiPolygonGeometry).getInteriorPoint();
        final Wgs84 wgs84 = new Wgs84();
        wgs84.setLatitude(punkt.getY());
        wgs84.setLongitude(punkt.getX());
        return wgs84;
    }
}
