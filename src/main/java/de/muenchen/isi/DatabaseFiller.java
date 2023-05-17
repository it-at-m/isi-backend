/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi;

import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import de.muenchen.isi.infrastructure.entity.Bauabschnitt;
import de.muenchen.isi.infrastructure.entity.Baugebiet;
import de.muenchen.isi.infrastructure.entity.Baurate;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Foerderart;
import de.muenchen.isi.infrastructure.entity.Foerdermix;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import de.muenchen.isi.infrastructure.entity.enums.IdealtypischeBaurateTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtGsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.BaugebietTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.GsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.HausFuerKinder;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Mittelschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Schule;
import de.muenchen.isi.infrastructure.entity.stammdaten.FoerdermixStamm;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.IdealtypischeBaurate;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.Jahresrate;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.GrundschuleRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.GsNachmittagBetreuungRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.HausFuerKinderRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.KindergartenRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.KinderkrippeRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.MittelschuleRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.FoerdermixStammRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.IdealtypischeBaurateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Klasse, die beim Starten der Anwendung im Profil local/dev und no-security
 * Testdaten in die Datenbank schreibt.
 */
@Component
@Profile({"local", "dev", "kon", "demo"})
@Slf4j
@RequiredArgsConstructor
public class DatabaseFiller implements CommandLineRunner {

    private final InfrastrukturabfrageRepository infrastrukturabfrageRepository;

    private final BauvorhabenRepository bauvorhabenRepository;

    private final FoerdermixStammRepository foerdermixStammRepository;

    private final KinderkrippeRepository kinderkrippeRepository;

    private final KindergartenRepository kindergartenRepository;

    private final HausFuerKinderRepository hausFuerKinderRepository;

    private final GsNachmittagBetreuungRepository gsNachmittagBetreuungRepository;

    private final GrundschuleRepository grundschuleRepository;

    private final MittelschuleRepository mittelschuleRepository;

    private final IdealtypischeBaurateRepository idealtypischeBaurateRepository;

    /**
     * Zu Implementierende Methode des CommandLineRunners
     *
     * @param strings siehe Interface, wird nicht benutzt
     */
    @Override
    public void run(final String... strings) throws Exception {

        System.err.println("***********************************************************************************");
        log.info("{} was called after startup of the spring context.", this.getClass().getSimpleName());

        /*
         * Enter here the code to persist some data via the repositories given in the
         * variables.
         */
        this.clearDatabase();

        this.createIdealtypischeBauraten();

        final var bauvorhaben = this.addBauvorhaben1();
        this.addBauvorhaben2();

        this.addNichtOffiziellerVerfahrensschrittInfrastrukturabfrage(bauvorhaben);
        this.addOffiziellerVerfahrensschrittInfrastrukturabfrage(bauvorhaben);

        this.addFoerdermixStaemme();

        this.addInfrastruktureinrichtungen();

        log.info("{} has finished.", this.getClass().getSimpleName());
        System.err.println("***********************************************************************************");
    }

    private void addNichtOffiziellerVerfahrensschrittInfrastrukturabfrage(final Bauvorhaben bauvorhaben) {
        Infrastrukturabfrage abfrage = createNichtOffiziellerVerfahrensschrittInfrastrukturabfrage();
        abfrage.getAbfrage().setBauvorhaben(bauvorhaben);
        final var abfragevariante = createAbfragevariante(1);
        abfrage.setAbfragevarianten(List.of(abfragevariante));
        this.infrastrukturabfrageRepository.save(abfrage);
    }

    private void addOffiziellerVerfahrensschrittInfrastrukturabfrage(final Bauvorhaben bauvorhaben) {
        Infrastrukturabfrage abfrage = createOffiziellerVerfahrensschrittInfrastrukturabfrage();
        abfrage.getAbfrage().setBauvorhaben(bauvorhaben);
        final var abfragevariante = createAbfragevariante(2);
        abfrage.setAbfragevarianten(List.of(abfragevariante));
        this.infrastrukturabfrageRepository.save(abfrage);
    }

    private Bauvorhaben addBauvorhaben1() {
        final var bauvorhaben = createBauvorhaben();
        bauvorhaben.setNameVorhaben("Bauvorhaben 1");
        return this.bauvorhabenRepository.save(bauvorhaben);
    }

    private Bauvorhaben addBauvorhaben2() {
        final var bauvorhaben = createBauvorhaben();
        bauvorhaben.setNameVorhaben("Bauvorhaben 2");
        return this.bauvorhabenRepository.save(bauvorhaben);
    }

    private Bauvorhaben addBauvorhaben3() {
        final var bauvorhaben = createBauvorhaben();
        bauvorhaben.setNameVorhaben("Bauvorhaben 3");
        return this.bauvorhabenRepository.save(bauvorhaben);
    }

    private Bauvorhaben addBauvorhaben4() {
        final var bauvorhaben = createBauvorhaben();
        bauvorhaben.setNameVorhaben("Bauvorhaben 4");
        return this.bauvorhabenRepository.save(bauvorhaben);
    }


    private void addFoerdermixStaemme() {
        for (final FoerdermixStamm foerdermixStamm : createFoerdermixStaemme()) {
            this.foerdermixStammRepository.save(foerdermixStamm);
        }
    }

    private void clearDatabase() {
        this.infrastrukturabfrageRepository.deleteAll();
        this.bauvorhabenRepository.deleteAll();
        this.foerdermixStammRepository.deleteAll();
        this.kinderkrippeRepository.deleteAll();
        this.kindergartenRepository.deleteAll();
        this.hausFuerKinderRepository.deleteAll();
        this.gsNachmittagBetreuungRepository.deleteAll();
        this.grundschuleRepository.deleteAll();
        this.mittelschuleRepository.deleteAll();
        this.idealtypischeBaurateRepository.deleteAll();
    }

    public Infrastrukturabfrage createNichtOffiziellerVerfahrensschrittInfrastrukturabfrage() {
        final Infrastrukturabfrage infrastrukturabfrage = new Infrastrukturabfrage();

        final Abfrage abfrage = new Abfrage();
        final Wgs84 coordinate = new Wgs84();
        coordinate.setLatitude(48.1556795465256);
        coordinate.setLongitude(11.5568456350688);
        abfrage.setAdresse(new Adresse("Lothstraße", "7", "80331", "München", coordinate));
        abfrage.setFristStellungnahme(LocalDate.of(2022, 12, 31));
        abfrage.setAnmerkung("Bitte die Abfrage zeitnah behandeln");
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrage.setNameAbfrage("Neubausiedlung in Musterort");
        abfrage.setStandVorhaben(StandVorhaben.BAUANTRAG_EINGEREICHT);
        infrastrukturabfrage.setAbfrage(abfrage);
        infrastrukturabfrage.setSobonRelevant(UncertainBoolean.TRUE);
        infrastrukturabfrage.setSobonJahr(SobonVerfahrensgrundsaetzeJahr.JAHR_2021);
        infrastrukturabfrage.setAktenzeichenProLbk("PRO12345");
        infrastrukturabfrage.setOffiziellerVerfahrensschritt(UncertainBoolean.FALSE);

        return infrastrukturabfrage;
    }

    public Infrastrukturabfrage createOffiziellerVerfahrensschrittInfrastrukturabfrage() {
        final Infrastrukturabfrage infrastrukturabfrage = new Infrastrukturabfrage();

        final Abfrage abfrage = new Abfrage();
        final Wgs84 coordinate = new Wgs84();
        coordinate.setLatitude(48.136129050253);
        coordinate.setLongitude(11.5711182197544);
        abfrage.setAdresse(new Adresse("Sendlinger Straße", "1A", "80331", "München", coordinate));
        abfrage.setFristStellungnahme(LocalDate.of(2022, 6, 1));
        abfrage.setAnmerkung("Die Baugenehmigung wird nachgereicht");
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        abfrage.setNameAbfrage("Wohnraumverdichtung Stadmitte");
        abfrage.setStandVorhaben(StandVorhaben.BAUGENEHMIGUNG_ERTEILT);
        infrastrukturabfrage.setAbfrage(abfrage);
        infrastrukturabfrage.setSobonRelevant(UncertainBoolean.TRUE);
        infrastrukturabfrage.setSobonJahr(SobonVerfahrensgrundsaetzeJahr.JAHR_2017_PLUS);
        infrastrukturabfrage.setOffiziellerVerfahrensschritt(UncertainBoolean.TRUE);

        return infrastrukturabfrage;
    }

    private Abfragevariante createAbfragevariante(final int variante) {
        final Abfragevariante original = new Abfragevariante();
        original.setAbfragevariantenNr(1);
        original.setAbfragevariantenName("Dorf");
        original.setPlanungsrecht(variante == 1 ? Planungsrecht.BPLAN_PARAG_30 : Planungsrecht.BPLAN_PARAG_12);
        original.setGeschossflaecheWohnen(new BigDecimal(variante == 1 ? "1234.56" : "70"));
        original.setGesamtanzahlWe(variante == 1 ? 31 : 90);
        if (variante == 2) {
            original.setGeschossflaecheStudentenwohnungen(new BigDecimal(25));
        }
        original.setRealisierungVon(variante == 1 ? 2023 : 2024);
        original.setRealisierungBis(variante == 1 ? 2024 : 2026);

        original.setBauabschnitte(List.of(createBauabschnitt()));

        original.setSonderwohnformen(false);

        return original;
    }

    private Bauabschnitt createBauabschnitt() {
        final Bauabschnitt bauabschnitt = new Bauabschnitt();
        bauabschnitt.setBezeichnung("Der einzigartige Bauabschnitt");
        bauabschnitt.setTechnical(false);
        bauabschnitt.setBaugebiete(List.of(createBaugebiet()));
        return bauabschnitt;
    }

    private Baugebiet createBaugebiet() {
        final Baugebiet baugebiet = new Baugebiet();
        baugebiet.setBezeichnung("Das Baugebiet des einzigartigen Baubschnitts");
        baugebiet.setBaugebietTyp(BaugebietTyp.WA);
        baugebiet.setTechnical(false);
        baugebiet.setBauraten(List.of(createBaurate()));
        return baugebiet;
    }

    private  Baurate createBaurate() {
        final Baurate baurate = new Baurate();
        baurate.setJahr(2022);
        baurate.setAnzahlWeGeplant(10);
        baurate.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(15.55));
        baurate.setFoerdermix(createFoerdermix());
        return baurate;
    }

    private Foerdermix createFoerdermix() {
        final Foerdermix foerdermix = new Foerdermix();
        Foerderart foerderart = new Foerderart();
        foerderart.setBezeichnung("AnteilMuenchenModell");
        foerderart.setAnteilProzent(BigDecimal.valueOf(40));

        Foerderart foerderart2 = new Foerderart();
        foerderart2.setBezeichnung("AnteilBaugemeinschaft");
        foerderart2.setAnteilProzent(BigDecimal.valueOf(60));

        List<Foerderart> foerderarten = new ArrayList<>(Arrays.asList(foerderart, foerderart2));
        foerdermix.setFoerderarten(foerderarten);

        /*FoerdermixStamm foerdermixStamm = this.foerdermixStammRepository.findAll().stream().findFirst().get();
        Foerdermix foerder = foerdermixStamm.getFoerdermix();*/

        return foerdermix;
    }

    private static Bauvorhaben createBauvorhaben() {
        final Bauvorhaben bauvorhaben = new Bauvorhaben();
        bauvorhaben.setNameVorhaben("Bauvorhaben");
        bauvorhaben.setEigentuemer("Eigentümer Name");
        bauvorhaben.setGrundstuecksgroesse(1234L);
        bauvorhaben.setStandVorhaben(StandVorhaben.BAUGENEHMIGUNG_ERTEILT);
        bauvorhaben.setBauvorhabenNummer("12345");
        final Wgs84 coordinate = new Wgs84();
        coordinate.setLatitude(48.1741495213055);
        coordinate.setLongitude(11.5352020030272);
        bauvorhaben.setAdresse(new Adresse("Agnes-Pockels-Bogen", "21", "80992", "München", coordinate));
        bauvorhaben.setBebauungsplannummer("13579");
        bauvorhaben.setFisNummer("1A3D5H");

        bauvorhaben.setAnmerkung("Bitte überprüft den Eigentümer und die Ortsangabe");
        bauvorhaben.setSobonRelevant(UncertainBoolean.TRUE);
        bauvorhaben.setPlanungsrecht(Planungsrecht.SONSTIGES_PARAG_165);
        bauvorhaben.setArtFnp(List.of(BaugebietTyp.WA, BaugebietTyp.MK));

        return bauvorhaben;
    }


    private static List<FoerdermixStamm> createFoerdermixStaemme() {
        final List<FoerdermixStamm> returnListe = new ArrayList<>();

        final FoerdermixStamm foerdermixStamm2021 = new FoerdermixStamm();
        foerdermixStamm2021.setBezeichnungJahr("SoBoN 2021");
        foerdermixStamm2021.setBezeichnung("Bebauungsplan - SoBoN-Ursächlichkeit: Grundmodell (20% PMB;SoBoN 2021)");

        final Foerdermix foerdermix = new Foerdermix();
        Foerderart foerderart = new Foerderart();
        foerderart.setBezeichnung("AnteilMuenchenModell");
        foerderart.setAnteilProzent(BigDecimal.valueOf(40));

        Foerderart foerderart2 = new Foerderart();
        foerderart2.setBezeichnung("AnteilBaugemeinschaft");
        foerderart2.setAnteilProzent(BigDecimal.valueOf(60));

        List<Foerderart> foerderarten = new ArrayList<>(Arrays.asList(foerderart, foerderart2));
        foerdermix.setFoerderarten(foerderarten);

        foerdermixStamm2021.setFoerdermix(foerdermix);
        returnListe.add(foerdermixStamm2021);

        final FoerdermixStamm secondFoerdermixStamm2021 = new FoerdermixStamm();
        secondFoerdermixStamm2021.setBezeichnungJahr("SoBoN 2021");
        secondFoerdermixStamm2021.setBezeichnung("Bebauungsplan - SoBoN-Ursächlichkeit: 0 % PMB (SoBoN 2021)");

        final Foerdermix foerdermix2 = new Foerdermix();
        Foerderart foerderart21 = new Foerderart();
        foerderart21.setBezeichnung("AnteilBaugemeinschaften");
        foerderart21.setAnteilProzent(BigDecimal.valueOf(69.50));

        Foerderart foerderart22 = new Foerderart();
        foerderart22.setBezeichnung("AnteilFreifinanzierterGeschosswohnungsbau");
        foerderart22.setAnteilProzent(BigDecimal.valueOf(30.50));

        List<Foerderart> foerderarten2 = new ArrayList<>(Arrays.asList(foerderart21, foerderart22));
        foerdermix2.setFoerderarten(foerderarten2);

        secondFoerdermixStamm2021.setFoerdermix(foerdermix2);
        returnListe.add(secondFoerdermixStamm2021);


        final FoerdermixStamm foerdermixStamm2017 = new FoerdermixStamm();
        foerdermixStamm2017.setBezeichnungJahr("SoBoN 2017");
        foerdermixStamm2017.setBezeichnung("Bebauungsplan - SoBoN-Ursächlichkeit: SoBoN 2017");

        final Foerdermix foerdermix3 = new Foerdermix();
        Foerderart foerderart31 = new Foerderart();
        foerderart31.setBezeichnung("FreifinanzierterGeschosswohnungsbau");
        foerderart31.setAnteilProzent(BigDecimal.valueOf(10));

        Foerderart foerderart32 = new Foerderart();
        foerderart32.setBezeichnung("GefoerderterMietwohnungsbau");
        foerderart32.setAnteilProzent(BigDecimal.valueOf(20));

        Foerderart foerderart33 = new Foerderart();
        foerderart33.setBezeichnung("MuenchenModell");
        foerderart33.setAnteilProzent(BigDecimal.valueOf(10));

        Foerderart foerderart34 = new Foerderart();
        foerderart34.setBezeichnung("PreisgedaempfterMietwohnungsbau");
        foerderart34.setAnteilProzent(BigDecimal.valueOf(10));

        Foerderart foerderart35 = new Foerderart();
        foerderart35.setBezeichnung("KonzeptionellerMietwohnungsbau");
        foerderart35.setAnteilProzent(BigDecimal.valueOf(10));

        Foerderart foerderart36 = new Foerderart();
        foerderart36.setBezeichnung("Baugemeinschaften");
        foerderart36.setAnteilProzent(BigDecimal.valueOf(10));

        Foerderart foerderart37 = new Foerderart();
        foerderart37.setBezeichnung("EinUndZweifamilienhaeuser");
        foerderart37.setAnteilProzent(BigDecimal.valueOf(30));

        List<Foerderart> foerderarten3 = new ArrayList<>(Arrays.asList(foerderart31, foerderart32,foerderart33, foerderart34,foerderart35,foerderart36,foerderart37));
        foerdermix3.setFoerderarten(foerderarten3);

        foerdermixStamm2017.setFoerdermix(foerdermix3);
        returnListe.add(foerdermixStamm2017);


        final FoerdermixStamm foerdermixStammWeitere = new FoerdermixStamm();
        foerdermixStammWeitere.setBezeichnungJahr("Weitere");
        foerdermixStammWeitere.setBezeichnung("Nachverdichtung (§34 und §35 BauGB)");

        final Foerdermix foerdermix4 = new Foerdermix();
        Foerderart foerderart41 = new Foerderart();
        foerderart41.setBezeichnung("AnteilPreisgedaempfterMietwohnungsbau");
        foerderart41.setAnteilProzent(BigDecimal.valueOf(42));

        Foerderart foerderart42 = new Foerderart();
        foerderart42.setBezeichnung("AnteilKonzeptionellerMietwohnungsbau");
        foerderart42.setAnteilProzent(BigDecimal.valueOf(58));

        List<Foerderart> foerderarten4 = new ArrayList<>(Arrays.asList(foerderart41, foerderart42));
        foerdermix4.setFoerderarten(foerderarten4);

        foerdermixStammWeitere.setFoerdermix(foerdermix4);
        returnListe.add(foerdermixStammWeitere);

        return returnListe;
    }

    private void addInfrastruktureinrichtungen() {
        this.addKinderkrippe();
        this.addKindergarten();
        this.addHausFuerKinder();
        this.addGsNachmittagBetreuung_1();
        this.addGsNachmittagBetreuung_2();
        this.addGrundschule();
        this.addMittelschule();
    }

    private void addKinderkrippe() {
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        final Kinderkrippe kinderkrippe = new Kinderkrippe();

        kinderkrippe.setInfrastruktureinrichtung(infrastruktureinrichtung);
        infrastruktureinrichtung.setNameEinrichtung("Kinderkrippe der Marienkäfer");
        infrastruktureinrichtung.setFertigstellungsjahr(1989);
        infrastruktureinrichtung.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        infrastruktureinrichtung.setAdresse(new Adresse());
        infrastruktureinrichtung.getAdresse().setStrasse("Maximilianstraße");
        infrastruktureinrichtung.getAdresse().setHausnummer("1");
        infrastruktureinrichtung.setEinrichtungstraeger(Einrichtungstraeger.KITA_STAEDTISCHE_EINRICHTUNG);
        infrastruktureinrichtung.setBauvorhaben(this.addBauvorhaben3());
        infrastruktureinrichtung.setFlaecheGesamtgrundstueck(BigDecimal.valueOf(1200));
        infrastruktureinrichtung.setFlaecheTeilgrundstueck(BigDecimal.valueOf(450));

        kinderkrippe.setAnzahlKinderkrippePlaetze(15);
        kinderkrippe.setAnzahlKinderkrippeGruppen(3);
        kinderkrippe.setWohnungsnaheKinderkrippePlaetze(5);

        this.kinderkrippeRepository.save(kinderkrippe);
    }

    private void addKindergarten() {
        var infrastruktureinrichtung = new Infrastruktureinrichtung();
        var kindergarten = new Kindergarten();

        kindergarten.setInfrastruktureinrichtung(infrastruktureinrichtung);
        infrastruktureinrichtung.setNameEinrichtung("Kindergarten Maikäfer");
        infrastruktureinrichtung.setFertigstellungsjahr(1995);
        infrastruktureinrichtung.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        infrastruktureinrichtung.setAdresse(new Adresse());
        infrastruktureinrichtung.getAdresse().setStrasse("Bad Schachener Straße");
        infrastruktureinrichtung.getAdresse().setHausnummer("65");
        infrastruktureinrichtung.setEinrichtungstraeger(Einrichtungstraeger.KITA_EINRICHTUNG_GESAMTSTAEDTISCH);
        infrastruktureinrichtung.setFlaecheGesamtgrundstueck(BigDecimal.valueOf(512));
        infrastruktureinrichtung.setFlaecheTeilgrundstueck(BigDecimal.valueOf(128));

        kindergarten.setAnzahlKindergartenPlaetze(25);
        kindergarten.setAnzahlKindergartenGruppen(5);
        kindergarten.setWohnungsnaheKindergartenPlaetze(10);

        this.kindergartenRepository.save(kindergarten);

        infrastruktureinrichtung = new Infrastruktureinrichtung();
        kindergarten = new Kindergarten();

        kindergarten.setInfrastruktureinrichtung(infrastruktureinrichtung);
        infrastruktureinrichtung.setNameEinrichtung("Kindergarten Biene Maja");
        infrastruktureinrichtung.setFertigstellungsjahr(1998);
        infrastruktureinrichtung.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        infrastruktureinrichtung.setAdresse(new Adresse());
        infrastruktureinrichtung.getAdresse().setStrasse("Theresienstraße");
        infrastruktureinrichtung.getAdresse().setHausnummer("15");
        infrastruktureinrichtung.setEinrichtungstraeger(Einrichtungstraeger.KITA_STAEDTISCHE_EINRICHTUNG);
        infrastruktureinrichtung.setFlaecheGesamtgrundstueck(BigDecimal.valueOf(256));
        infrastruktureinrichtung.setFlaecheTeilgrundstueck(BigDecimal.valueOf(64));

        kindergarten.setAnzahlKindergartenPlaetze(18);
        kindergarten.setAnzahlKindergartenGruppen(3);
        kindergarten.setWohnungsnaheKindergartenPlaetze(5);

        this.kindergartenRepository.save(kindergarten);
    }

    private void addHausFuerKinder() {
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        final HausFuerKinder hausFuerKinder = new HausFuerKinder();

        hausFuerKinder.setInfrastruktureinrichtung(infrastruktureinrichtung);
        infrastruktureinrichtung.setNameEinrichtung("Haus für Kinder Bogenhausen");
        infrastruktureinrichtung.setFertigstellungsjahr(2025);
        infrastruktureinrichtung.setStatus(StatusInfrastruktureinrichtung.UNGESICHERTE_PLANUNG);
        infrastruktureinrichtung.setAllgemeineOrtsangabe("München Bogenhausen, Törringstraße");
        infrastruktureinrichtung.setEinrichtungstraeger(Einrichtungstraeger.KITA_EINRICHTUNG_GESAMTSTAEDTISCH);
        infrastruktureinrichtung.setBauvorhaben(this.addBauvorhaben4());
        infrastruktureinrichtung.setFlaecheGesamtgrundstueck(BigDecimal.valueOf(1024));
        infrastruktureinrichtung.setFlaecheTeilgrundstueck(BigDecimal.valueOf(512));

        hausFuerKinder.setAnzahlKinderkrippePlaetze(30);
        hausFuerKinder.setAnzahlKinderkrippeGruppen(7);
        hausFuerKinder.setAnzahlKindergartenPlaetze(8);
        hausFuerKinder.setAnzahlKindergartenGruppen(1);
        hausFuerKinder.setAnzahlHortPlaetze(6);
        hausFuerKinder.setAnzahlHortGruppen(2);
        hausFuerKinder.setWohnungsnaheKindergartenPlaetze(10);

        this.hausFuerKinderRepository.save(hausFuerKinder);
    }

    private void addGsNachmittagBetreuung_1() {
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        final GsNachmittagBetreuung gsNachmittagBetreuung = new GsNachmittagBetreuung();


        infrastruktureinrichtung.setNameEinrichtung("Nachmittagsbetreuung für Grundschulkinder Glühwürmchen");
        infrastruktureinrichtung.setFertigstellungsjahr(2023);
        infrastruktureinrichtung.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR);
        infrastruktureinrichtung.setAllgemeineOrtsangabe("München Johanneskirchen, Johanneskirchner Straße");
        infrastruktureinrichtung.setEinrichtungstraeger(Einrichtungstraeger.SCHULE_KIRCHLICHE_TRAEGERSCHAFT);
        gsNachmittagBetreuung.setInfrastruktureinrichtung(infrastruktureinrichtung);

        gsNachmittagBetreuung.setArtGsNachmittagBetreuung(ArtGsNachmittagBetreuung.KOOPERATIVER_GANZTAG_RHYTHMISIERTE_VARIANTE);
        gsNachmittagBetreuung.setAnzahlHortPlaetze(150);
        gsNachmittagBetreuung.setAnzahlHortGruppen(10);


        this.gsNachmittagBetreuungRepository.save(gsNachmittagBetreuung);
    }

    private void addGsNachmittagBetreuung_2() {
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        final GsNachmittagBetreuung gsNachmittagBetreuung = new GsNachmittagBetreuung();


        infrastruktureinrichtung.setNameEinrichtung("Nachmittagsbetreuung für Grundschulkinder Maikglöckchen");
        infrastruktureinrichtung.setFertigstellungsjahr(2023);
        infrastruktureinrichtung.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR);
        infrastruktureinrichtung.setAllgemeineOrtsangabe("München Bogenhausen, Mauerkircherstraße");
        infrastruktureinrichtung.setEinrichtungstraeger(Einrichtungstraeger.GS_BETREUUNG_ELTERN_KIND_INITIATIVE);
        gsNachmittagBetreuung.setInfrastruktureinrichtung(infrastruktureinrichtung);

        gsNachmittagBetreuung.setAnzahlHortPlaetze(100);
        gsNachmittagBetreuung.setAnzahlHortGruppen(4);

        this.gsNachmittagBetreuungRepository.save(gsNachmittagBetreuung);
    }

    private void addGrundschule() {
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        final Grundschule grundschule = new Grundschule();

        grundschule.setInfrastruktureinrichtung(infrastruktureinrichtung);
        infrastruktureinrichtung.setNameEinrichtung("Käthe-Kollwitz Grundschule");
        infrastruktureinrichtung.setFertigstellungsjahr(2026);
        infrastruktureinrichtung.setStatus(StatusInfrastruktureinrichtung.UNGESICHERTE_PLANUNG);
        infrastruktureinrichtung.setEinrichtungstraeger(Einrichtungstraeger.SCHULE_STAATLICHE_EINRICHTUNG);

        grundschule.setSchule(new Schule());
        grundschule.getSchule().setAnzahlKlassen(4);
        grundschule.getSchule().setAnzahlPlaetze(60);

        this.grundschuleRepository.save(grundschule);
    }

    private void addMittelschule() {
        final Infrastruktureinrichtung infrastruktureinrichtung = new Infrastruktureinrichtung();
        final Mittelschule mittelschule = new Mittelschule();

        mittelschule.setInfrastruktureinrichtung(infrastruktureinrichtung);
        infrastruktureinrichtung.setNameEinrichtung("Theodor-Heuss Realschule");
        infrastruktureinrichtung.setFertigstellungsjahr(1999);
        infrastruktureinrichtung.setAdresse(new Adresse());
        infrastruktureinrichtung.getAdresse().setStrasse("Reichenbachstraße");
        infrastruktureinrichtung.getAdresse().setHausnummer("4");
        infrastruktureinrichtung.getAdresse().setPlz("80469");
        infrastruktureinrichtung.getAdresse().setOrt("München");
        final Wgs84 coordinate = new Wgs84();
        coordinate.setLatitude(48.1337919447642);
        coordinate.setLongitude(11.5766207727739);
        infrastruktureinrichtung.getAdresse().setCoordinate(coordinate);
        infrastruktureinrichtung.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        infrastruktureinrichtung.setEinrichtungstraeger(Einrichtungstraeger.SCHULE_STAEDTISCHE_EINRICHTUNG);

        mittelschule.setSchule(new Schule());
        mittelschule.getSchule().setAnzahlKlassen(5);
        mittelschule.getSchule().setAnzahlPlaetze(85);

        this.mittelschuleRepository.save(mittelschule);
    }

    public void createIdealtypischeBauraten() {
        var jahresrate1 = new Jahresrate();
        jahresrate1.setJahr(1);
        jahresrate1.setRate(BigDecimal.valueOf(0.105));
        var jahresrate2 = new Jahresrate();
        jahresrate2.setJahr(2);
        jahresrate2.setRate(BigDecimal.valueOf(0.105));
        var jahresrate3 = new Jahresrate();
        jahresrate3.setJahr(3);
        jahresrate3.setRate(BigDecimal.valueOf(0.25));
        var jahresrate4 = new Jahresrate();
        jahresrate4.setJahr(4);
        jahresrate4.setRate(BigDecimal.valueOf(0.25));
        var jahresrate5 = new Jahresrate();
        jahresrate5.setJahr(5);
        jahresrate5.setRate(BigDecimal.valueOf(0.29));


        final var idealtypischeBaurate = new IdealtypischeBaurate();
        idealtypischeBaurate.setTyp(IdealtypischeBaurateTyp.WOHNEINHEITEN);
        idealtypischeBaurate.setVon(BigDecimal.ZERO);
        idealtypischeBaurate.setBisExklusiv(BigDecimal.valueOf(1500));
        idealtypischeBaurate.setJahresraten(List.of(
                jahresrate1,
                jahresrate2,
                jahresrate3,
                jahresrate4,
                jahresrate5
        ));
        idealtypischeBaurateRepository.saveAndFlush(idealtypischeBaurate);
    }

}