package de.muenchen.isi.infrastructure.entity.enums.lookup;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

@AllArgsConstructor
public enum ArtBaulicheNutzung implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED),
    WR("Reines Wohngebiet (WR)"),
    WA("Allgemeines Wohngebiet (WA)"),
    MU("Urbanes Gebiet (MU)"),
    MK("Kerngebiet (MK)"),
    MI("Mischgebiet (MI)"),
    GE("Gewerbegebiet (GE)"),
    FREIE_EINGABE("freie Eingabe");

    @Getter
    private final String bezeichnung;

    public static List<ArtBaulicheNutzung> getArtBaulicheNutzungForBauvorhaben() {
        final var withoutUnspecified = EnumUtils.getEnumList(ArtBaulicheNutzung.class);
        withoutUnspecified.remove(ArtBaulicheNutzung.UNSPECIFIED);
        return withoutUnspecified;
    }
}
