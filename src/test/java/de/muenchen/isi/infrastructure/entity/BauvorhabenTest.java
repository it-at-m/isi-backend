package de.muenchen.isi.infrastructure.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.infrastructure.entity.common.BearbeitendePerson;
import org.junit.jupiter.api.Test;

class BauvorhabenTest {

    @Test
    void getEmailBearbeitendePerson() {
        final var bauvorhaben = new Bauvorhaben();
        final var bearbeitendePerson = new BearbeitendePerson();
        bearbeitendePerson.setEmail("Bearbeiter@Muenchen.de");
        bauvorhaben.setBearbeitendePerson(bearbeitendePerson);

        assertThat(bauvorhaben.getEmailBearbeitendePerson(), is("bearbeiter@muenchen.de"));
    }

    @Test
    void getEmailBearbeitendePersonOhneBearbeitendePerson() {
        final var bauvorhaben = new Bauvorhaben();
        assertThat(bauvorhaben.getEmailBearbeitendePerson(), is(nullValue()));

        bauvorhaben.setBearbeitendePerson(new BearbeitendePerson());
        assertThat(bauvorhaben.getEmailBearbeitendePerson(), is(nullValue()));
    }
}
