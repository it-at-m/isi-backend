package de.muenchen.isi.infrastructure.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@AllArgsConstructor
public enum Altersklasse {

    NULL_ZWEI("0-2"),
    DREI_SECHSEINHALB("3-6,5"),
    SECHSEINHALB_NEUNEINHALB("6,5-9,5"),
    ZEHNEINHALB_FUENFZEHN("10,5-15"),
    SECHSZEHN_ACHTZEHN("16-18"),
    ALLE_EWO("alle EWO");

    @Getter
    private final String bezeichnung;

    public static Optional<Altersklasse> findByBezeichnung(final String bezeichnung) {
        return EnumUtils.getEnumList(Altersklasse.class).stream()
                .filter(altersklasse -> StringUtils.equals(altersklasse.getBezeichnung(), bezeichnung))
                .findFirst();
    }

}
