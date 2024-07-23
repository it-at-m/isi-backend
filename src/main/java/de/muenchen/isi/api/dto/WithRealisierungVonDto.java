package de.muenchen.isi.api.dto;

import java.util.List;

public interface WithRealisierungVonDto {
    Integer getRealisierungVon();

    void setRealisierungVon(final Integer value);

    List<BauabschnittDto> getBauabschnitte();

    void setBauabschnitte(final List<BauabschnittDto> value);
}
