package de.muenchen.isi.api.dto.bauratendatei;

import java.util.List;

public interface WithBauratendateiInputDto {
    Boolean getHasBauratendateiInputs();

    void setHasBauratendateiInputs(final Boolean value);

    String getAnmerkungBauratendateiInputs();

    void setAnmerkungBauratendateiInputs(final String value);

    BauratendateiInputDto getBauratendateiInputBasis();

    void setBauratendateiInputBasis(final BauratendateiInputDto value);

    List<BauratendateiInputDto> getBauratendateiInputs();

    void setBauratendateiInputs(final List<BauratendateiInputDto> value);
}
