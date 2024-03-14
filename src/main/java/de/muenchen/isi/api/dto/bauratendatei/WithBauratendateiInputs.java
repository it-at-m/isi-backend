package de.muenchen.isi.api.dto.bauratendatei;

import java.util.List;

public interface WithBauratendateiInputs {
    Boolean getHasBauratendateiInputs();

    void setHasBauratendateiInputs(Boolean value);

    String getAnmerkungBauratendateiInputs();

    void setAnmerkungBauratendateiInputs(String value);

    BauratendateiInputDto getBauratendateiInputBasis();

    void setBauratendateiInputBasis(BauratendateiInputDto value);

    List<BauratendateiInputDto> getBauratendateiInputs();

    void setBauratendateiInputs(List<BauratendateiInputDto> value);
}
