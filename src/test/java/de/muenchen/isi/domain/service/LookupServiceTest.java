package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.isi.api.controller.LookupController;
import org.junit.jupiter.api.Test;

class LookupServiceTest {

    private final LookupService lookupService = new LookupService();

    @Test
    void getLookupLists() throws JsonProcessingException {
        final var resultModel = this.lookupService.getLookupLists();
        final var result = new ObjectMapper().writeValueAsString(resultModel);
        assertThat(result, is(this.getExpectedJsonString()));
    }

    /**
     * @return die JSON-Response des REST-Endpunkts {@link LookupController#getLookupLists()}.
     */
    private String getExpectedJsonString() {
        return "{\"uncertainBoolean\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"TRUE\",\"value\":\"Ja\"},{\"key\":\"FALSE\",\"value\":\"Nein\"}]},\"artDokument\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"EMAIL\",\"value\":\"E-Mail\"},{\"key\":\"BESCHLUSS\",\"value\":\"Beschluss(entwurf)\"},{\"key\":\"ANLAGE\",\"value\":\"Anlage zu Beschluss(entwurf)\"},{\"key\":\"ANTRAG\",\"value\":\"Antrag\"},{\"key\":\"KARTE\",\"value\":\"Kartenausschnitt / Lageplan\"},{\"key\":\"STELLUNGNAHME\",\"value\":\"Stellungnahme\"},{\"key\":\"DATEN_BAUVORHABEN\",\"value\":\"Planungsdaten Bauvorhaben\"},{\"key\":\"GEBAEUDEPLAN\",\"value\":\"Gebäudeplan und Ansicht\"},{\"key\":\"BERECHNUNG\",\"value\":\"Berechnung\"},{\"key\":\"INFOS_BAUGENEHMIGUNG\",\"value\":\"Infos zur Baugenehmigung\"},{\"key\":\"PRESSEARTIKEL\",\"value\":\"Presseartikel\"},{\"key\":\"INFOS_ZU_SOZ_INFRASTRUKTUR\",\"value\":\"Infos zu soz. Infrastruktur\"},{\"key\":\"PROTOKOLL\",\"value\":\"Protokoll\"},{\"key\":\"SONSTIGES\",\"value\":\"Sonstiges\"}]},\"artAbfrage\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"BAULEITPLANVERFAHREN\",\"value\":\"Bauleitplanverfahren\"},{\"key\":\"BAUGENEHMIGUNGSVERFAHREN\",\"value\":\"Baugenehmigungsverfahren\"},{\"key\":\"WEITERES_VERFAHREN\",\"value\":\"Weiteres Verfahren\"}]},\"sobonVerfahrensgrundsaetzeJahr\":{\"list\":[{\"key\":\"JAHR_1995\",\"value\":\"1995\"},{\"key\":\"JAHR_1997\",\"value\":\"1997\"},{\"key\":\"JAHR_2001\",\"value\":\"2001\"},{\"key\":\"JAHR_2006\",\"value\":\"2006\"},{\"key\":\"JAHR_2012\",\"value\":\"2012\"},{\"key\":\"JAHR_2017\",\"value\":\"2017\"},{\"key\":\"JAHR_2017_PLUS\",\"value\":\"2017 plus\"},{\"key\":\"JAHR_2021\",\"value\":\"2021\"}]},\"standVerfahrenBauleitplanverfahren\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"VORBEREITUNG_ECKDATENBESCHLUSS\",\"value\":\"Vorbereitung Eckdatenbeschluss\"},{\"key\":\"VORBEREITUNG_WETTBEWERBAUSLOBUNG\",\"value\":\"Vorbereitung Wettbewerbsauslobung\"},{\"key\":\"VORBEREITUNG_AUFSTELLUNGSBESCHLUSS\",\"value\":\"Vorbereitung Aufstellungsbeschluss (ggf. + Eckdatenbeschluss)\"},{\"key\":\"VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG\",\"value\":\"Vorbereitung Billigungsbeschluss / Städtebaulicher Vertrag\"},{\"key\":\"VORLIEGENDER_SATZUNGSBESCHLUSS\",\"value\":\"vorliegender Satzungsbeschluss\"},{\"key\":\"RECHTSVERBINDLICHKEIT_AMTSBLATT\",\"value\":\"Rechtsverbindlichkeit (Amtsblatt)\"},{\"key\":\"AUFTEILUNGSPLAN\",\"value\":\"Aufteilungsplan\"},{\"key\":\"INFO_FEHLT\",\"value\":\"Info fehlt\"},{\"key\":\"FREIE_EINGABE\",\"value\":\"freie Eingabe\"}]},\"standVerfahrenBaugenehmigungsverfahren\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"VORBEREITUNG_VORBESCHEID\",\"value\":\"Vorbereitung Vorbescheid\"},{\"key\":\"VORBEREITUNG_BAUGENEHMIGUNG\",\"value\":\"Vorbereitung Baugenehmigung\"},{\"key\":\"INFO_FEHLT\",\"value\":\"Info fehlt\"},{\"key\":\"FREIE_EINGABE\",\"value\":\"freie Eingabe\"}]},\"standVerfahrenWeiteresVerfahren\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"VORABFRAGE_OHNE_KONKRETEN_STAND\",\"value\":\"Vorabfrage ohne konkreten Stand\"},{\"key\":\"STRUKTURKONZEPT\",\"value\":\"Strukturkonzept\"},{\"key\":\"RAHMENPLANUNG\",\"value\":\"Rahmenplanung\"},{\"key\":\"POTENTIALUNTERSUCHUNG\",\"value\":\"Potentialuntersuchung\"},{\"key\":\"STAEDTEBAULICHE_SANIERUNGSMASSNAHME\",\"value\":\"Städtebauliche Sanierungsmaßnahme (Sanierungsgebiet)\"},{\"key\":\"STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME\",\"value\":\"Städtebauliche Entwicklungsmaßnahme (Entwicklungsgebiet)\"},{\"key\":\"INFO_FEHLT\",\"value\":\"Info fehlt\"},{\"key\":\"FREIE_EINGABE\",\"value\":\"freie Eingabe\"},{\"key\":\"STANDORTABFRAGE\",\"value\":\"Standortabfrage\"}]},\"standVerfahren\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"VORBEREITUNG_ECKDATENBESCHLUSS\",\"value\":\"Vorbereitung Eckdatenbeschluss\"},{\"key\":\"VORBEREITUNG_WETTBEWERBAUSLOBUNG\",\"value\":\"Vorbereitung Wettbewerbsauslobung\"},{\"key\":\"VORBEREITUNG_AUFSTELLUNGSBESCHLUSS\",\"value\":\"Vorbereitung Aufstellungsbeschluss (ggf. + Eckdatenbeschluss)\"},{\"key\":\"VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG\",\"value\":\"Vorbereitung Billigungsbeschluss / Städtebaulicher Vertrag\"},{\"key\":\"VORLIEGENDER_SATZUNGSBESCHLUSS\",\"value\":\"vorliegender Satzungsbeschluss\"},{\"key\":\"RECHTSVERBINDLICHKEIT_AMTSBLATT\",\"value\":\"Rechtsverbindlichkeit (Amtsblatt)\"},{\"key\":\"AUFTEILUNGSPLAN\",\"value\":\"Aufteilungsplan\"},{\"key\":\"VORBEREITUNG_VORBESCHEID\",\"value\":\"Vorbereitung Vorbescheid\"},{\"key\":\"VORBEREITUNG_BAUGENEHMIGUNG\",\"value\":\"Vorbereitung Baugenehmigung\"},{\"key\":\"VORABFRAGE_OHNE_KONKRETEN_STAND\",\"value\":\"Vorabfrage ohne konkreten Stand\"},{\"key\":\"STRUKTURKONZEPT\",\"value\":\"Strukturkonzept\"},{\"key\":\"RAHMENPLANUNG\",\"value\":\"Rahmenplanung\"},{\"key\":\"POTENTIALUNTERSUCHUNG\",\"value\":\"Potentialuntersuchung\"},{\"key\":\"STAEDTEBAULICHE_SANIERUNGSMASSNAHME\",\"value\":\"Städtebauliche Sanierungsmaßnahme (Sanierungsgebiet)\"},{\"key\":\"STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME\",\"value\":\"Städtebauliche Entwicklungsmaßnahme (Entwicklungsgebiet)\"},{\"key\":\"INFO_FEHLT\",\"value\":\"Info fehlt\"},{\"key\":\"FREIE_EINGABE\",\"value\":\"freie Eingabe\"},{\"key\":\"STANDORTABFRAGE\",\"value\":\"Standortabfrage\"}]},\"statusAbfrage\":{\"list\":[{\"key\":\"ANGELEGT\",\"value\":\"angelegt\"},{\"key\":\"OFFEN\",\"value\":\"offen\"},{\"key\":\"IN_BEARBEITUNG_SACHBEARBEITUNG\",\"value\":\"in Bearbeitung bei Sachbearbeitung\"},{\"key\":\"IN_BEARBEITUNG_FACHREFERATE\",\"value\":\"in Bearbeitung bei den Fachreferaten\"},{\"key\":\"BEDARFSMELDUNG_ERFOLGT\",\"value\":\"Bedarfsmeldung der Fachreferate ist erfolgt\"},{\"key\":\"ERLEDIGT_MIT_FACHREFERAT\",\"value\":\"erledigt\"},{\"key\":\"ERLEDIGT_OHNE_FACHREFERAT\",\"value\":\"erledigt\"},{\"key\":\"ABBRUCH\",\"value\":\"abbruch\"}]},\"wesentlicheRechtsgrundlageBauleitplanverfahren\":{\"list\":[{\"key\":\"QUALIFIZIERTER_BEBAUUNGSPLAN\",\"value\":\"Qualifizierter Bebauungsplan (§30 Abs. 1 BauGB)\"},{\"key\":\"VORHABENSBEZOGENER_BEBAUUNGSPLAN\",\"value\":\"Vorhabenbezogener Bebauungsplan (§12 BauGB, § 30 Abs. 2 BauGB)\"},{\"key\":\"EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30\",\"value\":\"Einfacher Bebauungsplan (§30 Abs. 3 BauGB)\"},{\"key\":\"SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9\",\"value\":\"Sektoraler Bebauungsplan (§9 Abs. 2a-d BauGB)\"},{\"key\":\"INFO_FEHLT\",\"value\":\"Info fehlt\"},{\"key\":\"FREIE_EINGABE\",\"value\":\"freie Eingabe\"}]},\"wesentlicheRechtsgrundlageBaugenehmigungsverfahren\":{\"list\":[{\"key\":\"QUALIFIZIERTER_BEBAUUNGSPLAN\",\"value\":\"Qualifizierter Bebauungsplan (§30 Abs. 1 BauGB)\"},{\"key\":\"VORHABENSBEZOGENER_BEBAUUNGSPLAN\",\"value\":\"Vorhabenbezogener Bebauungsplan (§12 BauGB, § 30 Abs. 2 BauGB)\"},{\"key\":\"EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35\",\"value\":\"Einfacher Bebauungsplan (§30 Abs. 3 BauGB i.V.m. §34 BauGB oder i.V.m. §35 BauGB)\"},{\"key\":\"SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35\",\"value\":\"Sektoraler Bebauungsplan (§30 Abs. 3 BauGB i.V.m. §34 BauGB oder i.V.m. §35 BauGB)\"},{\"key\":\"INNENBEREICH\",\"value\":\"Innenbereich (§34 BauGB)\"},{\"key\":\"AUSSENBEREICH\",\"value\":\"Außenbereich (§35 BauGB)\"},{\"key\":\"BEFREIUNG\",\"value\":\"Befreiung (§ 31 BauGB)\"},{\"key\":\"INFO_FEHLT\",\"value\":\"Info fehlt\"},{\"key\":\"FREIE_EINGABE\",\"value\":\"freie Eingabe\"}]},\"wesentlicheRechtsgrundlage\":{\"list\":[{\"key\":\"QUALIFIZIERTER_BEBAUUNGSPLAN\",\"value\":\"Qualifizierter Bebauungsplan (§30 Abs. 1 BauGB)\"},{\"key\":\"VORHABENSBEZOGENER_BEBAUUNGSPLAN\",\"value\":\"Vorhabenbezogener Bebauungsplan (§12 BauGB, § 30 Abs. 2 BauGB)\"},{\"key\":\"EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30\",\"value\":\"Einfacher Bebauungsplan (§30 Abs. 3 BauGB)\"},{\"key\":\"EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35\",\"value\":\"Einfacher Bebauungsplan (§30 Abs. 3 BauGB i.V.m. §34 BauGB oder i.V.m. §35 BauGB)\"},{\"key\":\"SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9\",\"value\":\"Sektoraler Bebauungsplan (§9 Abs. 2a-d BauGB)\"},{\"key\":\"SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35\",\"value\":\"Sektoraler Bebauungsplan (§30 Abs. 3 BauGB i.V.m. §34 BauGB oder i.V.m. §35 BauGB)\"},{\"key\":\"INNENBEREICH\",\"value\":\"Innenbereich (§34 BauGB)\"},{\"key\":\"AUSSENBEREICH\",\"value\":\"Außenbereich (§35 BauGB)\"},{\"key\":\"BEFREIUNG\",\"value\":\"Befreiung (§ 31 BauGB)\"},{\"key\":\"INFO_FEHLT\",\"value\":\"Info fehlt\"},{\"key\":\"FREIE_EINGABE\",\"value\":\"freie Eingabe\"}]},\"planungsrecht\":null,\"artBaulicheNutzung\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"WR\",\"value\":\"Reines Wohngebiet (WR)\"},{\"key\":\"WA\",\"value\":\"Allgemeines Wohngebiet (WA)\"},{\"key\":\"MU\",\"value\":\"Urbanes Gebiet (MU)\"},{\"key\":\"MK\",\"value\":\"Kerngebiet (MK)\"},{\"key\":\"MI\",\"value\":\"Mischgebiet (MI)\"},{\"key\":\"GE\",\"value\":\"Gewerbegebiet (GE)\"},{\"key\":\"INFO_FEHLT\",\"value\":\"Info fehlt\"}]},\"artBaulicheNutzungBauvorhaben\":{\"list\":[{\"key\":\"WR\",\"value\":\"Reines Wohngebiet (WR)\"},{\"key\":\"WA\",\"value\":\"Allgemeines Wohngebiet (WA)\"},{\"key\":\"MU\",\"value\":\"Urbanes Gebiet (MU)\"},{\"key\":\"MK\",\"value\":\"Kerngebiet (MK)\"},{\"key\":\"MI\",\"value\":\"Mischgebiet (MI)\"},{\"key\":\"GE\",\"value\":\"Gewerbegebiet (GE)\"},{\"key\":\"INFO_FEHLT\",\"value\":\"Info fehlt\"}]},\"statusInfrastruktureinrichtung\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"UNGESICHERTE_PLANUNG\",\"value\":\"ungesicherte Planung\"},{\"key\":\"GESICHERTE_PLANUNG_NEUE_EINR\",\"value\":\"gesicherte Planung – neue Einrichtung\"},{\"key\":\"GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR\",\"value\":\"gesicherte Planung – Erweiterung Plätze bei bestehenden Einrichtungen\"},{\"key\":\"GESICHERTE_PLANUNG_TF_KITA_STANDORT\",\"value\":\"gesicherte Planung – TF Kita Standort\"},{\"key\":\"GESICHERTE_PLANUNG_REDUZIERUNG_PLAETZE\",\"value\":\"gesicherte Planung – Reduzierung Plätze bei bestehenden Einrichtungen\"},{\"key\":\"GESICHERTE_PLANUNG_INTERIMSSTANDORT\",\"value\":\"gesicherte Planung – Interimsstandort\"},{\"key\":\"UNGESICHERTE_PLANUNG_TF_KITA_STANDORT\",\"value\":\"ungesicherte Planung – TF Kita Standort\"},{\"key\":\"BESTAND\",\"value\":\"Bestand\"}]},\"einrichtungstraeger\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"STAEDTISCHE_EINRICHTUNG\",\"value\":\"Städtische Einrichtung\"},{\"key\":\"EINRICHTUNG_BETRIEBSTRAEGERSCHAFT\",\"value\":\"Einrichtung in Betriebsträgerschaft\"},{\"key\":\"FREIE_GEMEINNUETZIGE_SONSTIGE\",\"value\":\"Freie / gemeinnützige / sonstige Einrichtungen\"},{\"key\":\"EINRICHTUNG_GESAMTSTAEDTISCH\",\"value\":\"Einrichtung, deren Plätze nur gesamtstädtisch zur Verfügung stehen\"},{\"key\":\"ELTERN_KIND_INITIATIVE\",\"value\":\"Eltern-Kind-Initiative\"},{\"key\":\"STAATLICHE_EINRICHTUNG\",\"value\":\"Staatliche Einrichtung\"}]},\"einrichtungstraegerSchulen\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"STAEDTISCHE_EINRICHTUNG\",\"value\":\"Städtische Einrichtung\"},{\"key\":\"FREIE_GEMEINNUETZIGE_SONSTIGE\",\"value\":\"Freie / gemeinnützige / sonstige Einrichtungen\"},{\"key\":\"STAATLICHE_EINRICHTUNG\",\"value\":\"Staatliche Einrichtung\"},{\"key\":\"PRIVATE_TRAEGERSCHAFT\",\"value\":\"Private Trägerschaft\"},{\"key\":\"KIRCHLICHE_TRAEGERSCHAFT\",\"value\":\"Kirchliche Trägerschaft\"}]},\"infrastruktureinrichtungTyp\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"KINDERKRIPPE\",\"value\":\"Kinderkrippe\"},{\"key\":\"KINDERGARTEN\",\"value\":\"Kindergarten\"},{\"key\":\"GS_NACHMITTAG_BETREUUNG\",\"value\":\"Nachmittagsbetreuung für Grundschulkinder\"},{\"key\":\"HAUS_FUER_KINDER\",\"value\":\"Haus für Kinder\"},{\"key\":\"GRUNDSCHULE\",\"value\":\"Grundschule\"},{\"key\":\"MITTELSCHULE\",\"value\":\"Mittelschule\"}]},\"artGsNachmittagBetreuung\":{\"list\":[{\"key\":\"HORT\",\"value\":\"Hort\"},{\"key\":\"KOOPERATIVER_GANZTAG_FLEXIBLE_VARIANTE\",\"value\":\"Kooperativer Ganztag – flexibel\"},{\"key\":\"KOOPERATIVER_GANZTAG_RHYTHMISIERTE_VARIANTE\",\"value\":\"Kooperativer Ganztag – rhythmisiert\"},{\"key\":\"TAGESHEIM\",\"value\":\"Tagesheim\"},{\"key\":\"MITTAGSBETREUUNG\",\"value\":\"Mittagsbetreuung\"}]},\"sobonOrientierungswertJahr\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"JAHR_2014\",\"value\":\"2014\"},{\"key\":\"JAHR_2017\",\"value\":\"2017\"},{\"key\":\"JAHR_2022\",\"value\":\"2022\"},{\"key\":\"STANDORTABFRAGE\",\"value\":\"Standortabfrage\"}]},\"sobonOrientierungswertJahrWithoutStandortabfrage\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"JAHR_2014\",\"value\":\"2014\"},{\"key\":\"JAHR_2017\",\"value\":\"2017\"},{\"key\":\"JAHR_2022\",\"value\":\"2022\"}]}}";
    }
}
