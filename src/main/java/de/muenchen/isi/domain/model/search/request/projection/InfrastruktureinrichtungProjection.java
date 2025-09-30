package de.muenchen.isi.domain.model.search.request.projection;

import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;

/**
 * Enthält alle Felder die Infrastruktureinrichtung hat
 */
public interface InfrastruktureinrichtungProjection extends BaseProjection {
    VerortungPoint verortungPoint();
    InfrastruktureinrichtungTyp infrastruktureinrichtungTyp();
    String nameEinrichtung();
    StatusInfrastruktureinrichtung status();
    String bauvorhabenName();

    default VerortungPoint getVerortungPoint() {
        return verortungPoint();
    }

    default InfrastruktureinrichtungTyp getInfrastruktureinrichtungTyp() {
        return infrastruktureinrichtungTyp();
    }

    default String getNameEinrichtung() {
        return nameEinrichtung();
    }

    default StatusInfrastruktureinrichtung getStatus() {
        return status();
    }

    default String getBauvorhabenName() {
        return bauvorhabenName();
    }
}
