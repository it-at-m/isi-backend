/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@AllArgsConstructor
public enum SobonVerfahrensgrundsaetzeJahr implements ILookup {

    DAVOR("vor 2014"),
    JAHR_2014("2014"),
    JAHR_2017("2017"),
    JAHR_2017_PLUS("2017 plus"),
    JAHR_2021("2021");

    @Getter
    private final String bezeichnung;

    public static Optional<SobonVerfahrensgrundsaetzeJahr> findByBezeichnung(final String bezeichnung) {
        return EnumUtils.getEnumList(SobonVerfahrensgrundsaetzeJahr.class).stream()
                .filter(sobonVerfahrensgrundsaetzeJahr -> StringUtils.equals(sobonVerfahrensgrundsaetzeJahr.getBezeichnung(), bezeichnung))
                .findFirst();
    }

}
