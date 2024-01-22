/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstants {

    public static final String SPRING_UNIT_TEST_PROFILE = "unittest";

    public static final String SPRING_NO_SECURITY_PROFILE = "no-security";

    public static final String FF = "freifinanzierter Geschosswohnungsbau";

    public static final String EOF = "geförderter Mietwohnungsbau";

    public static final String MM = "MünchenModell";

    public static final String FH = "Ein-/Zweifamilienhäuser";

    public static final String PMB = "preis-gedämpfter Mietwohnungsbau";

    public static final String KMB = "konzeptioneller Mietwohnungsbau";

    public static final String BAU = "Baugemeinschaften";
}
