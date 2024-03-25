package de.muenchen.isi.domain.model.bauratendatei;

import java.util.List;
import java.util.UUID;

public interface WithBauratendateiInputsModel {
    UUID getId();

    Boolean getHasBauratendateiInputs();

    void setHasBauratendateiInputs(Boolean value);

    String getAnmerkungBauratendateiInputs();

    void setAnmerkungBauratendateiInputs(String value);

    BauratendateiInputModel getBauratendateiInputBasis();

    void setBauratendateiInputBasis(BauratendateiInputModel value);

    List<BauratendateiInputModel> getBauratendateiInputs();

    void setBauratendateiInputs(List<BauratendateiInputModel> value);
}
