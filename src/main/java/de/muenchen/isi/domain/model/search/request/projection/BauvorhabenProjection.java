package de.muenchen.isi.domain.model.search.request.projection;

import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import java.math.BigDecimal;

/**
 * Enthält alle Felder die Bauvorhaben hat
 */
public interface BauvorhabenProjection extends BaseProjection, AbfrageBauvorhabenProjection {
    String nameVorhaben();
    BigDecimal grundstuecksgroesse();

    default String getNameVorhaben() {
        return nameVorhaben();
    }

    default BigDecimal getGrundstuecksgroesse() {
        return grundstuecksgroesse();
    }
}
