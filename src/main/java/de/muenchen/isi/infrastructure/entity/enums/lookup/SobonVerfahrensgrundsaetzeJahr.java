/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
public enum SobonVerfahrensgrundsaetzeJahr implements ILookup {
    JAHR_1995("1995"),
    JAHR_1997("1997"),
    JAHR_2001("2001"),
    JAHR_2006("2006"),
    JAHR_2012("2012"),
    JAHR_2017("2017"),
    JAHR_2017_PLUS("2017 plus"),
    JAHR_2021("2021"),
    JAHR_2022("2022");

    @Getter
    private final String bezeichnung;

    public static Optional<SobonVerfahrensgrundsaetzeJahr> findByBezeichnung(final String bezeichnung) {
        return EnumUtils
            .getEnumList(SobonVerfahrensgrundsaetzeJahr.class)
            .stream()
            .filter(sobonVerfahrensgrundsaetzeJahr ->
                StringUtils.equals(sobonVerfahrensgrundsaetzeJahr.getBezeichnung(), bezeichnung)
            )
            .findFirst();
    }
}
