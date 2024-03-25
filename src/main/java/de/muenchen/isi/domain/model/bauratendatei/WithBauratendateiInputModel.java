package de.muenchen.isi.domain.model.bauratendatei;

import java.util.List;
import java.util.UUID;

public interface WithBauratendateiInputModel {
    UUID getId();

    Boolean getHasBauratendateiInputs();

    void setHasBauratendateiInputs(final Boolean value);

    String getAnmerkungBauratendateiInputs();

    void setAnmerkungBauratendateiInputs(final String value);

    BauratendateiInputModel getBauratendateiInputBasis();

    void setBauratendateiInputBasis(final BauratendateiInputModel value);

    List<BauratendateiInputModel> getBauratendateiInputs();

    void setBauratendateiInputs(final List<BauratendateiInputModel> value);
}
