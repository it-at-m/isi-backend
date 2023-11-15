package de.muenchen.isi.api.dto;

import de.muenchen.isi.api.dto.common.AdresseDto;
import de.muenchen.isi.api.dto.common.VerortungDto;
import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauleitplanverfahrenDto extends AbfrageDto {

    private String bebauungsplannummer;

    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    private StandVerfahren standVerfahren;

    private String standVerfahrenFreieEingabe;

    private AdresseDto adresse;

    private VerortungDto verortung;

    private List<DokumentDto> dokumente;

    private LocalDate fristBearbeitung;

    private UncertainBoolean offizielleMitzeichnung;

    private List<AbfragevarianteBauleitplanverfahrenDto> abfragevariantenBauleitplanverfahren;

    private List<AbfragevarianteBauleitplanverfahrenDto> abfragevariantenSachbearbeitungBauleitplanverfahren;
}
