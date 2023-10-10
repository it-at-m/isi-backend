/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaugebietModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.common.FlurstueckModel;
import de.muenchen.isi.domain.model.common.GemarkungModel;
import de.muenchen.isi.domain.model.common.MultiPolygonGeometryModel;
import de.muenchen.isi.domain.model.common.StadtbezirkModel;
import de.muenchen.isi.domain.model.common.VerortungModel;
import de.muenchen.isi.domain.model.common.WGS84Model;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtBaulicheNutzung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtDokument;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
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

    public static InfrastrukturabfrageModel createInfrastrukturabfrageModel() {
        final InfrastrukturabfrageModel infrastrukturabfrage = new InfrastrukturabfrageModel();

        final AbfrageModel abfrage = new AbfrageModel();
        final WGS84Model coordinate = new WGS84Model();
        coordinate.setLatitude(48.1556795465256);
        coordinate.setLongitude(11.5568456350688);
        abfrage.setAdresse(new AdresseModel("80331", "München", "Lothstraße", "7", coordinate));
        abfrage.setAllgemeineOrtsangabe("12345 Musterort, Musterstraße 2");
        abfrage.setFristStellungnahme(LocalDate.of(2022, 12, 31));
        abfrage.setAnmerkung("Bitte die Abfrage zeitnah behandeln");
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        abfrage.setNameAbfrage("Neubausiedlung in Musterort");
        abfrage.setStandVorhaben(StandVorhaben.BAUANTRAG_EINGEREICHT);
        abfrage.setVerortung(createVerortung());
        infrastrukturabfrage.setAbfrage(abfrage);

        infrastrukturabfrage.setSobonRelevant(UncertainBoolean.TRUE);
        infrastrukturabfrage.setSobonJahr(SobonVerfahrensgrundsaetzeJahr.JAHR_2021);
        infrastrukturabfrage.setAktenzeichenProLbk("PRO12345");
        infrastrukturabfrage.setOffiziellerVerfahrensschritt(UncertainBoolean.FALSE);

        final AbfragevarianteModel abfragevariante = createAbfragevarianteModel();
        infrastrukturabfrage.setAbfragevarianten(List.of(abfragevariante));

        return infrastrukturabfrage;
    }

    public static AbfragevarianteModel createAbfragevarianteModel() {
        final AbfragevarianteModel original = new AbfragevarianteModel();
        original.setAbfragevariantenNr(1);
        original.setAbfragevariantenName("Dorf");
        original.setPlanungsrecht(Planungsrecht.BPLAN_PARAG_30);
        original.setGeschossflaecheWohnen(new BigDecimal("1234.56"));
        original.setGesamtanzahlWe(31);
        original.setRealisierungVon(2023);
        original.setSonderwohnformen(false);
        original.setBauabschnitte(List.of(createBauabschnittModel()));

        return original;
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
        baugebiet.setBaugebietArt(ArtBaulicheNutzung.WA);
        baugebiet.setTechnical(false);
        baugebiet.setRealisierungVon(2023);
        baugebiet.setBauraten(List.of(createBaurateModel()));
        return baugebiet;
    }

    private static BaurateModel createBaurateModel() {
        final BaurateModel baurate = new BaurateModel();
        baurate.setJahr(2022);
        baurate.setAnzahlWeGeplant(10);
        baurate.setGeschossflaecheWohnenGeplant(BigDecimal.valueOf(15.55));
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

    public static VerortungModel createVerortung() {
        final var verortung = new VerortungModel();
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
