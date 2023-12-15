package de.muenchen.isi.domain.service;

import de.muenchen.isi.api.dto.common.UtmDto;
import de.muenchen.isi.api.dto.common.Wgs84Dto;
import de.muenchen.isi.domain.exception.KoordinatenException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
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
}
