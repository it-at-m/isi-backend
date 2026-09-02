package de.muenchen.isi.api.dto.search.filter;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterSettingsDto {

    private List<String> stadtbezirkNummer;

    private List<String> kitaplanungsbereichKitaPlbT;

    private List<Long> grundschulsprengelNummer;

    private List<Long> mittelschulsprengelNummer;

    private Integer realisierungsbeginnVon;

    private Integer realisierungsbeginnBis;

    private Boolean nurEigeneAbfragen;

    private List<StatusAbfrage> statusAbfrage;

    private UncertainBoolean sobonRelevant;

    private Integer weGesamtVon;

    private Integer weGesamtBis;

    private BigDecimal gfWohnenGeplantVon;

    private BigDecimal gfWohnenGeplantBis;

    private List<Verfahrensstand> verfahrensstand;

    private List<StatusInfrastruktureinrichtung> infrastruktureinrichtungStatus;
}
