package de.muenchen.isi.domain.model.stammdaten;

import lombok.Data;

@Data
public class LookupListsModel {

    private LookupListModel uncertainBoolean;

    private LookupListModel artDokument;

    private LookupListModel artAbfrage;

    private LookupListModel sobonVerfahrensgrundsaetzeJahr;

    private LookupListModel standVerfahrenBauleitplanverfahrenList;

    private LookupListModel standVerfahren;

    private LookupListModel statusAbfrage;

    private LookupListModel wesentlicheRechtsgrundlageBauleitplanverfahren;

    private LookupListModel wesentlicheRechtsgrundlage;

    private LookupListModel planungsrecht;

    private LookupListModel artBaulicheNutzung;

    private LookupListModel statusInfrastruktureinrichtung;

    private LookupListModel einrichtungstraeger;

    private LookupListModel infrastruktureinrichtungTyp;

    private LookupListModel artGsNachmittagBetreuung;

    private LookupListModel sobonOrientierungswertJahr;
}
