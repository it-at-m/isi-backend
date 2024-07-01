package de.muenchen.isi.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.model.common.AdresseModel;
import de.muenchen.isi.domain.model.common.Wgs84Model;
import de.muenchen.isi.domain.service.KoordinatenService;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.Utm;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AdresseDomainMapperTest {

    private final AdresseDomainMapper adresseDomainMapper = new AdresseDomainMapperImpl(
        new KoordinatenDomainMapperImpl()
    );

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        Field field = adresseDomainMapper.getClass().getSuperclass().getDeclaredField("koordinatenService");
        field.setAccessible(true);
        field.set(adresseDomainMapper, new KoordinatenService());
        field = adresseDomainMapper.getClass().getSuperclass().getDeclaredField("koordinatenDomainMapper");
        field.setAccessible(true);
        field.set(adresseDomainMapper, new KoordinatenDomainMapperImpl());
    }

    @Test
    void model2Entity() {
        final var adresse = new AdresseModel();
        final var wgs84 = new Wgs84Model();
        wgs84.setLongitude(11.542026984158708);
        wgs84.setLatitude(48.108341907595324);
        adresse.setCoordinate(wgs84);

        final var result = adresseDomainMapper.model2Entity(adresse);

        final var expected = new Adresse();
        final var wgs84Entity = new Wgs84();
        wgs84Entity.setLongitude(11.542026984158708);
        wgs84Entity.setLatitude(48.108341907595324);
        expected.setCoordinate(wgs84Entity);

        final var utm = new Utm();
        utm.setZone("32U");
        utm.setEast(689219.7547272056);
        utm.setNorth(5331467.745840158);
        expected.setCoordinateUtm(utm);

        assertThat(result, is(expected));
    }

    @Test
    void model2EntityEmtpy() {
        final var adresse = new AdresseModel();

        final var result = adresseDomainMapper.model2Entity(adresse);

        final var expected = new Adresse();

        assertThat(result, is(expected));
    }

    @Test
    void model2EntityException() {
        final var adresse = new AdresseModel();
        final var wgs84 = new Wgs84Model();
        wgs84.setLongitude(999999999999999999999d);
        wgs84.setLatitude(48.108341907595324);
        adresse.setCoordinate(wgs84);

        final var result = adresseDomainMapper.model2Entity(adresse);

        final var expected = new Adresse();
        final var wgs84Entity = new Wgs84();
        wgs84Entity.setLongitude(999999999999999999999d);
        wgs84Entity.setLatitude(48.108341907595324);
        expected.setCoordinate(wgs84Entity);

        assertThat(result, is(expected));
    }
}
