package de.muenchen.isi.infrastructure.repository.stammdaten;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.IdealtypischeBaurate;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.Jahresrate;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.SelectionRange;
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
@SpringBootTest(
    classes = { IsiBackendApplication.class },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = { "tomcat.gracefulshutdown.pre-wait-seconds=0" }
)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
@MockitoSettings(strictness = Strictness.LENIENT)
class IdealtypischeBaurateRepositoryTest {

    @Autowired
    private IdealtypischeBaurateRepository idealtypischeBaurateRepository;

    @BeforeEach
    @Transactional
    void beforeEach() {
        idealtypischeBaurateRepository.deleteAll();
        var idealtypeischeBaurate = createIdealtypischeBaurate(0L, 100L, 0L, 150L);
        idealtypischeBaurateRepository.saveAndFlush(idealtypeischeBaurate);
        idealtypeischeBaurate = createIdealtypischeBaurate(101L, 200L, 151L, 250L);
        idealtypischeBaurateRepository.saveAndFlush(idealtypeischeBaurate);
        idealtypeischeBaurate = createIdealtypischeBaurate(201L, 300L, 251L, 350L);
        idealtypischeBaurateRepository.saveAndFlush(idealtypeischeBaurate);
    }

    @Test
    @Transactional
    void findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual() {
        var result =
            idealtypischeBaurateRepository.findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
                -1L
            );
        assertThat(result.isPresent(), is(false));

        result =
            idealtypischeBaurateRepository.findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
                0L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(0L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(100L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(0L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(150L));

        result =
            idealtypischeBaurateRepository.findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
                50L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(0L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(100L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(0L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(150L));

        result =
            idealtypischeBaurateRepository.findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
                100L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(0L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(100L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(0L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(150L));

        result =
            idealtypischeBaurateRepository.findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
                101L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(101L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(200L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(151L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(250L));

        result =
            idealtypischeBaurateRepository.findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
                151L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(101L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(200L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(151L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(250L));

        result =
            idealtypischeBaurateRepository.findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
                200L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(101L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(200L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(151L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(250L));

        result =
            idealtypischeBaurateRepository.findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
                201L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(201L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(300L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(251L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(350L));

        result =
            idealtypischeBaurateRepository.findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
                250L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(201L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(300L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(251L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(350L));

        result =
            idealtypischeBaurateRepository.findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
                300L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(201L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(300L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(251L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(350L));

        result =
            idealtypischeBaurateRepository.findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
                301L
            );
        assertThat(result.isPresent(), is(false));

        result =
            idealtypischeBaurateRepository.findByRangeWohneinheitenVonLessThanEqualAndRangeWohneinheitenBisEinschliesslichGreaterThanEqual(
                null
            );
        assertThat(result.isPresent(), is(false));
    }

    @Test
    @Transactional
    void findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual() {
        var result =
            idealtypischeBaurateRepository.findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                -1L
            );
        assertThat(result.isPresent(), is(false));

        result =
            idealtypischeBaurateRepository.findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                0L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(0L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(100L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(0L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(150L));

        result =
            idealtypischeBaurateRepository.findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                50L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(0L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(100L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(0L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(150L));

        result =
            idealtypischeBaurateRepository.findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                150L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(0L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(100L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(0L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(150L));

        result =
            idealtypischeBaurateRepository.findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                151L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(101L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(200L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(151L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(250L));

        result =
            idealtypischeBaurateRepository.findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                200L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(101L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(200L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(151L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(250L));

        result =
            idealtypischeBaurateRepository.findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                250L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(101L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(200L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(151L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(250L));

        result =
            idealtypischeBaurateRepository.findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                251L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(201L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(300L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(251L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(350L));

        result =
            idealtypischeBaurateRepository.findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                300L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(201L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(300L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(251L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(350L));

        result =
            idealtypischeBaurateRepository.findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                350L
            );
        assertThat(result.get().getRangeWohneinheiten().getVon(), is(201L));
        assertThat(result.get().getRangeWohneinheiten().getBisEinschliesslich(), is(300L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getVon(), is(251L));
        assertThat(result.get().getRangeGeschossflaecheWohnen().getBisEinschliesslich(), is(350L));

        result =
            idealtypischeBaurateRepository.findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                351L
            );
        assertThat(result.isPresent(), is(false));

        result =
            idealtypischeBaurateRepository.findByRangeGeschossflaecheWohnenVonLessThanEqualAndRangeGeschossflaecheWohnenBisEinschliesslichGreaterThanEqual(
                null
            );
        assertThat(result.isPresent(), is(false));
    }

    public static IdealtypischeBaurate createIdealtypischeBaurate(
        final Long wohneinheitenVon,
        final Long wohneinheitenBisEinschliesslich,
        final Long geschossflaecheWohnenVon,
        final Long geschossflaecheWohnenBisEinschliesslich
    ) {
        final var idealtypischeBaurate = new IdealtypischeBaurate();

        var selectionRange = new SelectionRange();
        selectionRange.setVon(wohneinheitenVon);
        selectionRange.setBisEinschliesslich(wohneinheitenBisEinschliesslich);
        idealtypischeBaurate.setRangeWohneinheiten(selectionRange);

        selectionRange = new SelectionRange();
        selectionRange.setVon(geschossflaecheWohnenVon);
        selectionRange.setBisEinschliesslich(geschossflaecheWohnenBisEinschliesslich);
        idealtypischeBaurate.setRangeGeschossflaecheWohnen(selectionRange);

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
