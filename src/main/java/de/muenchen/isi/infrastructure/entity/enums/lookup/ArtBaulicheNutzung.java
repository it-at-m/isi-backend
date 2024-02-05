package de.muenchen.isi.infrastructure.entity.enums.lookup;

import de.muenchen.isi.infrastructure.entity.Baugebiet;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

/**
 * In {@link Baugebiet#getArtBaulicheNutzung()} wird der Enum
 * nicht mittels @Enumerated(EnumType.STRING) als String repräsentiert.
 * Somit sind neue Enumausprägungen am Ende der Enumration anzufügen.
 */
@AllArgsConstructor
public enum ArtBaulicheNutzung implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED),
    WR("Reines Wohngebiet (WR)"),
    WA("Allgemeines Wohngebiet (WA)"),
    MU("Urbanes Gebiet (MU)"),
    MK("Kerngebiet (MK)"),
    MI("Mischgebiet (MI)"),
    GE("Gewerbegebiet (GE)"),
    INFO_FEHLT("Info fehlt");

    @Getter
    private final String bezeichnung;

    public static List<ArtBaulicheNutzung> getArtBaulicheNutzungForBauvorhaben() {
        final var withoutUnspecified = EnumUtils.getEnumList(ArtBaulicheNutzung.class);
        withoutUnspecified.remove(ArtBaulicheNutzung.UNSPECIFIED);
        return withoutUnspecified;
    }
}
