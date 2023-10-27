package de.muenchen.isi.api.dto;

import de.muenchen.isi.api.dto.common.AdresseDto;
import de.muenchen.isi.api.dto.common.VerortungDto;
import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaugenehmigungsverfahrenDto extends AbfrageDto {

    private String aktenzeichenProLbk;

    private String bebauungsplannummer;

    private StandVerfahren standVerfahren;

    private String standVerfahrenFreieEingabe;

    private AdresseDto adresse;

    private VerortungDto verortung;

    private List<DokumentDto> dokumente;

    private LocalDate fristBearbeitung;

    private List<AbfragevarianteBaugenehmigungsverfahrenDto> abfragevarianten;

    private List<AbfragevarianteBaugenehmigungsverfahrenDto> abfragevariantenSachbearbeitung;
}
