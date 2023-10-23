package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.infrastruktureinrichtung.GrundschuleDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.GsNachmittagBetreuungDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.HausFuerKinderDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.KindergartenDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.KinderkrippeDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.MittelschuleDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.SchuleDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import org.junit.jupiter.api.Test;

public class EinrichtungstraegerValidatorTest {

    private final EinrichtungstraegerValidator validator = new EinrichtungstraegerValidator();

    @Test
    void isValidGrundschule() {
        final GrundschuleDto value = new GrundschuleDto();
        final SchuleDto schuleDto = new SchuleDto();
        value.setSchule(schuleDto);

        assertThat(this.validator.isValid(null, null), is(false));
        assertThat(this.validator.isValid(value, null), is(true));

        value.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        assertThat(this.validator.isValid(value, null), is(false));

        value.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR);
        assertThat(this.validator.isValid(value, null), is(false));

        value.getSchule().setEinrichtungstraeger(Einrichtungstraeger.STAEDTISCHE_EINRICHTUNG);
        assertThat(this.validator.isValid(value, null), is(true));

        value.getSchule().setEinrichtungstraeger(Einrichtungstraeger.EINRICHTUNG_GESAMTSTAEDTISCH);
        assertThat(this.validator.isValid(value, null), is(false));
    }

    @Test
    void isValidMittelschule() {
        final MittelschuleDto value = new MittelschuleDto();
        final SchuleDto schuleDto = new SchuleDto();
        value.setSchule(schuleDto);

        assertThat(this.validator.isValid(null, null), is(false));
        assertThat(this.validator.isValid(value, null), is(true));

        value.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        assertThat(this.validator.isValid(value, null), is(false));

        value.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR);
        assertThat(this.validator.isValid(value, null), is(false));

        value.getSchule().setEinrichtungstraeger(Einrichtungstraeger.STAEDTISCHE_EINRICHTUNG);
        assertThat(this.validator.isValid(value, null), is(true));

        value.getSchule().setEinrichtungstraeger(Einrichtungstraeger.EINRICHTUNG_GESAMTSTAEDTISCH);
        assertThat(this.validator.isValid(value, null), is(false));
    }

    @Test
    void isValidGsNachmittagBetreuung() {
        final GsNachmittagBetreuungDto value = new GsNachmittagBetreuungDto();

        assertThat(this.validator.isValid(null, null), is(false));
        assertThat(this.validator.isValid(value, null), is(true));

        value.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        assertThat(this.validator.isValid(value, null), is(false));

        value.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR);
        assertThat(this.validator.isValid(value, null), is(false));

        value.setEinrichtungstraeger(Einrichtungstraeger.STAEDTISCHE_EINRICHTUNG);
        assertThat(this.validator.isValid(value, null), is(true));

        value.setEinrichtungstraeger(Einrichtungstraeger.PRIVATE_TRAEGERSCHAFT);
        assertThat(this.validator.isValid(value, null), is(false));
    }

    @Test
    void isValidGrundschuleHausFuerKinder() {
        final HausFuerKinderDto value = new HausFuerKinderDto();

        assertThat(this.validator.isValid(null, null), is(false));
        assertThat(this.validator.isValid(value, null), is(true));

        value.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        assertThat(this.validator.isValid(value, null), is(false));

        value.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR);
        assertThat(this.validator.isValid(value, null), is(false));

        value.setEinrichtungstraeger(Einrichtungstraeger.STAEDTISCHE_EINRICHTUNG);
        assertThat(this.validator.isValid(value, null), is(true));

        value.setEinrichtungstraeger(Einrichtungstraeger.PRIVATE_TRAEGERSCHAFT);
        assertThat(this.validator.isValid(value, null), is(false));
    }

    @Test
    void isValidKindergarten() {
        final KindergartenDto value = new KindergartenDto();

        assertThat(this.validator.isValid(null, null), is(false));
        assertThat(this.validator.isValid(value, null), is(true));

        value.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        assertThat(this.validator.isValid(value, null), is(false));

        value.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR);
        assertThat(this.validator.isValid(value, null), is(false));

        value.setEinrichtungstraeger(Einrichtungstraeger.STAEDTISCHE_EINRICHTUNG);
        assertThat(this.validator.isValid(value, null), is(true));

        value.setEinrichtungstraeger(Einrichtungstraeger.PRIVATE_TRAEGERSCHAFT);
        assertThat(this.validator.isValid(value, null), is(false));
    }

    @Test
    void isValidKinderkrippe() {
        final KinderkrippeDto value = new KinderkrippeDto();

        assertThat(this.validator.isValid(null, null), is(false));
        assertThat(this.validator.isValid(value, null), is(true));

        value.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        assertThat(this.validator.isValid(value, null), is(false));

        value.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR);
        assertThat(this.validator.isValid(value, null), is(false));

        value.setEinrichtungstraeger(Einrichtungstraeger.STAEDTISCHE_EINRICHTUNG);
        assertThat(this.validator.isValid(value, null), is(true));

        value.setEinrichtungstraeger(Einrichtungstraeger.PRIVATE_TRAEGERSCHAFT);
        assertThat(this.validator.isValid(value, null), is(false));
    }

    @Test
    void isValidEinrichutngstraegerSchule() {
        Einrichtungstraeger einrichtungstraeger = Einrichtungstraeger.PRIVATE_TRAEGERSCHAFT;

        assertThat(this.validator.isValidEinrichtungstraegerSchule(null), is(false));
        assertThat(this.validator.isValidEinrichtungstraegerSchule(einrichtungstraeger), is(true));

        einrichtungstraeger = Einrichtungstraeger.EINRICHTUNG_GESAMTSTAEDTISCH;
        assertThat(this.validator.isValidEinrichtungstraegerSchule(einrichtungstraeger), is(false));
    }

    @Test
    void isValidEinrichutngstraegere() {
        Einrichtungstraeger einrichtungstraeger = Einrichtungstraeger.ELTERN_KIND_INITIATIVE;

        assertThat(this.validator.isValidEinrichtungstraeger(null), is(false));
        assertThat(this.validator.isValidEinrichtungstraeger(einrichtungstraeger), is(true));

        einrichtungstraeger = Einrichtungstraeger.KIRCHLICHE_TRAEGERSCHAFT;
        assertThat(this.validator.isValidEinrichtungstraeger(einrichtungstraeger), is(false));
    }

    @Test
    void isStatusBestandOrGesicherteERWTest() {
        final GrundschuleDto value = new GrundschuleDto();

        value.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        assertThat(this.validator.isStatusBestandOrGesicherteERW(value), is(true));

        value.setStatus(StatusInfrastruktureinrichtung.UNGESICHERTE_PLANUNG);
        assertThat(this.validator.isStatusBestandOrGesicherteERW(value), is(false));

        value.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR);
        assertThat(this.validator.isStatusBestandOrGesicherteERW(value), is(true));

        value.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_REDUZIERUNG_PLAETZE);
        assertThat(this.validator.isStatusBestandOrGesicherteERW(value), is(false));
    }
}
