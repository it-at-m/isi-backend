package de.muenchen.isi.api.dto.stammdaten;

import lombok.Data;

@Data
public class LookupListsDto {

    private LookupListDto uncertainBoolean;

    private LookupListDto artDokument;

    private LookupListDto artAbfrage;

    private LookupListDto sobonVerfahrensgrundsaetzeJahr;

    private LookupListDto verfahrensstandBauleitplanverfahren;

    private LookupListDto verfahrensstandBaugenehmigungsverfahren;

    private LookupListDto verfahrensstandWeiteresVerfahren;

    private LookupListDto verfahrensstand;

    private LookupListDto statusAbfrage;

    private LookupListDto planart;

    private LookupListDto wesentlicheRechtsgrundlageBaugenehmigungsverfahren;

    private LookupListDto wesentlicheRechtsgrundlageWeiteresVerfahren;

    private LookupListDto wesentlicheRechtsgrundlageBauvorhaben;

    private LookupListDto wesentlicheRechtsgrundlage;

    private LookupListDto artBaulicheNutzung;

    private LookupListDto artBaulicheNutzungBauvorhaben;

    private LookupListDto statusInfrastruktureinrichtung;

    private LookupListDto einrichtungstraeger;

    private LookupListDto einrichtungstraegerSchulen;

    private LookupListDto infrastruktureinrichtungTyp;

    private LookupListDto artGsNachmittagBetreuung;

    private LookupListDto sobonOrientierungswertJahr;

    private LookupListDto sobonOrientierungswertJahrWithoutStandortabfrage;

    private LookupListDto bauratenmethodik;
}
