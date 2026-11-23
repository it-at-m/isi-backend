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

    private final WohnungsnahePlaetzeValidator wohnungsnahePlaetzeValidator = new WohnungsnahePlaetzeValidator();

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
