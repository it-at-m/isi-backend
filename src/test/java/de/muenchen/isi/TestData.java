/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.isi.domain.model.AbfragevarianteBaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteWeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaugebietModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBaugenehmigungsverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteWeiteresVerfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BaugenehmigungsverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.WeiteresVerfahrenAngelegtModel;
import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.common.FlurstueckModel;
import de.muenchen.isi.domain.model.common.GemarkungModel;
import de.muenchen.isi.domain.model.common.MultiPolygonGeometryModel;
import de.muenchen.isi.domain.model.common.StadtbezirkModel;
import de.muenchen.isi.domain.model.common.VerortungMultiPolygonModel;
import de.muenchen.isi.domain.model.common.WGS84Model;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtBaulicheNutzung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtDokument;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import de.muenchen.isi.infrastructure.entity.filehandling.Filepath;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;

public class TestData {

    public static BauleitplanverfahrenModel createBauleitplanverfahrenModel() {
        final var bauleitplanverfahren = new BauleitplanverfahrenModel();
        bauleitplanverfahren.setName("Neubausiedlung in Musterort");
        bauleitplanverfahren.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        final WGS84Model coordinate = new WGS84Model();
        coordinate.setLatitude(48.1556795465256);
        coordinate.setLongitude(11.5568456350688);
        bauleitplanverfahren.setAdresse(new AdresseModel("80331", "München", "Lothstraße", "7", coordinate, null));
        bauleitplanverfahren.setFristBearbeitung(LocalDate.of(2022, 12, 31));
        bauleitplanverfahren.setAnmerkung("Bitte die Abfrage zeitnah behandeln");
        bauleitplanverfahren.setStandVerfahren(StandVerfahren.STRUKTURKONZEPT);
        bauleitplanverfahren.setVerortung(createVerortung());
        bauleitplanverfahren.setSobonRelevant(UncertainBoolean.TRUE);
        bauleitplanverfahren.setSobonJahr(SobonVerfahrensgrundsaetzeJahr.JAHR_2021);
        bauleitplanverfahren.setOffizielleMitzeichnung(UncertainBoolean.TRUE);
        bauleitplanverfahren.setOffizielleMitzeichnung(UncertainBoolean.FALSE);
        bauleitplanverfahren.setAbfragevariantenBauleitplanverfahren(
            List.of(createAbfragevarianteBauleitplanverfahrenModel())
        );
        return bauleitplanverfahren;
    }

    public static BaugenehmigungsverfahrenModel createBaugenehmigungsverfahrenModel() {
        final var baugenehmigungsverfahren = new BaugenehmigungsverfahrenModel();
        baugenehmigungsverfahren.setName("Altbausiedlung in Musterort");
        baugenehmigungsverfahren.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        final WGS84Model coordinate = new WGS84Model();
        coordinate.setLatitude(48.1556795465256);
        coordinate.setLongitude(11.5568456350688);
        baugenehmigungsverfahren.setAdresse(new AdresseModel("80331", "München", "Lothstraße", "7", coordinate, null));
        baugenehmigungsverfahren.setFristBearbeitung(LocalDate.of(2022, 12, 31));
        baugenehmigungsverfahren.setAnmerkung("Bitte die Abfrage zeitnah behandeln");
        baugenehmigungsverfahren.setStandVerfahren(StandVerfahren.STRUKTURKONZEPT);
        baugenehmigungsverfahren.setVerortung(createVerortung());
        baugenehmigungsverfahren.setAbfragevariantenBaugenehmigungsverfahren(
            List.of(createAbfragevarianteBaugenehmigungsverfahrenModel())
        );
        return baugenehmigungsverfahren;
    }

    public static WeiteresVerfahrenModel createWeiteresVerfahrenModel() {
        final var weiteresVerfahren = new WeiteresVerfahrenModel();
        weiteresVerfahren.setName("Überbausiedlung in Musterort");
        weiteresVerfahren.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        final WGS84Model coordinate = new WGS84Model();
        coordinate.setLatitude(48.1556795465256);
        coordinate.setLongitude(11.5568456350688);
        weiteresVerfahren.setAdresse(new AdresseModel("80331", "München", "Lothstraße", "7", coordinate, null));
        weiteresVerfahren.setFristBearbeitung(LocalDate.of(2022, 12, 31));
        weiteresVerfahren.setAnmerkung("Bitte die Abfrage zeitnah behandeln");
        weiteresVerfahren.setStandVerfahren(StandVerfahren.STRUKTURKONZEPT);
        weiteresVerfahren.setVerortung(createVerortung());
        weiteresVerfahren.setSobonRelevant(UncertainBoolean.TRUE);
        weiteresVerfahren.setSobonJahr(SobonVerfahrensgrundsaetzeJahr.JAHR_2021);
        weiteresVerfahren.setOffizielleMitzeichnung(UncertainBoolean.TRUE);
        weiteresVerfahren.setOffizielleMitzeichnung(UncertainBoolean.FALSE);
        weiteresVerfahren.setAbfragevariantenWeiteresVerfahren(List.of(createAbfragevarianteWeiteresVerfahrenModel()));
        return weiteresVerfahren;
    }

    public static BauleitplanverfahrenAngelegtModel createBauleitplanverfahrenAngelegtModel() {
        final var bauleitplanverfahren = new BauleitplanverfahrenAngelegtModel();
        bauleitplanverfahren.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        bauleitplanverfahren.setName("Neubausiedlung in Musterort 2");
        final WGS84Model coordinate = new WGS84Model();
        coordinate.setLatitude(48.1556795465256);
        coordinate.setLongitude(11.5568456350688);
        bauleitplanverfahren.setAdresse(new AdresseModel("80331", "München", "Lothstraße", "7", coordinate, null));
        bauleitplanverfahren.setFristBearbeitung(LocalDate.of(2022, 12, 31));
        bauleitplanverfahren.setAnmerkung("Bitte die Abfrage zeitnah behandeln");
        bauleitplanverfahren.setStandVerfahren(StandVerfahren.STRUKTURKONZEPT);
        bauleitplanverfahren.setVerortung(createVerortung());
        bauleitplanverfahren.setSobonRelevant(UncertainBoolean.FALSE);
        bauleitplanverfahren.setOffizielleMitzeichnung(UncertainBoolean.FALSE);
        bauleitplanverfahren.setAbfragevariantenBauleitplanverfahren(
            List.of(createAbfragevarianteBauleitplanverfahrenAngelegtModel())
        );
        return bauleitplanverfahren;
    }

    public static BaugenehmigungsverfahrenAngelegtModel createBaugenehmigungsverfahrenAngelegtModel() {
        final var baugenehmigungsverfahren = new BaugenehmigungsverfahrenAngelegtModel();
        baugenehmigungsverfahren.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        baugenehmigungsverfahren.setName("Altbausiedlung in Musterort 2");
        final WGS84Model coordinate = new WGS84Model();
        coordinate.setLatitude(48.1556795465256);
        coordinate.setLongitude(11.5568456350688);
        baugenehmigungsverfahren.setAdresse(new AdresseModel("80331", "München", "Lothstraße", "7", coordinate, null));
        baugenehmigungsverfahren.setFristBearbeitung(LocalDate.of(2022, 12, 31));
        baugenehmigungsverfahren.setAnmerkung("Bitte die Abfrage zeitnah behandeln");
        baugenehmigungsverfahren.setStandVerfahren(StandVerfahren.STRUKTURKONZEPT);
        baugenehmigungsverfahren.setVerortung(createVerortung());
        baugenehmigungsverfahren.setAbfragevariantenBaugenehmigungsverfahren(
            List.of(createAbfragevarianteBaugenehmigungsverfahrenAngelegtModel())
        );
        return baugenehmigungsverfahren;
    }

    public static WeiteresVerfahrenAngelegtModel createWeiteresVerfahrenAngelegtModel() {
        final var bauleitplanverfahren = new WeiteresVerfahrenAngelegtModel();
        bauleitplanverfahren.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        bauleitplanverfahren.setName("Überbausiedlung in Musterort 2");
        final WGS84Model coordinate = new WGS84Model();
        coordinate.setLatitude(48.1556795465256);
        coordinate.setLongitude(11.5568456350688);
        bauleitplanverfahren.setAdresse(new AdresseModel("80331", "München", "Lothstraße", "7", coordinate, null));
        bauleitplanverfahren.setFristBearbeitung(LocalDate.of(2022, 12, 31));
        bauleitplanverfahren.setAnmerkung("Bitte die Abfrage zeitnah behandeln");
        bauleitplanverfahren.setStandVerfahren(StandVerfahren.STRUKTURKONZEPT);
        bauleitplanverfahren.setVerortung(createVerortung());
        bauleitplanverfahren.setSobonRelevant(UncertainBoolean.FALSE);
        bauleitplanverfahren.setOffizielleMitzeichnung(UncertainBoolean.FALSE);
        bauleitplanverfahren.setAbfragevariantenWeiteresVerfahren(
            List.of(createAbfragevarianteWeiteresVerfahrenAngelegtModel())
        );
        return bauleitplanverfahren;
    }

    public static AbfragevarianteBauleitplanverfahrenModel createAbfragevarianteBauleitplanverfahrenModel() {
        final var abfragevariante = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante.setAbfragevariantenNr(1);
        abfragevariante.setName("Name Abfragevariante 10");
        abfragevariante.setWesentlicheRechtsgrundlage(
            List.of(WesentlicheRechtsgrundlage.EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30)
        );
        abfragevariante.setGfWohnenSonderwohnformen(false);
        abfragevariante.setGfWohnenGesamt(BigDecimal.valueOf(1234.56));
        abfragevariante.setWeSonderwohnformen(false);
        abfragevariante.setWeGesamt(31);
        abfragevariante.setRealisierungVon(2023);
        abfragevariante.setSobonOrientierungswertJahr(SobonOrientierungswertJahr.JAHR_2022);
        abfragevariante.setBauabschnitte(List.of(createBauabschnittModel()));
        return abfragevariante;
    }

    public static AbfragevarianteBaugenehmigungsverfahrenModel createAbfragevarianteBaugenehmigungsverfahrenModel() {
        final var abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevariante.setAbfragevariantenNr(1);
        abfragevariante.setName("Name Abfragevariante 11");
        abfragevariante.setWesentlicheRechtsgrundlage(
            List.of(WesentlicheRechtsgrundlage.EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30)
        );
        abfragevariante.setGfWohnenSonderwohnformen(false);
        abfragevariante.setGfWohnenGesamt(BigDecimal.valueOf(1234.56));
        abfragevariante.setWeSonderwohnformen(false);
        abfragevariante.setWeGesamt(31);
        abfragevariante.setRealisierungVon(2023);
        abfragevariante.setSobonOrientierungswertJahr(SobonOrientierungswertJahr.JAHR_2014);
        abfragevariante.setBauabschnitte(List.of(createBauabschnittModel()));
        return abfragevariante;
    }

    public static AbfragevarianteWeiteresVerfahrenModel createAbfragevarianteWeiteresVerfahrenModel() {
        final var abfragevariante = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevariante.setAbfragevariantenNr(1);
        abfragevariante.setName("Name Abfragevariante 9");
        abfragevariante.setWesentlicheRechtsgrundlage(
            List.of(WesentlicheRechtsgrundlage.EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30)
        );
        abfragevariante.setGfWohnenSonderwohnformen(false);
        abfragevariante.setGfWohnenGesamt(BigDecimal.valueOf(1234.56));
        abfragevariante.setWeSonderwohnformen(false);
        abfragevariante.setWeGesamt(31);
        abfragevariante.setRealisierungVon(2023);
        abfragevariante.setSobonOrientierungswertJahr(SobonOrientierungswertJahr.JAHR_2022);
        abfragevariante.setBauabschnitte(List.of(createBauabschnittModel()));
        return abfragevariante;
    }

    public static AbfragevarianteBauleitplanverfahrenAngelegtModel createAbfragevarianteBauleitplanverfahrenAngelegtModel() {
        final var abfragevariante = new AbfragevarianteBauleitplanverfahrenAngelegtModel();
        abfragevariante.setAbfragevariantenNr(1);
        abfragevariante.setName("Name Abfragevariante 102");
        abfragevariante.setWesentlicheRechtsgrundlage(
            List.of(WesentlicheRechtsgrundlage.EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30)
        );
        abfragevariante.setGfWohnenSonderwohnformen(false);
        abfragevariante.setGfWohnenGesamt(BigDecimal.valueOf(1234.56));
        abfragevariante.setWeSonderwohnformen(false);
        abfragevariante.setWeGesamt(31);
        abfragevariante.setRealisierungVon(2023);
        abfragevariante.setBauabschnitte(List.of(createBauabschnittModel()));
        return abfragevariante;
    }

    public static AbfragevarianteBaugenehmigungsverfahrenAngelegtModel createAbfragevarianteBaugenehmigungsverfahrenAngelegtModel() {
        final var abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenAngelegtModel();
        abfragevariante.setAbfragevariantenNr(1);
        abfragevariante.setName("Name Abfragevariante 112");
        abfragevariante.setWesentlicheRechtsgrundlage(
            List.of(WesentlicheRechtsgrundlage.EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30)
        );
        abfragevariante.setGfWohnenSonderwohnformen(false);
        abfragevariante.setGfWohnenGesamt(BigDecimal.valueOf(1234.56));
        abfragevariante.setWeSonderwohnformen(false);
        abfragevariante.setWeGesamt(31);
        abfragevariante.setRealisierungVon(2023);
        abfragevariante.setBauabschnitte(List.of(createBauabschnittModel()));
        return abfragevariante;
    }

    public static AbfragevarianteWeiteresVerfahrenAngelegtModel createAbfragevarianteWeiteresVerfahrenAngelegtModel() {
        final var abfragevariante = new AbfragevarianteWeiteresVerfahrenAngelegtModel();
        abfragevariante.setAbfragevariantenNr(1);
        abfragevariante.setName("Name Abfragevariante 92");
        abfragevariante.setWesentlicheRechtsgrundlage(
            List.of(WesentlicheRechtsgrundlage.EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30)
        );
        abfragevariante.setGfWohnenSonderwohnformen(false);
        abfragevariante.setGfWohnenGesamt(BigDecimal.valueOf(1234.56));
        abfragevariante.setWeSonderwohnformen(false);
        abfragevariante.setWeGesamt(31);
        abfragevariante.setRealisierungVon(2023);
        abfragevariante.setBauabschnitte(List.of(createBauabschnittModel()));
        return abfragevariante;
    }

    private static BauabschnittModel createBauabschnittModel() {
        final BauabschnittModel bauabschnitt = new BauabschnittModel();
        bauabschnitt.setBezeichnung("Der einzigartige Bauabschnitt");
        bauabschnitt.setTechnical(false);
        bauabschnitt.setBaugebiete(List.of(createBaugebietModel()));
        return bauabschnitt;
    }

    private static BaugebietModel createBaugebietModel() {
        final BaugebietModel baugebiet = new BaugebietModel();
        baugebiet.setBezeichnung("Das Baugebiet des einzigartigen Baubschnitts");
        baugebiet.setArtBaulicheNutzung(ArtBaulicheNutzung.WA);
        baugebiet.setTechnical(false);
        baugebiet.setRealisierungVon(2023);
        baugebiet.setBauraten(List.of(createBaurateModel()));
        return baugebiet;
    }

    private static BaurateModel createBaurateModel() {
        final BaurateModel baurate = new BaurateModel();
        baurate.setJahr(2022);
        baurate.setWeGeplant(10);
        baurate.setGfWohnenGeplant(BigDecimal.valueOf(15.55));
        baurate.setFoerdermix(createFoerdermixModel());
        return baurate;
    }

    private static FoerdermixModel createFoerdermixModel() {
        final FoerdermixModel foerdermix = new FoerdermixModel();
        FoerderartModel foerderart = new FoerderartModel();
        foerderart.setBezeichnung("AnteilFreifinanzierterGeschosswohnungsbau");
        foerderart.setAnteilProzent(BigDecimal.valueOf(60));

        FoerderartModel foerderart2 = new FoerderartModel();
        foerderart2.setBezeichnung("AnteilGefoerderterMietwohnungsbau");
        foerderart2.setAnteilProzent(BigDecimal.valueOf(40));

        List<FoerderartModel> foerderarten = new ArrayList<>(Arrays.asList(foerderart, foerderart2));
        foerdermix.setFoerderarten(foerderarten);
        foerdermix.setBezeichnung("Test Fördermix");
        foerdermix.setBezeichnungJahr("2023");
        return foerdermix;
    }

    public static Dokument createDokument(final String pathToFile, final ArtDokument artDokument) {
        final var dokument = new Dokument();
        final var filePath = new Filepath();
        filePath.setPathToFile(pathToFile);
        dokument.setFilePath(filePath);
        dokument.setArtDokument(artDokument);
        return dokument;
    }

    public static DokumentModel createDokumentModel(final String pathToFile, final ArtDokument artDokument) {
        final var dokument = new DokumentModel();
        final var filePath = new FilepathModel();
        filePath.setPathToFile(pathToFile);
        dokument.setFilePath(filePath);
        dokument.setArtDokument(artDokument);
        return dokument;
    }

    public static VerortungMultiPolygonModel createVerortung() {
        final var verortung = new VerortungMultiPolygonModel();
        verortung.setMultiPolygon(createMultipolygon());

        final var stadtbezirk1 = new StadtbezirkModel();
        stadtbezirk1.setMultiPolygon(createMultipolygon());
        stadtbezirk1.setName("Der Name1");
        stadtbezirk1.setNummer("01");
        final var stadtbezirk2 = new StadtbezirkModel();
        stadtbezirk2.setMultiPolygon(createMultipolygon());
        stadtbezirk2.setName("Der Name2");
        stadtbezirk2.setNummer("02");
        verortung.setStadtbezirke(Set.of(stadtbezirk1, stadtbezirk2));

        final var gemarkung = new GemarkungModel();
        gemarkung.setNummer(BigDecimal.TEN);
        gemarkung.setName("Der Gemarkungsname");
        gemarkung.setMultiPolygon(createMultipolygon());

        final var flurstueck = new FlurstueckModel();
        flurstueck.setGemarkungNummer(BigDecimal.ONE);
        flurstueck.setZaehler(1L);
        flurstueck.setNenner(2L);
        flurstueck.setMultiPolygon(createMultipolygon());
        gemarkung.setFlurstuecke(Set.of(flurstueck));

        verortung.setGemarkungen(Set.of(gemarkung));

        return verortung;
    }

    @SneakyThrows
    public static MultiPolygonGeometryModel createMultipolygon() {
        final var multipolygon =
            "{ \"type\": \"MultiPolygon\",\n" +
            "    \"coordinates\": [\n" +
            "        [\n" +
            "            [[40, 40], [20, 45], [45, 30], [40, 40]]\n" +
            "        ],\n" +
            "        [\n" +
            "            [[20, 35], [10, 30], [10, 10], [30, 5], [45, 20], [20, 35]],\n" +
            "            [[30, 20], [20, 15], [20, 25], [30, 20]]\n" +
            "        ]\n" +
            "    ]\n" +
            "}";
        return new ObjectMapper().readValue(multipolygon, MultiPolygonGeometryModel.class);
    }
}
