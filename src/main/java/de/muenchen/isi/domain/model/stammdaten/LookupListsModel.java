package de.muenchen.isi.domain.model.stammdaten;

import lombok.Data;

@Data
public class LookupListsModel {

    private LookupListModel uncertainBoolean;

    private LookupListModel artDokument;

    private LookupListModel artAbfrage;

    private LookupListModel sobonVerfahrensgrundsaetzeJahr;

    private LookupListModel standVorhaben;

    private LookupListModel statusAbfrage;

    private LookupListModel planungsrecht;

    private LookupListModel baugebietTyp;

    private LookupListModel statusInfrastruktureinrichtung;

    private LookupListModel einrichtungstraeger;

    private LookupListModel infrastruktureinrichtungTyp;

    private LookupListModel artGsNachmittagBetreuung;

    private LookupListModel sobonOrientierungswertJahr;
}
