package de.muenchen.isi.infrastructure.entity.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class BearbeitungshistorieTest {

    @Test
    void getEmailsOhneHistorie() {
        assertThat(Bearbeitungshistorie.getEmails(null), is(empty()));
        assertThat(Bearbeitungshistorie.getEmails(new ArrayList<>()), is(empty()));
    }

    @Test
    void getEmailsEntferntDuplikateUndVereinheitlichtSchreibweise() {
        final var historie = List.of(
            eintrag("Ersteller@Muenchen.de"),
            eintrag("bearbeiter@muenchen.de"),
            eintrag("ersteller@muenchen.de")
        );

        assertThat(
            Bearbeitungshistorie.getEmails(historie),
            contains("ersteller@muenchen.de", "bearbeiter@muenchen.de")
        );
    }

    @Test
    void getEmailsIgnoriertEintraegeOhneEmail() {
        final var ohnePerson = new Bearbeitungshistorie();

        assertThat(
            Bearbeitungshistorie.getEmails(Arrays.asList(ohnePerson, eintrag(null), eintrag("  "))),
            is(empty())
        );
    }

    @Test
    void entitaetenIndexierenDieEmailsIhrerHistorie() {
        final var abfrage = new Bauleitplanverfahren();
        abfrage.getBearbeitungshistorie().add(eintrag("a@muenchen.de"));
        final var bauvorhaben = new Bauvorhaben();
        bauvorhaben.getBearbeitungshistorie().add(eintrag("b@muenchen.de"));
        final var einrichtung = new Kinderkrippe();
        einrichtung.getBearbeitungshistorie().add(eintrag("c@muenchen.de"));

        assertThat(abfrage.getEmailsBearbeitendePersonen(), contains("a@muenchen.de"));
        assertThat(bauvorhaben.getEmailsBearbeitendePersonen(), contains("b@muenchen.de"));
        assertThat(einrichtung.getEmailsBearbeitendePersonen(), contains("c@muenchen.de"));
    }

    private static Bearbeitungshistorie eintrag(final String email) {
        final var person = new BearbeitendePerson();
        person.setEmail(email);
        final var eintrag = new Bearbeitungshistorie();
        eintrag.setBearbeitendePerson(person);
        return eintrag;
    }
}
