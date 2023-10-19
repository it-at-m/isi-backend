package de.muenchen.isi.api.dto.stammdaten;

import lombok.Data;

@Data
public class LookupListsDto {

    private LookupListDto uncertainBoolean;

    private LookupListDto artDokument;

    private LookupListDto artAbfrage;

    private LookupListDto sobonVerfahrensgrundsaetzeJahr;

    private LookupListDto standVerfahrenBauleitplanverfahren;

    private LookupListDto standVerfahren;

    private LookupListDto statusAbfrage;

    private LookupListDto wesentlicheRechtsgrundlageBauleitplanverfahren;

    private LookupListDto wesentlicheRechtsgrundlage;

    private LookupListDto artBaulicheNutzung;

    private LookupListDto statusInfrastruktureinrichtung;

    private LookupListDto einrichtungstraeger;

    private LookupListDto infrastruktureinrichtungTyp;

    private LookupListDto artGsNachmittagBetreuung;

    private LookupListDto sobonOrientierungswertJahr;
}
