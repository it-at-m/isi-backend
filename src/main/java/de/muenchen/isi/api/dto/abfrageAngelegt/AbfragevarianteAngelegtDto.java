package de.muenchen.isi.api.dto.abfrageAngelegt;

import de.muenchen.isi.api.dto.BauabschnittDto;
import java.util.List;

public interface AbfragevarianteAngelegtDto {
    Integer getRealisierungVon();

    List<BauabschnittDto> getBauabschnitte();
}
