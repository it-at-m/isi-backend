package de.muenchen.isi.domain.model.search.request.projection;

import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;

/**
 * Enthält alle Felder die Abfrage und Bauvorhaben haben
 */
public interface AbfrageBauvorhabenProjection {
    VerortungMultiPolygon verortung();
    StandVerfahren standVerfahren();

    default StandVerfahren getStandVerfahren() {
        return standVerfahren();
    }

    default VerortungMultiPolygon getVerortung() {
        return verortung();
    }
}
