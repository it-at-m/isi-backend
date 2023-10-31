package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.infrastruktureinrichtung.GrundschuleDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.GsNachmittagBetreuungDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.HausFuerKinderDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.KindergartenDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.KinderkrippeDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.MittelschuleDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WohnungsnahePlaetzeValidatorTest {

    private WohnungsnahePlaetzeValidator wohnungsnahePlaetzeValidator = new WohnungsnahePlaetzeValidator();

    @Test
    void isValidKinderkrippe() {
        assertThat(wohnungsnahePlaetzeValidator.isValid(null, null), is(true));

        final var kinderkrippe = new KinderkrippeDto();
        kinderkrippe.setAnzahlKinderkrippePlaetze(null);
        kinderkrippe.setWohnungsnaheKinderkrippePlaetze(null);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderkrippe, null), is(true));

        kinderkrippe.setAnzahlKinderkrippePlaetze(100);
        kinderkrippe.setWohnungsnaheKinderkrippePlaetze(null);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderkrippe, null), is(true));

        kinderkrippe.setAnzahlKinderkrippePlaetze(100);
        kinderkrippe.setWohnungsnaheKinderkrippePlaetze(0);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderkrippe, null), is(true));

        kinderkrippe.setAnzahlKinderkrippePlaetze(100);
        kinderkrippe.setWohnungsnaheKinderkrippePlaetze(99);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderkrippe, null), is(true));

        kinderkrippe.setAnzahlKinderkrippePlaetze(100);
        kinderkrippe.setWohnungsnaheKinderkrippePlaetze(100);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderkrippe, null), is(true));

        kinderkrippe.setAnzahlKinderkrippePlaetze(100);
        kinderkrippe.setWohnungsnaheKinderkrippePlaetze(101);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderkrippe, null), is(false));

        kinderkrippe.setAnzahlKinderkrippePlaetze(null);
        kinderkrippe.setWohnungsnaheKinderkrippePlaetze(101);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderkrippe, null), is(false));

        kinderkrippe.setAnzahlKinderkrippePlaetze(0);
        kinderkrippe.setWohnungsnaheKinderkrippePlaetze(101);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderkrippe, null), is(false));
    }

    @Test
    void isValidKindergarten() {
        assertThat(wohnungsnahePlaetzeValidator.isValid(null, null), is(true));

        final var kinderarten = new KindergartenDto();
        kinderarten.setAnzahlKindergartenPlaetze(null);
        kinderarten.setWohnungsnaheKindergartenPlaetze(null);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderarten, null), is(true));

        kinderarten.setAnzahlKindergartenPlaetze(100);
        kinderarten.setWohnungsnaheKindergartenPlaetze(null);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderarten, null), is(true));

        kinderarten.setAnzahlKindergartenPlaetze(100);
        kinderarten.setWohnungsnaheKindergartenPlaetze(0);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderarten, null), is(true));

        kinderarten.setAnzahlKindergartenPlaetze(100);
        kinderarten.setWohnungsnaheKindergartenPlaetze(99);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderarten, null), is(true));

        kinderarten.setAnzahlKindergartenPlaetze(100);
        kinderarten.setWohnungsnaheKindergartenPlaetze(100);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderarten, null), is(true));

        kinderarten.setAnzahlKindergartenPlaetze(100);
        kinderarten.setWohnungsnaheKindergartenPlaetze(101);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderarten, null), is(false));

        kinderarten.setAnzahlKindergartenPlaetze(null);
        kinderarten.setWohnungsnaheKindergartenPlaetze(101);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderarten, null), is(false));

        kinderarten.setAnzahlKindergartenPlaetze(0);
        kinderarten.setWohnungsnaheKindergartenPlaetze(101);
        assertThat(wohnungsnahePlaetzeValidator.isValid(kinderarten, null), is(false));
    }

    @Test
    void isValidHausFuerKinder() {
        assertThat(wohnungsnahePlaetzeValidator.isValid(null, null), is(true));

        final var hausFuerKinder = new HausFuerKinderDto();
        hausFuerKinder.setAnzahlKinderkrippePlaetze(null);
        hausFuerKinder.setWohnungsnaheKinderkrippePlaetze(null);
        hausFuerKinder.setAnzahlKindergartenPlaetze(null);
        hausFuerKinder.setWohnungsnaheKindergartenPlaetze(null);
        hausFuerKinder.setAnzahlHortPlaetze(null);
        hausFuerKinder.setWohnungsnaheHortPlaetze(null);
        assertThat(wohnungsnahePlaetzeValidator.isValid(hausFuerKinder, null), is(true));

        hausFuerKinder.setAnzahlKinderkrippePlaetze(100);
        hausFuerKinder.setWohnungsnaheKinderkrippePlaetze(null);
        hausFuerKinder.setAnzahlKindergartenPlaetze(100);
        hausFuerKinder.setWohnungsnaheKindergartenPlaetze(null);
        hausFuerKinder.setAnzahlHortPlaetze(100);
        hausFuerKinder.setWohnungsnaheHortPlaetze(null);
        assertThat(wohnungsnahePlaetzeValidator.isValid(hausFuerKinder, null), is(true));

        hausFuerKinder.setAnzahlKinderkrippePlaetze(100);
        hausFuerKinder.setWohnungsnaheKinderkrippePlaetze(0);
        hausFuerKinder.setAnzahlKindergartenPlaetze(100);
        hausFuerKinder.setWohnungsnaheKindergartenPlaetze(0);
        hausFuerKinder.setAnzahlHortPlaetze(100);
        hausFuerKinder.setWohnungsnaheHortPlaetze(0);
        assertThat(wohnungsnahePlaetzeValidator.isValid(hausFuerKinder, null), is(true));

        hausFuerKinder.setAnzahlKinderkrippePlaetze(100);
        hausFuerKinder.setWohnungsnaheKinderkrippePlaetze(99);
        hausFuerKinder.setAnzahlKindergartenPlaetze(100);
        hausFuerKinder.setWohnungsnaheKindergartenPlaetze(99);
        hausFuerKinder.setAnzahlHortPlaetze(100);
        hausFuerKinder.setWohnungsnaheHortPlaetze(99);
        assertThat(wohnungsnahePlaetzeValidator.isValid(hausFuerKinder, null), is(true));

        hausFuerKinder.setAnzahlKinderkrippePlaetze(100);
        hausFuerKinder.setWohnungsnaheKinderkrippePlaetze(100);
        hausFuerKinder.setAnzahlKindergartenPlaetze(100);
        hausFuerKinder.setWohnungsnaheKindergartenPlaetze(100);
        hausFuerKinder.setAnzahlHortPlaetze(100);
        hausFuerKinder.setWohnungsnaheHortPlaetze(100);
        assertThat(wohnungsnahePlaetzeValidator.isValid(hausFuerKinder, null), is(true));

        hausFuerKinder.setAnzahlKinderkrippePlaetze(100);
        hausFuerKinder.setWohnungsnaheKinderkrippePlaetze(101);
        hausFuerKinder.setAnzahlKindergartenPlaetze(100);
        hausFuerKinder.setWohnungsnaheKindergartenPlaetze(100);
        hausFuerKinder.setAnzahlHortPlaetze(100);
        hausFuerKinder.setWohnungsnaheHortPlaetze(100);
        assertThat(wohnungsnahePlaetzeValidator.isValid(hausFuerKinder, null), is(false));

        hausFuerKinder.setAnzahlKinderkrippePlaetze(100);
        hausFuerKinder.setWohnungsnaheKinderkrippePlaetze(100);
        hausFuerKinder.setAnzahlKindergartenPlaetze(100);
        hausFuerKinder.setWohnungsnaheKindergartenPlaetze(101);
        hausFuerKinder.setAnzahlHortPlaetze(100);
        hausFuerKinder.setWohnungsnaheHortPlaetze(100);
        assertThat(wohnungsnahePlaetzeValidator.isValid(hausFuerKinder, null), is(false));

        hausFuerKinder.setAnzahlKinderkrippePlaetze(100);
        hausFuerKinder.setWohnungsnaheKinderkrippePlaetze(100);
        hausFuerKinder.setAnzahlKindergartenPlaetze(100);
        hausFuerKinder.setWohnungsnaheKindergartenPlaetze(100);
        hausFuerKinder.setAnzahlHortPlaetze(100);
        hausFuerKinder.setWohnungsnaheHortPlaetze(101);
        assertThat(wohnungsnahePlaetzeValidator.isValid(hausFuerKinder, null), is(false));

        hausFuerKinder.setAnzahlKinderkrippePlaetze(null);
        hausFuerKinder.setWohnungsnaheKinderkrippePlaetze(100);
        hausFuerKinder.setAnzahlKindergartenPlaetze(100);
        hausFuerKinder.setWohnungsnaheKindergartenPlaetze(100);
        hausFuerKinder.setAnzahlHortPlaetze(100);
        hausFuerKinder.setWohnungsnaheHortPlaetze(100);
        assertThat(wohnungsnahePlaetzeValidator.isValid(hausFuerKinder, null), is(false));

        hausFuerKinder.setAnzahlKinderkrippePlaetze(0);
        hausFuerKinder.setWohnungsnaheKinderkrippePlaetze(100);
        hausFuerKinder.setAnzahlKindergartenPlaetze(100);
        hausFuerKinder.setWohnungsnaheKindergartenPlaetze(100);
        hausFuerKinder.setAnzahlHortPlaetze(100);
        hausFuerKinder.setWohnungsnaheHortPlaetze(100);
        assertThat(wohnungsnahePlaetzeValidator.isValid(hausFuerKinder, null), is(false));

        hausFuerKinder.setAnzahlKinderkrippePlaetze(100);
        hausFuerKinder.setWohnungsnaheKinderkrippePlaetze(100);
        hausFuerKinder.setAnzahlKindergartenPlaetze(null);
        hausFuerKinder.setWohnungsnaheKindergartenPlaetze(100);
        hausFuerKinder.setAnzahlHortPlaetze(100);
        hausFuerKinder.setWohnungsnaheHortPlaetze(100);
        assertThat(wohnungsnahePlaetzeValidator.isValid(hausFuerKinder, null), is(false));

        hausFuerKinder.setAnzahlKinderkrippePlaetze(100);
        hausFuerKinder.setWohnungsnaheKinderkrippePlaetze(100);
        hausFuerKinder.setAnzahlKindergartenPlaetze(0);
        hausFuerKinder.setWohnungsnaheKindergartenPlaetze(100);
        hausFuerKinder.setAnzahlHortPlaetze(100);
        hausFuerKinder.setWohnungsnaheHortPlaetze(100);
        assertThat(wohnungsnahePlaetzeValidator.isValid(hausFuerKinder, null), is(false));

        hausFuerKinder.setAnzahlKinderkrippePlaetze(100);
        hausFuerKinder.setWohnungsnaheKinderkrippePlaetze(100);
        hausFuerKinder.setAnzahlKindergartenPlaetze(100);
        hausFuerKinder.setWohnungsnaheKindergartenPlaetze(100);
        hausFuerKinder.setAnzahlHortPlaetze(null);
        hausFuerKinder.setWohnungsnaheHortPlaetze(100);
        assertThat(wohnungsnahePlaetzeValidator.isValid(hausFuerKinder, null), is(false));

        hausFuerKinder.setAnzahlKinderkrippePlaetze(100);
        hausFuerKinder.setWohnungsnaheKinderkrippePlaetze(100);
        hausFuerKinder.setAnzahlKindergartenPlaetze(100);
        hausFuerKinder.setWohnungsnaheKindergartenPlaetze(100);
        hausFuerKinder.setAnzahlHortPlaetze(0);
        hausFuerKinder.setWohnungsnaheHortPlaetze(100);
        assertThat(wohnungsnahePlaetzeValidator.isValid(hausFuerKinder, null), is(false));
    }

    @Test
    void isValidGsNachmittagBetreuungDto() {
        assertThat(wohnungsnahePlaetzeValidator.isValid(null, null), is(true));

        final var gsNachmittagBetreuung = new GsNachmittagBetreuungDto();
        gsNachmittagBetreuung.setAnzahlHortPlaetze(null);
        gsNachmittagBetreuung.setWohnungsnaheHortPlaetze(null);
        assertThat(wohnungsnahePlaetzeValidator.isValid(gsNachmittagBetreuung, null), is(true));

        gsNachmittagBetreuung.setAnzahlHortPlaetze(100);
        gsNachmittagBetreuung.setWohnungsnaheHortPlaetze(null);
        assertThat(wohnungsnahePlaetzeValidator.isValid(gsNachmittagBetreuung, null), is(true));

        gsNachmittagBetreuung.setAnzahlHortPlaetze(100);
        gsNachmittagBetreuung.setWohnungsnaheHortPlaetze(0);
        assertThat(wohnungsnahePlaetzeValidator.isValid(gsNachmittagBetreuung, null), is(true));

        gsNachmittagBetreuung.setAnzahlHortPlaetze(100);
        gsNachmittagBetreuung.setWohnungsnaheHortPlaetze(99);
        assertThat(wohnungsnahePlaetzeValidator.isValid(gsNachmittagBetreuung, null), is(true));

        gsNachmittagBetreuung.setAnzahlHortPlaetze(100);
        gsNachmittagBetreuung.setWohnungsnaheHortPlaetze(100);
        assertThat(wohnungsnahePlaetzeValidator.isValid(gsNachmittagBetreuung, null), is(true));

        gsNachmittagBetreuung.setAnzahlHortPlaetze(100);
        gsNachmittagBetreuung.setWohnungsnaheHortPlaetze(101);
        assertThat(wohnungsnahePlaetzeValidator.isValid(gsNachmittagBetreuung, null), is(false));

        gsNachmittagBetreuung.setAnzahlHortPlaetze(null);
        gsNachmittagBetreuung.setWohnungsnaheHortPlaetze(101);
        assertThat(wohnungsnahePlaetzeValidator.isValid(gsNachmittagBetreuung, null), is(false));

        gsNachmittagBetreuung.setAnzahlHortPlaetze(0);
        gsNachmittagBetreuung.setWohnungsnaheHortPlaetze(101);
        assertThat(wohnungsnahePlaetzeValidator.isValid(gsNachmittagBetreuung, null), is(false));
    }

    @Test
    void isValidOther() {
        assertThat(wohnungsnahePlaetzeValidator.isValid(new GrundschuleDto(), null), is(true));
        assertThat(wohnungsnahePlaetzeValidator.isValid(new MittelschuleDto(), null), is(true));
    }
}
