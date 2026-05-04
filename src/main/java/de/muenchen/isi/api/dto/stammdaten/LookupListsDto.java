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

    private LookupListDto wesentlicheRechtsgrundlageBauleitplanverfahren;

    private LookupListDto wesentlicheRechtsgrundlageBaugenehmigungsverfahren;

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
}
