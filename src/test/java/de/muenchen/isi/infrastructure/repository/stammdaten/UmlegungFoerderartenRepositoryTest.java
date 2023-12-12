package de.muenchen.isi.infrastructure.repository.stammdaten;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.infrastructure.entity.Foerderart;
import de.muenchen.isi.infrastructure.entity.stammdaten.UmlegungFoerderarten;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = { IsiBackendApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
@MockitoSettings(strictness = Strictness.LENIENT)
public class UmlegungFoerderartenRepositoryTest {

    @Autowired
    private UmlegungFoerderartenRepository umlegungFoerderartenRepository;

    @BeforeEach
    public void beforeEach() {
        this.umlegungFoerderartenRepository.deleteAll();
    }

    @Test
    void findUmlegungFoerderartenbyBezeichnungAndDatum() {
        UmlegungFoerderarten umlegungFoerderarten1 = new UmlegungFoerderarten();
        umlegungFoerderarten1.setId(UUID.randomUUID());
        umlegungFoerderarten1.setBezeichnung("Test1");
        umlegungFoerderarten1.setGueltigAb(LocalDate.parse("2000-01-01"));
        var foerderart1 = new Foerderart();
        foerderart1.setBezeichnung("foerderart1");
        foerderart1.setAnteilProzent(BigDecimal.valueOf(75));
        var foerderart2 = new Foerderart();
        foerderart2.setBezeichnung("foerderart2");
        foerderart2.setAnteilProzent(BigDecimal.valueOf(25));
        var umlegungschluessel = Set.of(foerderart1, foerderart2);
        umlegungFoerderarten1.setUmlegungsschluessel(umlegungschluessel);

        UmlegungFoerderarten umlegungFoerderarten2 = new UmlegungFoerderarten();
        umlegungFoerderarten2.setId(UUID.randomUUID());
        umlegungFoerderarten2.setBezeichnung("Test2");
        umlegungFoerderarten2.setGueltigAb(LocalDate.parse("2005-05-05"));

        UmlegungFoerderarten umlegungFoerderarten3 = new UmlegungFoerderarten();
        umlegungFoerderarten3.setId(UUID.randomUUID());
        umlegungFoerderarten3.setBezeichnung("Test3");
        umlegungFoerderarten3.setGueltigAb(LocalDate.parse("2010-01-01"));

        this.umlegungFoerderartenRepository.saveAll(
                List.of(umlegungFoerderarten1, umlegungFoerderarten2, umlegungFoerderarten3)
            );

        Optional<UmlegungFoerderarten> result1 =
            this.umlegungFoerderartenRepository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    umlegungFoerderarten1.getBezeichnung(),
                    LocalDate.parse("2003-05-05")
                );

        Optional<UmlegungFoerderarten> result2 =
            this.umlegungFoerderartenRepository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    umlegungFoerderarten2.getBezeichnung(),
                    LocalDate.parse("2005-05-05")
                );

        Optional<UmlegungFoerderarten> result3 =
            this.umlegungFoerderartenRepository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    umlegungFoerderarten2.getBezeichnung(),
                    LocalDate.parse("2007-10-15")
                );

        Optional<UmlegungFoerderarten> result4 =
            this.umlegungFoerderartenRepository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    umlegungFoerderarten3.getBezeichnung(),
                    LocalDate.parse("2011-06-14")
                );

        Optional<UmlegungFoerderarten> result5 =
            this.umlegungFoerderartenRepository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    umlegungFoerderarten1.getBezeichnung(),
                    LocalDate.parse("2005-05-04")
                );

        Optional<UmlegungFoerderarten> result6 =
            this.umlegungFoerderartenRepository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    umlegungFoerderarten2.getBezeichnung(),
                    LocalDate.parse("2005-05-06")
                );

        assertThat(result1.get(), is(umlegungFoerderarten1));
        assertThat(result2.get(), is(umlegungFoerderarten2));
        assertThat(result3.get(), is(umlegungFoerderarten2));
        assertThat(result4.get(), is(umlegungFoerderarten3));
        assertThat(result5.get(), is(umlegungFoerderarten1));
        assertThat(result6.get(), is(umlegungFoerderarten2));
    }

    @Test
    void NoSuchElementExceptionUmlegungFoerderartenbyBezeichnungAndDatum() {
        UmlegungFoerderarten umlegungFoerderarten1 = new UmlegungFoerderarten();
        umlegungFoerderarten1.setId(UUID.randomUUID());
        umlegungFoerderarten1.setBezeichnung("Test1");
        umlegungFoerderarten1.setGueltigAb(LocalDate.parse("2000-01-01"));

        UmlegungFoerderarten umlegungFoerderarten2 = new UmlegungFoerderarten();
        umlegungFoerderarten2.setId(UUID.randomUUID());
        umlegungFoerderarten2.setBezeichnung("Test2");
        umlegungFoerderarten2.setGueltigAb(LocalDate.parse("2005-05-05"));

        UmlegungFoerderarten umlegungFoerderarten3 = new UmlegungFoerderarten();
        umlegungFoerderarten3.setId(UUID.randomUUID());
        umlegungFoerderarten3.setBezeichnung("Test3");
        umlegungFoerderarten3.setGueltigAb(LocalDate.parse("2010-01-01"));

        this.umlegungFoerderartenRepository.saveAll(
                List.of(umlegungFoerderarten1, umlegungFoerderarten2, umlegungFoerderarten3)
            );

        Optional<UmlegungFoerderarten> result1 =
            this.umlegungFoerderartenRepository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    umlegungFoerderarten1.getBezeichnung(),
                    LocalDate.parse("1999-06-14")
                );

        Optional<UmlegungFoerderarten> result2 =
            this.umlegungFoerderartenRepository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    "Test4",
                    LocalDate.parse("2003-05-05")
                );

        Optional<UmlegungFoerderarten> result3 =
            this.umlegungFoerderartenRepository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    "Test5",
                    LocalDate.parse("1980-05-05")
                );

        Optional<UmlegungFoerderarten> result4 =
            this.umlegungFoerderartenRepository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    umlegungFoerderarten2.getBezeichnung(),
                    LocalDate.parse("2005-05-04")
                );

        Assertions.assertThrows(NoSuchElementException.class, () -> result1.get());
        Assertions.assertThrows(NoSuchElementException.class, () -> result2.get());
        Assertions.assertThrows(NoSuchElementException.class, () -> result3.get());
        Assertions.assertThrows(NoSuchElementException.class, () -> result4.get());
    }
}
