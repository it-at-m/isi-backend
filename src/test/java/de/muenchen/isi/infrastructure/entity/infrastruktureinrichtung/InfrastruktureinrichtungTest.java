package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.infrastructure.entity.common.BearbeitendePerson;
import org.junit.jupiter.api.Test;

class InfrastruktureinrichtungTest {

    @Test
    void getEmailBearbeitendePerson() {
        final var kinderkrippe = new Kinderkrippe();
        final var bearbeitendePerson = new BearbeitendePerson();
        bearbeitendePerson.setEmail("Bearbeiter@Muenchen.de");
        kinderkrippe.setBearbeitendePerson(bearbeitendePerson);

        assertThat(kinderkrippe.getEmailBearbeitendePerson(), is("bearbeiter@muenchen.de"));
    }

    @Test
    void getEmailBearbeitendePersonOhneBearbeitendePerson() {
        final var kinderkrippe = new Kinderkrippe();
        assertThat(kinderkrippe.getEmailBearbeitendePerson(), is(nullValue()));

        kinderkrippe.setBearbeitendePerson(new BearbeitendePerson());
        assertThat(kinderkrippe.getEmailBearbeitendePerson(), is(nullValue()));
    }
}
