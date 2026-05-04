package de.muenchen.isi.domain.model.stammdaten;

import lombok.Data;

@Data
public class LookupListsModel {

    private LookupListModel uncertainBoolean;

    private LookupListModel artDokument;

    private LookupListModel artAbfrage;

    private LookupListModel sobonVerfahrensgrundsaetzeJahr;

    private LookupListModel verfahrensstandBauleitplanverfahren;

    private LookupListModel verfahrensstandBaugenehmigungsverfahren;

    private LookupListModel verfahrensstandWeiteresVerfahren;

    private LookupListModel verfahrensstand;

    private LookupListModel statusAbfrage;

    private LookupListModel wesentlicheRechtsgrundlageBauleitplanverfahren;

    private LookupListModel wesentlicheRechtsgrundlageBaugenehmigungsverfahren;

    private LookupListModel wesentlicheRechtsgrundlage;

    private LookupListModel planart;

    private LookupListModel artBaulicheNutzung;

    private LookupListModel artBaulicheNutzungBauvorhaben;

    private LookupListModel statusInfrastruktureinrichtung;

    private LookupListModel einrichtungstraeger;

    private LookupListModel einrichtungstraegerSchulen;

    private LookupListModel infrastruktureinrichtungTyp;

    private LookupListModel artGsNachmittagBetreuung;

    private LookupListModel sobonOrientierungswertJahr;

    private LookupListModel sobonOrientierungswertJahrWithoutStandortabfrage;
}
