package de.muenchen.isi.api.dto;

import de.muenchen.isi.api.dto.common.AdresseDto;
import de.muenchen.isi.api.dto.common.VerortungMultiPolygonDto;
import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Bauratenmethodik;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
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

    private Verfahrensstand verfahrensstand;

    private String verfahrensstandFreieEingabe;

    private AdresseDto adresse;

    private VerortungMultiPolygonDto verortung;

    private List<DokumentDto> dokumente;

    private LocalDate fristBearbeitung;

    private UncertainBoolean mitzeichnungBeschlussentwurf;

    private LocalDate start42Verfahren;

    private Boolean start42VerfahrenDatumUnbekannt;

    private Bauratenmethodik bauratenmethodikVorbelegung;

    private List<AbfragevarianteBauleitplanverfahrenDto> abfragevariantenBauleitplanverfahren;

    private List<AbfragevarianteBauleitplanverfahrenDto> abfragevariantenSachbearbeitungBauleitplanverfahren;
}
