package de.muenchen.isi.domain.model.bauratendatei;

import java.util.List;
import java.util.UUID;

public interface WithBauratendateiInputModel {
    UUID getId();

    Boolean getHasBauratendateiInput();

    void setHasBauratendateiInput(final Boolean value);

    String getAnmerkungBauratendateiInput();

    void setAnmerkungBauratendateiInput(final String value);

    BauratendateiInputModel getBauratendateiInputBasis();

    void setBauratendateiInputBasis(final BauratendateiInputModel value);

    List<BauratendateiInputModel> getBauratendateiInput();

    void setBauratendateiInput(final List<BauratendateiInputModel> value);
}
