package de.muenchen.isi.infrastructure.repository.stammdaten;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.IdealtypischeBaurate;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.Jahresrate;
import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = { IsiBackendApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
@MockitoSettings(strictness = Strictness.LENIENT)
class IdealtypischeBaurateRepositoryTest {

    @Autowired
    private IdealtypischeBaurateRepository idealtypischeBaurateRepository;

    @BeforeEach
    @Transactional
    void beforeEach() {
        idealtypischeBaurateRepository.deleteAll();
        var idealtypeischeBaurate = createIdealtypischeBaurate(
            0L,
            100L,
            BigDecimal.valueOf(0L),
            BigDecimal.valueOf(150L)
        );
        idealtypischeBaurateRepository.saveAndFlush(idealtypeischeBaurate);
        idealtypeischeBaurate =
            createIdealtypischeBaurate(101L, 200L, BigDecimal.valueOf(151L), BigDecimal.valueOf(250L));
        idealtypischeBaurateRepository.saveAndFlush(idealtypeischeBaurate);
        idealtypeischeBaurate =
            createIdealtypischeBaurate(201L, 300L, BigDecimal.valueOf(251L), BigDecimal.valueOf(350L));
        idealtypischeBaurateRepository.saveAndFlush(idealtypeischeBaurate);
    }

    @Test
    @Transactional
    void findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual() {
        var result =
            idealtypischeBaurateRepository.findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
                -1L
            );
        assertThat(result.isPresent(), is(false));

        result =
            idealtypischeBaurateRepository.findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
                0L
            );
        assertThat(result.get().getWohneinheitenVon(), is(0L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(100L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(0L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(150L)));

        result =
            idealtypischeBaurateRepository.findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
                50L
            );
        assertThat(result.get().getWohneinheitenVon(), is(0L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(100L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(0L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(150L)));

        result =
            idealtypischeBaurateRepository.findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
                100L
            );
        assertThat(result.get().getWohneinheitenVon(), is(0L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(100L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(0L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(150L)));

        result =
            idealtypischeBaurateRepository.findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
                101L
            );
        assertThat(result.get().getWohneinheitenVon(), is(101L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(200L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(151L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(250L)));

        result =
            idealtypischeBaurateRepository.findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
                151L
            );
        assertThat(result.get().getWohneinheitenVon(), is(101L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(200L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(151L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(250L)));

        result =
            idealtypischeBaurateRepository.findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
                200L
            );
        assertThat(result.get().getWohneinheitenVon(), is(101L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(200L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(151L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(250L)));

        result =
            idealtypischeBaurateRepository.findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
                201L
            );
        assertThat(result.get().getWohneinheitenVon(), is(201L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(300L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(251L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(350L)));

        result =
            idealtypischeBaurateRepository.findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
                250L
            );
        assertThat(result.get().getWohneinheitenVon(), is(201L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(300L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(251L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(350L)));

        result =
            idealtypischeBaurateRepository.findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
                300L
            );
        assertThat(result.get().getWohneinheitenVon(), is(201L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(300L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(251L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(350L)));

        result =
            idealtypischeBaurateRepository.findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
                301L
            );
        assertThat(result.isPresent(), is(false));

        result =
            idealtypischeBaurateRepository.findByWohneinheitenVonLessThanEqualAndWohneinheitenBisEinschliesslichGreaterThanEqual(
                null
            );
        assertThat(result.isPresent(), is(false));
    }

    @Test
    @Transactional
    void findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual() {
        var result =
            idealtypischeBaurateRepository.findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                BigDecimal.valueOf(-1L)
            );
        assertThat(result.isPresent(), is(false));

        result =
            idealtypischeBaurateRepository.findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                BigDecimal.valueOf(0L)
            );
        assertThat(result.get().getWohneinheitenVon(), is(0L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(100L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(0L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(150L)));

        result =
            idealtypischeBaurateRepository.findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                BigDecimal.valueOf(50L)
            );
        assertThat(result.get().getWohneinheitenVon(), is(0L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(100L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(0L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(150L)));

        result =
            idealtypischeBaurateRepository.findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                BigDecimal.valueOf(150L)
            );
        assertThat(result.get().getWohneinheitenVon(), is(0L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(100L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(0L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(150L)));

        result =
            idealtypischeBaurateRepository.findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                BigDecimal.valueOf(151L)
            );
        assertThat(result.get().getWohneinheitenVon(), is(101L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(200L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(151L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(250L)));

        result =
            idealtypischeBaurateRepository.findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                BigDecimal.valueOf(200L)
            );
        assertThat(result.get().getWohneinheitenVon(), is(101L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(200L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(151L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(250L)));

        result =
            idealtypischeBaurateRepository.findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                BigDecimal.valueOf(250L)
            );
        assertThat(result.get().getWohneinheitenVon(), is(101L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(200L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(151L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(250L)));

        result =
            idealtypischeBaurateRepository.findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                BigDecimal.valueOf(251L)
            );
        assertThat(result.get().getWohneinheitenVon(), is(201L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(300L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(251L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(350L)));

        result =
            idealtypischeBaurateRepository.findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                BigDecimal.valueOf(300L)
            );
        assertThat(result.get().getWohneinheitenVon(), is(201L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(300L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(251L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(350L)));

        result =
            idealtypischeBaurateRepository.findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                BigDecimal.valueOf(350L)
            );
        assertThat(result.get().getWohneinheitenVon(), is(201L));
        assertThat(result.get().getWohneinheitenBisEinschliesslich(), is(300L));
        assertThat(result.get().getGeschossflaecheWohnenVon(), is(BigDecimal.valueOf(251L)));
        assertThat(result.get().getGeschossflaecheWohnenBisEinschliesslich(), is(BigDecimal.valueOf(350L)));

        result =
            idealtypischeBaurateRepository.findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                BigDecimal.valueOf(351L)
            );
        assertThat(result.isPresent(), is(false));

        result =
            idealtypischeBaurateRepository.findByGeschossflaecheWohnenVonLessThanEqualAndGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                null
            );
        assertThat(result.isPresent(), is(false));
    }

    public static IdealtypischeBaurate createIdealtypischeBaurate(
        final Long wohneinheitenVon,
        final Long wohneinheitenBisEinschliesslich,
        final BigDecimal geschossflaecheWohnenVon,
        final BigDecimal geschossflaecheWohnenBisEinschliesslich
    ) {
        final var idealtypischeBaurate = new IdealtypischeBaurate();

        idealtypischeBaurate.setWohneinheitenVon(wohneinheitenVon);
        idealtypischeBaurate.setWohneinheitenBisEinschliesslich(wohneinheitenBisEinschliesslich);
        idealtypischeBaurate.setGeschossflaecheWohnenVon(geschossflaecheWohnenVon);
        idealtypischeBaurate.setGeschossflaecheWohnenBisEinschliesslich(geschossflaecheWohnenBisEinschliesslich);

        final var jahresrate1 = new Jahresrate();
        jahresrate1.setJahr(1);
        jahresrate1.setRate(BigDecimal.valueOf(10.49));
        final var jahresrate2 = new Jahresrate();
        jahresrate2.setJahr(2);
        jahresrate2.setRate(BigDecimal.valueOf(89.51));
        idealtypischeBaurate.setJahresraten(List.of(jahresrate1, jahresrate2));

        return idealtypischeBaurate;
    }
}
