package de.muenchen.isi.infrastructure.repository.stammdaten;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.infrastructure.entity.enums.IdealtypischeBaurateTyp;
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
        var idealtypeischeBaurate = createIdealtypischeBaurateForWohneinheitenAndGeschossflaecheWohnen(
            BigDecimal.valueOf(0L),
            BigDecimal.valueOf(100L),
            BigDecimal.valueOf(0L),
            BigDecimal.valueOf(150L)
        );
        idealtypischeBaurateRepository.saveAllAndFlush(idealtypeischeBaurate);
        idealtypeischeBaurate =
            createIdealtypischeBaurateForWohneinheitenAndGeschossflaecheWohnen(
                BigDecimal.valueOf(101L),
                BigDecimal.valueOf(200L),
                BigDecimal.valueOf(151L),
                BigDecimal.valueOf(250L)
            );
        idealtypischeBaurateRepository.saveAllAndFlush(idealtypeischeBaurate);
        idealtypeischeBaurate =
            createIdealtypischeBaurateForWohneinheitenAndGeschossflaecheWohnen(
                BigDecimal.valueOf(201L),
                BigDecimal.valueOf(300L),
                BigDecimal.valueOf(251L),
                BigDecimal.valueOf(350L)
            );
        idealtypischeBaurateRepository.saveAllAndFlush(idealtypeischeBaurate);
    }

    @Test
    @Transactional
    void findByTypAndVonLessThanEqualAndBisEinschliesslichGreaterThanEqual() {
        var result = idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisEinschliesslichGreaterThanEqual(
            IdealtypischeBaurateTyp.WOHNEINHEITEN,
            BigDecimal.valueOf(-1L)
        );
        assertThat(result.isPresent(), is(false));

        result =
            idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisEinschliesslichGreaterThanEqual(
                IdealtypischeBaurateTyp.WOHNEINHEITEN,
                BigDecimal.valueOf(0L)
            );
        assertThat(result.get().getVon(), is(BigDecimal.valueOf(0L)));
        assertThat(result.get().getBisEinschliesslich(), is(BigDecimal.valueOf(100L)));

        result =
            idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisEinschliesslichGreaterThanEqual(
                IdealtypischeBaurateTyp.WOHNEINHEITEN,
                BigDecimal.valueOf(50L)
            );
        assertThat(result.get().getVon(), is(BigDecimal.valueOf(0L)));
        assertThat(result.get().getBisEinschliesslich(), is(BigDecimal.valueOf(100L)));

        result =
            idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisEinschliesslichGreaterThanEqual(
                IdealtypischeBaurateTyp.WOHNEINHEITEN,
                BigDecimal.valueOf(100L)
            );
        assertThat(result.get().getVon(), is(BigDecimal.valueOf(0L)));
        assertThat(result.get().getBisEinschliesslich(), is(BigDecimal.valueOf(100L)));

        result =
            idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisEinschliesslichGreaterThanEqual(
                IdealtypischeBaurateTyp.WOHNEINHEITEN,
                BigDecimal.valueOf(101L)
            );
        assertThat(result.get().getVon(), is(BigDecimal.valueOf(101L)));
        assertThat(result.get().getBisEinschliesslich(), is(BigDecimal.valueOf(200L)));

        result =
            idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisEinschliesslichGreaterThanEqual(
                IdealtypischeBaurateTyp.WOHNEINHEITEN,
                BigDecimal.valueOf(151L)
            );
        assertThat(result.get().getVon(), is(BigDecimal.valueOf(101L)));
        assertThat(result.get().getBisEinschliesslich(), is(BigDecimal.valueOf(200L)));

        result =
            idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisEinschliesslichGreaterThanEqual(
                IdealtypischeBaurateTyp.WOHNEINHEITEN,
                BigDecimal.valueOf(200L)
            );
        assertThat(result.get().getVon(), is(BigDecimal.valueOf(101L)));
        assertThat(result.get().getBisEinschliesslich(), is(BigDecimal.valueOf(200L)));

        result =
            idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisEinschliesslichGreaterThanEqual(
                IdealtypischeBaurateTyp.WOHNEINHEITEN,
                BigDecimal.valueOf(201L)
            );
        assertThat(result.get().getVon(), is(BigDecimal.valueOf(201L)));
        assertThat(result.get().getBisEinschliesslich(), is(BigDecimal.valueOf(300L)));

        result =
            idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisEinschliesslichGreaterThanEqual(
                IdealtypischeBaurateTyp.WOHNEINHEITEN,
                BigDecimal.valueOf(250L)
            );
        assertThat(result.get().getVon(), is(BigDecimal.valueOf(201L)));
        assertThat(result.get().getBisEinschliesslich(), is(BigDecimal.valueOf(300L)));

        result =
            idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisEinschliesslichGreaterThanEqual(
                IdealtypischeBaurateTyp.WOHNEINHEITEN,
                BigDecimal.valueOf(300L)
            );
        assertThat(result.get().getVon(), is(BigDecimal.valueOf(201L)));
        assertThat(result.get().getBisEinschliesslich(), is(BigDecimal.valueOf(300L)));

        result =
            idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisEinschliesslichGreaterThanEqual(
                IdealtypischeBaurateTyp.WOHNEINHEITEN,
                BigDecimal.valueOf(301L)
            );
        assertThat(result.isPresent(), is(false));

        result =
            idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisEinschliesslichGreaterThanEqual(
                null,
                null
            );
        assertThat(result.isPresent(), is(false));
    }

    public static List<IdealtypischeBaurate> createIdealtypischeBaurateForWohneinheitenAndGeschossflaecheWohnen(
        final BigDecimal wohneinheitenVon,
        final BigDecimal wohneinheitenBisEinschliesslich,
        final BigDecimal geschossflaecheWohnenVon,
        final BigDecimal geschossflaecheWohnenBisEinschliesslich
    ) {
        final var idealtypischeBaurateWohneinheiten = new IdealtypischeBaurate();

        idealtypischeBaurateWohneinheiten.setTyp(IdealtypischeBaurateTyp.WOHNEINHEITEN);
        idealtypischeBaurateWohneinheiten.setVon(wohneinheitenVon);
        idealtypischeBaurateWohneinheiten.setBisEinschliesslich(wohneinheitenBisEinschliesslich);

        var jahresrate1 = new Jahresrate();
        jahresrate1.setJahr(1);
        jahresrate1.setRate(BigDecimal.valueOf(10.49));
        var jahresrate2 = new Jahresrate();
        jahresrate2.setJahr(2);
        jahresrate2.setRate(BigDecimal.valueOf(89.51));
        idealtypischeBaurateWohneinheiten.setJahresraten(List.of(jahresrate1, jahresrate2));

        final var idealtypischeBaurateGeschossflaecheWohnen = new IdealtypischeBaurate();

        idealtypischeBaurateGeschossflaecheWohnen.setTyp(IdealtypischeBaurateTyp.GESCHOSSFLAECHE_WOHNEN);
        idealtypischeBaurateGeschossflaecheWohnen.setVon(geschossflaecheWohnenVon);
        idealtypischeBaurateGeschossflaecheWohnen.setBisEinschliesslich(geschossflaecheWohnenBisEinschliesslich);

        jahresrate1 = new Jahresrate();
        jahresrate1.setJahr(1);
        jahresrate1.setRate(BigDecimal.valueOf(10.49));
        jahresrate2 = new Jahresrate();
        jahresrate2.setJahr(2);
        jahresrate2.setRate(BigDecimal.valueOf(89.51));
        idealtypischeBaurateGeschossflaecheWohnen.setJahresraten(List.of(jahresrate1, jahresrate2));

        return List.of(idealtypischeBaurateWohneinheiten, idealtypischeBaurateGeschossflaecheWohnen);
    }
}
