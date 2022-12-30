package de.muenchen.isi.infrastructure.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@AllArgsConstructor
public enum Wohnungstyp {

    GW_FREIFINANZEIRT("GW-freifinanziert"),
    GW_GEFOERDERT("GW-gefördert"),
    MM_EIGENTUM("MM-Eigentum"),
    MM_MIETE("MM-Miete"),
    EINS_ZWEI_FH("1-2-FH");

    @Getter
    private final String bezeichnung;

    public static Optional<Wohnungstyp> findByBezeichnung(final String bezeichnung) {
        return EnumUtils.getEnumList(Wohnungstyp.class).stream()
                .filter(wohnungstyp -> StringUtils.equals(wohnungstyp.getBezeichnung(), bezeichnung))
                .findFirst();
    }

}
