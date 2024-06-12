package de.muenchen.isi.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import de.muenchen.isi.domain.exception.GeometryOperationFailedException;
import de.muenchen.isi.domain.exception.KoordinatenException;
import de.muenchen.isi.domain.model.common.UtmModel;
import de.muenchen.isi.domain.model.common.Wgs84Model;
import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import java.io.IOException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@NoArgsConstructor
public class KoordinatenService {

    public static final CoordinateReferenceSystem WGS84;
    public static final CoordinateReferenceSystem UTM32;

    private static final int NUMBER_GEO_JSON_DECIMALS = 25;

    static {
        try {
            WGS84 = CRS.decode("EPSG:4326");
            UTM32 = CRS.decode("EPSG:32632");
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
            final MathTransform transform = CRS.findMathTransform(WGS84, UTM32);
            final Coordinate result = JTS.transform(in, out, transform);

            utm.setZone("32U");
            utm.setEast(result.getX());
            utm.setNorth(result.getY());
        } catch (FactoryException | TransformException exception) {
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
            final MathTransform transform = CRS.findMathTransform(UTM32, WGS84);
            final Coordinate result = JTS.transform(in, out, transform);

            wgs84.setLatitude(result.getX());
            wgs84.setLongitude(result.getY());
        } catch (FactoryException | TransformException exception) {
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
     * Erstellt ein JTS MultiPolygon-Objekt aus einem {@link MultiPolygonGeometry} Objekt. Die Methode verwendet
     * GeoJSON zur Konvertierung.
     *
     * @param multiPolygonGeometry Das {@link MultiPolygonGeometry} Objekt, das in ein JTS MultiPolygon umgewandelt werden soll.
     * @return Ein JTS MultiPolygon-Objekt, das aus dem MultiPolygonGeometry erstellt wurde.
     * @throws GeometryOperationFailedException Wenn die Geometrieoperation fehlschlägt oder das MultiPolygon nicht verarbeitet werden kann.
     */
    public MultiPolygon createMultiPolygon(final MultiPolygonGeometry multiPolygonGeometry)
        throws GeometryOperationFailedException {
        final GeometryJSON jsonGeometry = new GeometryJSON(NUMBER_GEO_JSON_DECIMALS);
        final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            final String geoJsonMultiPolygon = objectWriter.writeValueAsString(multiPolygonGeometry);
            return (MultiPolygon) jsonGeometry.read(geoJsonMultiPolygon);
        } catch (final IOException exception) {
            final var message = "Das übergebene Multipolygon konnte nicht verarbeitet werden.";
            log.error(message);
            throw new GeometryOperationFailedException(message, exception);
        }
    }

    /**
     * Ermittelt den Schwerpunkt (Centroid) eines {@link MultiPolygonGeometry} Objekts und gibt die Koordinaten
     * als {@link Wgs84} zurück. Falls die Geometrieoperation fehlschlägt oder das MultiPolygon nicht verarbeitet werden kann,
     * wird eine GeometryOperationFailedException ausgelöst.
     *
     * @param multiPolygonGeometry Das {@link MultiPolygonGeometry}Objekt, dessen Schwerpunkt ermittelt werden soll.
     * @return Ein {@link Wgs84} Objekt mit den Koordinaten des Schwerpunkts des MultiPolygons.
     * @throws GeometryOperationFailedException Wenn die Geometrieoperation fehlschlägt oder das MultiPolygon nicht verarbeitet werden kann.
     */
    public Wgs84 getMultiPolygonCentroid(final MultiPolygonGeometry multiPolygonGeometry)
        throws GeometryOperationFailedException {
        Point schwerpunkt = createMultiPolygon(multiPolygonGeometry).getCentroid();
        Wgs84 wgs84 = new Wgs84();
        wgs84.setLatitude(schwerpunkt.getY());
        wgs84.setLongitude(schwerpunkt.getX());
        return wgs84;
    }
}
