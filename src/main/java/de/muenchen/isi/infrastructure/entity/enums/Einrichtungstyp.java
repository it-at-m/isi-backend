package de.muenchen.isi.infrastructure.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@AllArgsConstructor
public enum Einrichtungstyp {

    KINDERKRIPPE("Kinderkrippe"),
    KINDERGARTEN("Kindergarten"),
    KINDERHORT("Kinderhort"),
    GRUNDSCHULE("Grundschule"),
    N_N("N.N.");

    @Getter
    private final String bezeichnung;

    public static Optional<Einrichtungstyp> findByBezeichnung(final String bezeichnung) {
        return EnumUtils.getEnumList(Einrichtungstyp.class).stream()
                .filter(einrichtungstyp -> StringUtils.equals(einrichtungstyp.getBezeichnung(), bezeichnung))
                .findFirst();
    }

}
