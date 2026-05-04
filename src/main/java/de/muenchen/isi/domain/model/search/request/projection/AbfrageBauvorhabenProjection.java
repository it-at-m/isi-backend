package de.muenchen.isi.domain.model.search.request.projection;

import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;

/**
 * Enthält alle Felder die Abfrage und Bauvorhaben haben
 */
public interface AbfrageBauvorhabenProjection {
    VerortungMultiPolygon verortung();
    Verfahrensstand verfahrensstand();

    default Verfahrensstand getVerfahrensstand() {
        return verfahrensstand();
    }

    default VerortungMultiPolygon getVerortung() {
        return verortung();
    }
}
