package de.muenchen.isi.infrastructure.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.infrastructure.entity.common.BearbeitendePerson;
import de.muenchen.isi.infrastructure.entity.common.Bearbeitungshistorie;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class AbfrageTest {

    @Test
    void getEmailSachbearbeitungOhneBearbeitungshistorie() {
        final var abfrage = new Bauleitplanverfahren();
        assertThat(abfrage.getEmailSachbearbeitung(), is(nullValue()));

        abfrage.setBearbeitungshistorie(null);
        assertThat(abfrage.getEmailSachbearbeitung(), is(nullValue()));
    }

    @Test
    void getEmailSachbearbeitungNochNichtUebernommen() {
        final var abfrage = new Bauleitplanverfahren();
        addBearbeitungshistorie(abfrage, StatusAbfrage.ANGELEGT, "ersteller@muenchen.de");
        addBearbeitungshistorie(abfrage, StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG, "ersteller@muenchen.de");

        assertThat(abfrage.getEmailSachbearbeitung(), is(nullValue()));
    }

    @Test
    void getEmailSachbearbeitungUebernommen() {
        final var abfrage = new Bauleitplanverfahren();
        addBearbeitungshistorie(abfrage, StatusAbfrage.ANGELEGT, "ersteller@muenchen.de");
        addBearbeitungshistorie(abfrage, StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG, "ersteller@muenchen.de");
        addBearbeitungshistorie(abfrage, StatusAbfrage.START_BEARBEITUNG, "Sachbearbeitung@Muenchen.de");
        addBearbeitungshistorie(abfrage, StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG, "andere@muenchen.de");

        assertThat(abfrage.getEmailSachbearbeitung(), is("sachbearbeitung@muenchen.de"));
    }

    @Test
    void getEmailSachbearbeitungIgnoriertAndereUebergaengeNachStartBearbeitung() {
        final var abfrage = new Bauleitplanverfahren();
        addBearbeitungshistorie(abfrage, StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG, "ersteller@muenchen.de");
        addBearbeitungshistorie(abfrage, StatusAbfrage.START_BEARBEITUNG, "sachbearbeitung@muenchen.de");
        addBearbeitungshistorie(abfrage, StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG, "sachbearbeitung@muenchen.de");
        // Rückgabe an die Sachbearbeitung durch eine andere Person
        addBearbeitungshistorie(abfrage, StatusAbfrage.START_BEARBEITUNG, "bedarfsmeldung@muenchen.de");

        assertThat(abfrage.getEmailSachbearbeitung(), is("sachbearbeitung@muenchen.de"));
    }

    @Test
    void getEmailSachbearbeitungLetzteUebernahmeGilt() {
        final var abfrage = new Bauleitplanverfahren();
        addBearbeitungshistorie(abfrage, StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG, "ersteller@muenchen.de");
        addBearbeitungshistorie(abfrage, StatusAbfrage.START_BEARBEITUNG, "sachbearbeitung@muenchen.de");
        addBearbeitungshistorie(abfrage, StatusAbfrage.ANGELEGT, "sachbearbeitung@muenchen.de");
        addBearbeitungshistorie(abfrage, StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG, "ersteller@muenchen.de");
        addBearbeitungshistorie(abfrage, StatusAbfrage.START_BEARBEITUNG, "vertretung@muenchen.de");

        assertThat(abfrage.getEmailSachbearbeitung(), is("vertretung@muenchen.de"));
    }

    @Test
    void getEmailSachbearbeitungOhneBearbeitendePerson() {
        final var abfrage = new Bauleitplanverfahren();
        addBearbeitungshistorie(abfrage, StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG, "ersteller@muenchen.de");
        final var eintrag = new Bearbeitungshistorie();
        eintrag.setZielStatus(StatusAbfrage.START_BEARBEITUNG);
        eintrag.setZeitpunkt(LocalDateTime.now());
        abfrage.getBearbeitungshistorie().add(eintrag);

        assertThat(abfrage.getEmailSachbearbeitung(), is(nullValue()));
    }

    private static void addBearbeitungshistorie(final Abfrage abfrage, final StatusAbfrage status, final String email) {
        if (abfrage.getBearbeitungshistorie() == null) {
            abfrage.setBearbeitungshistorie(new ArrayList<>());
        }
        final var bearbeitendePerson = new BearbeitendePerson();
        bearbeitendePerson.setEmail(email);
        final var eintrag = new Bearbeitungshistorie();
        eintrag.setZielStatus(status);
        eintrag.setZeitpunkt(LocalDateTime.now());
        eintrag.setBearbeitendePerson(bearbeitendePerson);
        abfrage.getBearbeitungshistorie().add(eintrag);
    }
}
