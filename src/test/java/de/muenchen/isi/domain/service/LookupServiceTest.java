package de.muenchen.isi.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.isi.api.controller.LookupController;
import de.muenchen.isi.domain.model.stammdaten.LookupListsModel;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class LookupServiceTest {

    private final LookupService lookupService = new LookupService();

    @Test
    void getLookupLists() throws JsonProcessingException {
        final LookupListsModel resultModel = this.lookupService.getLookupLists();
        final String result = new ObjectMapper().writeValueAsString(resultModel);
        assertThat(result, is(this.getExpectedJsonString()));
    }

    /**
     * @return die JSON-Response des REST-Endpunkts {@link LookupController#getLookupLists()}.
     */
    private String getExpectedJsonString() {
        return "{\"uncertainBoolean\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"TRUE\",\"value\":\"Ja\"},{\"key\":\"FALSE\",\"value\":\"Nein\"}]},\"artDokument\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"EMAIL\",\"value\":\"E-Mail\"},{\"key\":\"BESCHLUSS\",\"value\":\"Beschluss(entwurf)\"},{\"key\":\"ANLAGE\",\"value\":\"Anlage zu Beschluss(entwurf)\"},{\"key\":\"ANTRAG\",\"value\":\"Antrag\"},{\"key\":\"KARTE\",\"value\":\"Kartenausschnitt / Lageplan\"},{\"key\":\"STELLUNGNAHME\",\"value\":\"Stellungnahme\"},{\"key\":\"DATEN_BAUVORHABEN\",\"value\":\"Planungsdaten Bauvorhaben\"},{\"key\":\"GEBAEUDEPLAN\",\"value\":\"Gebäudeplan und Ansicht\"},{\"key\":\"BERECHNUNG\",\"value\":\"Berechnung\"},{\"key\":\"INFOS_BAUGENEHMIGUNG\",\"value\":\"Infos zur Baugenehmigung\"},{\"key\":\"PRESSEARTIKEL\",\"value\":\"Presseartikel\"},{\"key\":\"SONSTIGES\",\"value\":\"Sonstiges\"}]},\"artAbfrage\":{\"list\":[{\"key\":\"ERMITTLUNG_SOZINFRA_BEDARF\",\"value\":\"Ermittlung des durch das Wohnbauvorhaben ausgelösten Infrastrukturbedarfs\"},{\"key\":\"ERMITTLUNG_FLAECHE_SOZINFRA_BEDARF\",\"value\":\"Flächen- / Standortsuche\"},{\"key\":\"ABSCHAETZUNG_FLAECHE_FUER_SOZINFRA\",\"value\":\"Infrastrukturbedarfsermittlung für Flächen ohne Wohnbauvorhaben\"},{\"key\":\"STELLUNGNAHME_MITZEICHNUNGSKETTE_BESCHLUSSVORLAGE\",\"value\":\"Stellungnahme zu Beschlussvorlage\"}]},\"sobonVerfahrensgrundsaetzeJahr\":{\"list\":[{\"key\":\"DAVOR\",\"value\":\"vor 2014\"},{\"key\":\"JAHR_2014\",\"value\":\"2014\"},{\"key\":\"JAHR_2017\",\"value\":\"2017\"},{\"key\":\"JAHR_2017_PLUS\",\"value\":\"2017 plus\"},{\"key\":\"JAHR_2021\",\"value\":\"2021\"}]},\"standVorhaben\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"GRUNDSATZ_ECKDATENBESCHLUSS\",\"value\":\"Grundsatz- und Eckdatenbeschluss\"},{\"key\":\"AUFSTELLUNGSBESCHLUSS\",\"value\":\"Aufstellungsbeschluss\"},{\"key\":\"PARAG_3_1_BAUGB\",\"value\":\"§ 3.1 BauGB – Unterrichtung Öff.\"},{\"key\":\"PARAG_3_2_BAUGB\",\"value\":\"§ 3.2 BauGB – öff. Auslegung\"},{\"key\":\"PARAG_4_1_2_BAUGB\",\"value\":\"§ 4.1/2 BauGB – TÖB, Behördenbet.\"},{\"key\":\"BILLIGUNGSBESCHLUSS\",\"value\":\"Billigungsbeschluss\"},{\"key\":\"SATZUNGSBESCHLUSS\",\"value\":\"Satzungsbeschluss\"},{\"key\":\"BPLAN_IN_KRAFT\",\"value\":\"Bplan in Kraft getreten – Baurecht vorhanden\"},{\"key\":\"VORBESCHEID_EINGEREICHT\",\"value\":\"Vorbescheid eingereicht\"},{\"key\":\"BAUANTRAG_EINGEREICHT\",\"value\":\"Bauantrag eingereicht\"},{\"key\":\"BAUGENEHMIGUNG_ERTEILT\",\"value\":\"Baugenehmigung erteilt\"},{\"key\":\"BAUBEGINN_ANGEZEIGT\",\"value\":\"Baubeginn angezeigt\"},{\"key\":\"BAUFERTIGSTELLUNG_GEPLANT\",\"value\":\"Baufertigstellung geplant\"},{\"key\":\"BAUFERTIGSTELLUNG_ANGEZEIGT\",\"value\":\"Baufertigstellung angezeigt\"}]},\"statusAbfrage\":{\"list\":[{\"key\":\"ANGELEGT\",\"value\":\"angelegt\"},{\"key\":\"OFFEN\",\"value\":\"offen\"},{\"key\":\"IN_ERFASSUNG\",\"value\":\"in Erfassung\"},{\"key\":\"IN_BEARBEITUNG_PLAN\",\"value\":\"in Bearbeitung bei PlAN-HA I/2\"},{\"key\":\"IN_BEARBEITUNG_FACHREFERATE\",\"value\":\"in Bearbeitung bei den Fachreferaten\"},{\"key\":\"BEDARFSMELDUNG_ERFOLGT\",\"value\":\"Bedarfsmeldung der Fachreferate ist erfolgt\"},{\"key\":\"ERLEDIGT\",\"value\":\"erledigt\"},{\"key\":\"ABBRUCH\",\"value\":\"abbruch\"}]},\"planungsrecht\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"BPLAN_PARAG_30\",\"value\":\"Bebauungsplan: § 30\"},{\"key\":\"BPLAN_PARAG_12\",\"value\":\"Bebauungsplan: § 12 vorhabenbezogen\"},{\"key\":\"BPLAN_PARAG_11\",\"value\":\"Bebauungsplan: § 11 SoBoN\"},{\"key\":\"BPLAN_AEND_BBPLAN\",\"value\":\"Bebauungsplan: Änderung\"},{\"key\":\"NACHVERD_PARAG_34\",\"value\":\"Nachverdichtung: § 34 Innenbereich\"},{\"key\":\"NACHVERD_PARAG_35\",\"value\":\"Nachverdichtung: § 35 Außenbereich\"},{\"key\":\"NACHVERD_PARAG_31\",\"value\":\"Nachverdichtung: § 31 Befreiung Bplan\"},{\"key\":\"NACHVERD_BAURECHTSAUSSCHOEPFUNG\",\"value\":\"Nachverdichtung: Baurechtsausschöpfung (z.B. sektoraler Bebauungsplan)\"},{\"key\":\"SONSTIGES_UMSTRUKTURIERUNG\",\"value\":\"Sonstiges: Umstrukturierung\"},{\"key\":\"SONSTIGES_PARAG_165\",\"value\":\"Sonstiges: § 165 SEM\"},{\"key\":\"SONSTIGES_PARAG_246\",\"value\":\"Sonstiges: § 246 Flüchtlinge\"}]},\"baugebietTyp\":{\"list\":[{\"key\":\"MI\",\"value\":\"Mischgebiet (MI)\"},{\"key\":\"WA\",\"value\":\"Allgemeines Wohngebiet (WA)\"},{\"key\":\"MU\",\"value\":\"Urbanes Gebiet (MU)\"},{\"key\":\"MK\",\"value\":\"Kerngebiet (MK)\"},{\"key\":\"WR\",\"value\":\"Reines Wohngebiet (WR)\"},{\"key\":\"GE\",\"value\":\"Gewerbegebiet (GE)\"}]},\"zustaendigeDienststelle\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"PLAN_HA_2\",\"value\":\"PLAN-HA II Stadtplanung\"},{\"key\":\"KR\",\"value\":\"Grundstücksverwaltung / -verkehr\"},{\"key\":\"PLAN_HA_3\",\"value\":\"PLAN-HA III Stadtsanierung\"},{\"key\":\"PLAN_HA_4\",\"value\":\"PLAN-HA IV Baugenehmigung\"},{\"key\":\"PLAN_HA_1_11_2\",\"value\":\"PLAN-HA I/11-2 Koordinierung Stellungnahmen PLAN HA I\"},{\"key\":\"PLAN_HA_1_4\",\"value\":\"PLAN-HA I/11-2 Koordinierung Stellungnahmen PLAN HA I\"},{\"key\":\"RBS_SB\",\"value\":\"RBS-SB Schul- und Kitabedarfsplanung Stabsstelle Steuerungsunterstützung und Bedarfsplanung\"},{\"key\":\"RBS_ZIM_N\",\"value\":\"RBS-ZIM-N Neubau Infrastruktureinrichtungen\"}]},\"statusInfrastruktureinrichtung\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"UNGESICHERTE_PLANUNG\",\"value\":\"ungesicherte Planung\"},{\"key\":\"GESICHERTE_PLANUNG_NEUE_EINR\",\"value\":\"gesicherte Planung – neue Einrichtung\"},{\"key\":\"GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR\",\"value\":\"gesicherte Planung – Erweiterung Plätze bei bestehenden Einrichtungen\"},{\"key\":\"GESICHERTE_PLANUNG_TF_KITA_STANDORT\",\"value\":\"gesicherte Planung – TF Kita Standort\"},{\"key\":\"GESICHERTE_PLANUNG_REDUZIERUNG_PLAETZE\",\"value\":\"gesicherte Planung – Reduzierung Plätze bei bestehenden Einrichtungen\"},{\"key\":\"GESICHERTE_PLANUNG_INTERIMSSTANDORT\",\"value\":\"gesicherte Planung – Interimsstandort\"},{\"key\":\"UNGESICHERTE_PLANUNG_TF_KITA_STANDORT\",\"value\":\"ungesicherte Planung – TF Kita Standort\"},{\"key\":\"BESTAND\",\"value\":\"Bestand\"}]},\"einrichtungstraeger\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"KITA_STAEDTISCHE_EINRICHTUNG\",\"value\":\"Kita: Städtische Einrichtung\"},{\"key\":\"KITA_EINRICHTUNG_BETRIEBSTRAEGERSCHAFT\",\"value\":\"Kita: Einrichtung in Betriebsträgerschaft\"},{\"key\":\"KITA_FREIE_GEMEINNUETZIGE_SONSTIGE\",\"value\":\"Kita: Freie / gemeinnützige / sonstige Einrichtungen\"},{\"key\":\"KITA_EINRICHTUNG_GESAMTSTAEDTISCH\",\"value\":\"Kita: Einrichtung, deren Plätze nur gesamtstädtisch zur Verfügung stehen\"},{\"key\":\"GS_BETREUUNG_STAEDTISCHE_EINRICHTUNG\",\"value\":\"GS-Betreuung: Städtische Einrichtung\"},{\"key\":\"GS_BETREUUNG_ELTERN_KIND_INITIATIVE\",\"value\":\"GS-Betreuung: Eltern-Kind-Initiative\"},{\"key\":\"GS_BETREUUNG_STAATLICHE_EINRICHTUNG\",\"value\":\"GS-Betreuung: Staatliche Einrichtung\"},{\"key\":\"GS_BETREUUNG_PRIVATE_TRAEGERSCHAFT\",\"value\":\"GS-Betreuung: Private Trägerschaft\"},{\"key\":\"GS_BETREUUNG_KIRCHLICHE_TRAEGERSCHAFT\",\"value\":\"GS-Betreuung: Kirchliche Trägerschaft\"},{\"key\":\"SCHULE_STAEDTISCHE_EINRICHTUNG\",\"value\":\"Schule: Städtische Einrichtung\"},{\"key\":\"SCHULE_STAATLICHE_EINRICHTUNG\",\"value\":\"Schule: Staatliche Einrichtung\"},{\"key\":\"SCHULE_PRIVATE_TRAEGERSCHAFT\",\"value\":\"Schule: Private Trägerschaft\"},{\"key\":\"SCHULE_KIRCHLICHE_TRAEGERSCHAFT\",\"value\":\"Schule: Kirchliche Trägerschaft\"}]},\"infrastruktureinrichtungTyp\":{\"list\":[{\"key\":\"UNSPECIFIED\",\"value\":\"- - - Keine Angabe - - -\"},{\"key\":\"KINDERKRIPPE\",\"value\":\"Kinderkrippe\"},{\"key\":\"KINDERGARTEN\",\"value\":\"Kindergarten\"},{\"key\":\"GS_NACHMITTAG_BETREUUNG\",\"value\":\"Nachmittagsbetreuung für Grundschulkinder\"},{\"key\":\"HAUS_FUER_KINDER\",\"value\":\"Haus für Kinder\"},{\"key\":\"GRUNDSCHULE\",\"value\":\"Grundschule\"},{\"key\":\"MITTELSCHULE\",\"value\":\"Mittelschule\"}]},\"artGsNachmittagBetreuung\":{\"list\":[{\"key\":\"HORT\",\"value\":\"Hort\"},{\"key\":\"KOOPERATIVER_GANZTAG_FLEXIBLE_VARIANTE\",\"value\":\"Kooperativer Ganztag – flexibel\"},{\"key\":\"KOOPERATIVER_GANZTAG_RHYTHMISIERTE_VARIANTE\",\"value\":\"Kooperativer Ganztag – rhythmisiert\"},{\"key\":\"TAGESHEIM\",\"value\":\"Tagesheim\"},{\"key\":\"MITTAGSBETREUUNG\",\"value\":\"Mittagsbetreuung\"}]}}";
    }

}
