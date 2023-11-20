/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import java.time.LocalDate;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
public enum SobonOrientierungswertJahr implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED, LocalDate.EPOCH),
    JAHR_2014("2014", LocalDate.of(2014, 1, 1)),
    JAHR_2017("2017", LocalDate.of(2017, 1, 1)),
    JAHR_2022("2022", LocalDate.of(2022, 1, 1));

    @Getter
    private final String bezeichnung;

    @Getter
    private final LocalDate gueltigAb;

    public static Optional<SobonOrientierungswertJahr> findByBezeichnung(final String bezeichnung) {
        return EnumUtils
            .getEnumList(SobonOrientierungswertJahr.class)
            .stream()
            .filter(sobonOrientierungswertJahr ->
                StringUtils.equals(sobonOrientierungswertJahr.getBezeichnung(), bezeichnung)
            )
            .findFirst();
    }
}
