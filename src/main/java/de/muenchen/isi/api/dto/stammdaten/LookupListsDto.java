package de.muenchen.isi.api.dto.stammdaten;

import de.muenchen.isi.domain.model.stammdaten.LookupListModel;
import lombok.Data;

@Data
public class LookupListsDto {

    private LookupListDto uncertainBoolean;

    private LookupListDto artDokument;

    private LookupListDto artAbfrage;

    private LookupListDto sobonVerfahrensgrundsaetzeJahr;

    private LookupListDto standVorhaben;

    private LookupListDto statusAbfrage;

    private LookupListDto planungsrecht;

    private LookupListDto baugebietTyp;

    private LookupListDto zustaendigeDienststelle;

    private LookupListDto statusInfrastruktureinrichtung;

    private LookupListDto einrichtungstraeger;

    private LookupListDto infrastruktureinrichtungTyp;

}
