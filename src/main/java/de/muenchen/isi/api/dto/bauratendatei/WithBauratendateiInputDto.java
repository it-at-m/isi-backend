package de.muenchen.isi.api.dto.bauratendatei;

import java.util.List;

public interface WithBauratendateiInputDto {
    Boolean getHasBauratendateiInput();

    void setHasBauratendateiInput(final Boolean value);

    String getAnmerkungBauratendateiInput();

    void setAnmerkungBauratendateiInput(final String value);

    BauratendateiInputDto getBauratendateiInputBasis();

    void setBauratendateiInputBasis(final BauratendateiInputDto value);

    List<BauratendateiInputDto> getBauratendateiInput();

    void setBauratendateiInput(final List<BauratendateiInputDto> value);
}
