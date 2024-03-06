package de.muenchen.isi.api.dto;

import de.muenchen.isi.reporting.client.model.WohneinheitenProFoerderartProJahrDto;
import java.util.List;
import lombok.Data;

@Data
public class BauratendateiInputDto {

    private String grundschulsprengel;

    private String mittelschulsprengel;

    private String viertel;

    private List<WohneinheitenProFoerderartProJahrDto> wohneinheiten;
}
