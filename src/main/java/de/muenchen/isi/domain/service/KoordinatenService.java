package de.muenchen.isi.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import de.muenchen.isi.api.dto.common.UtmDto;
import de.muenchen.isi.api.dto.common.Wgs84Dto;
import de.muenchen.isi.domain.exception.GeometryOperationFailedException;
import de.muenchen.isi.domain.exception.KoordinatenException;
import de.muenchen.isi.domain.model.common.WGS84Model;
import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
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
     * @param wgs84Dto Koordinate die umgerechnet werden soll
     * @return String in UTM32
     * @throws FactoryException
     * @throws TransformException
     */
    public UtmDto wgs84ToUtm32(final Wgs84Dto wgs84Dto) throws KoordinatenException {
        final UtmDto utmDto = new UtmDto();
        try {
            final Coordinate in = new Coordinate(wgs84Dto.getLatitude(), wgs84Dto.getLongitude());
            final Coordinate out = in;
            final MathTransform transform = CRS.findMathTransform(WGS84, UTM32);
            final Coordinate result = JTS.transform(in, out, transform);

            utmDto.setZone("32U");
            utmDto.setEast(result.getX());
            utmDto.setNorth(result.getY());
        } catch (FactoryException | TransformException exception) {
            log.error(exception.getMessage());
            throw new KoordinatenException(
                "Bei der Transformation der Koordinate ist ein Fehler aufgetreten.",
                exception
            );
        }

        log.debug(
            "WGS84 {},{} nach UTM {},{} transformiert.",
            wgs84Dto.getLatitude(),
            wgs84Dto.getLongitude(),
            utmDto.getEast(),
            utmDto.getNorth()
        );
        return utmDto;
    }

    /**
     * Rechnet UTM32 zu Wgs84 um
     *
     * @param utmDto Koordinate die umgerechnet werden soll
     * @return String in Wgs84
     * @throws FactoryException
     * @throws TransformException
     */
    public Wgs84Dto utm32ToWgs84(final UtmDto utmDto) throws KoordinatenException {
        final Wgs84Dto wgs84Dto = new Wgs84Dto();

        try {
            final Coordinate in = new Coordinate(utmDto.getEast(), utmDto.getNorth());
            final Coordinate out = in;
            final MathTransform transform = CRS.findMathTransform(UTM32, WGS84);
            final Coordinate result = JTS.transform(in, out, transform);

            wgs84Dto.setLatitude(result.getX());
            wgs84Dto.setLongitude(result.getY());
        } catch (FactoryException | TransformException exception) {
            log.error(exception.getMessage());
            throw new KoordinatenException(
                "Bei der Transformation der Koordinate ist ein Fehler aufgetreten.",
                exception
            );
        }

        log.debug(
            "UTM {},{} nach WGS84 {},{} transformiert.",
            utmDto.getEast(),
            utmDto.getNorth(),
            wgs84Dto.getLatitude(),
            wgs84Dto.getLongitude()
        );
        return wgs84Dto;
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
     * als {@link WGS84Model} zurück. Falls die Geometrieoperation fehlschlägt oder das MultiPolygon nicht verarbeitet werden kann,
     * wird eine GeometryOperationFailedException ausgelöst.
     *
     * @param multiPolygonGeometry Das {@link MultiPolygonGeometry}Objekt, dessen Schwerpunkt ermittelt werden soll.
     * @return Ein {@link WGS84Model} Objekt mit den Koordinaten des Schwerpunkts des MultiPolygons.
     * @throws GeometryOperationFailedException Wenn die Geometrieoperation fehlschlägt oder das MultiPolygon nicht verarbeitet werden kann.
     */
    public WGS84Model getMultiPolygonCentroid(final MultiPolygonGeometry multiPolygonGeometry)
        throws GeometryOperationFailedException {
        Point schwerpunkt = createMultiPolygon(multiPolygonGeometry).getCentroid();
        WGS84Model wgs84Model = new WGS84Model();
        wgs84Model.setLatitude(schwerpunkt.getY());
        wgs84Model.setLongitude(schwerpunkt.getX());
        return wgs84Model;
    }
}
