package de.muenchen.isi.infrastructure.repository.stammdaten;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.infrastructure.entity.stammdaten.StaedtebaulicheOrientierungswert;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
public class StaedtebaulicheOrientierungswertRepositoryTest {

    @Autowired
    private StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    @BeforeEach
    public void beforeEach() {
        this.staedtebaulicheOrientierungswertRepository.deleteAll();
    }

    @Test
    void findStaedtebaulicheOrientierungswertByFoerderartBezeichnungAndGueltigAb() {
        StaedtebaulicheOrientierungswert orientierungswert1 = new StaedtebaulicheOrientierungswert();
        orientierungswert1.setFoerderartBezeichnung("Test1");
        orientierungswert1.setGueltigAb(LocalDate.parse("2000-01-01"));
        orientierungswert1.setDurchschnittlicheGrundflaeche(100L);
        orientierungswert1.setBelegungsdichte(new BigDecimal("1.50"));

        StaedtebaulicheOrientierungswert orientierungswert2 = new StaedtebaulicheOrientierungswert();
        orientierungswert2.setFoerderartBezeichnung("Test2");
        orientierungswert2.setGueltigAb(LocalDate.parse("2005-05-05"));
        orientierungswert2.setDurchschnittlicheGrundflaeche(150L);
        orientierungswert2.setBelegungsdichte(new BigDecimal("2.00"));

        StaedtebaulicheOrientierungswert orientierungswert3 = new StaedtebaulicheOrientierungswert();
        orientierungswert3.setFoerderartBezeichnung("Test3");
        orientierungswert3.setGueltigAb(LocalDate.parse("2010-01-01"));
        orientierungswert3.setDurchschnittlicheGrundflaeche(120L);
        orientierungswert3.setBelegungsdichte(new BigDecimal("1.80"));

        this.staedtebaulicheOrientierungswertRepository.saveAll(
                List.of(orientierungswert1, orientierungswert2, orientierungswert3)
            );

        Optional<StaedtebaulicheOrientierungswert> result1 =
            this.staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    orientierungswert1.getFoerderartBezeichnung(),
                    LocalDate.parse("2003-05-05")
                );

        Optional<StaedtebaulicheOrientierungswert> result2 =
            this.staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    orientierungswert2.getFoerderartBezeichnung(),
                    LocalDate.parse("2005-05-05")
                );

        Optional<StaedtebaulicheOrientierungswert> result3 =
            this.staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    orientierungswert2.getFoerderartBezeichnung(),
                    LocalDate.parse("2007-10-15")
                );

        Optional<StaedtebaulicheOrientierungswert> result4 =
            this.staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    orientierungswert3.getFoerderartBezeichnung(),
                    LocalDate.parse("2011-06-14")
                );

        Optional<StaedtebaulicheOrientierungswert> result5 =
            this.staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    orientierungswert1.getFoerderartBezeichnung(),
                    LocalDate.parse("2005-05-04")
                );

        Optional<StaedtebaulicheOrientierungswert> result6 =
            this.staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    orientierungswert2.getFoerderartBezeichnung(),
                    LocalDate.parse("2005-05-06")
                );

        assertThat(result1.get(), is(orientierungswert1));
        assertThat(result2.get(), is(orientierungswert2));
        assertThat(result3.get(), is(orientierungswert2));
        assertThat(result4.get(), is(orientierungswert3));
        assertThat(result5.get(), is(orientierungswert1));
        assertThat(result6.get(), is(orientierungswert2));
    }

    @Test
    void noSuchElementExceptionStaedtebaulicheOrientierungswertByFoerderartBezeichnungAndGueltigAb() {
        StaedtebaulicheOrientierungswert orientierungswert1 = new StaedtebaulicheOrientierungswert();
        orientierungswert1.setFoerderartBezeichnung("Test1");
        orientierungswert1.setGueltigAb(LocalDate.parse("2000-01-01"));
        orientierungswert1.setDurchschnittlicheGrundflaeche(100L);
        orientierungswert1.setBelegungsdichte(new BigDecimal("1.50"));

        StaedtebaulicheOrientierungswert orientierungswert2 = new StaedtebaulicheOrientierungswert();
        orientierungswert2.setFoerderartBezeichnung("Test2");
        orientierungswert2.setGueltigAb(LocalDate.parse("2005-05-05"));
        orientierungswert2.setDurchschnittlicheGrundflaeche(150L);
        orientierungswert2.setBelegungsdichte(new BigDecimal("2.00"));

        this.staedtebaulicheOrientierungswertRepository.saveAll(List.of(orientierungswert1, orientierungswert2));

        Optional<StaedtebaulicheOrientierungswert> result1 =
            this.staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    orientierungswert1.getFoerderartBezeichnung(),
                    LocalDate.parse("1999-06-14")
                );

        Optional<StaedtebaulicheOrientierungswert> result2 =
            this.staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    "Test4",
                    LocalDate.parse("2003-05-05")
                );

        Optional<StaedtebaulicheOrientierungswert> result3 =
            this.staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    "Test5",
                    LocalDate.parse("1980-05-05")
                );

        Optional<StaedtebaulicheOrientierungswert> result4 =
            this.staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    orientierungswert2.getFoerderartBezeichnung(),
                    LocalDate.parse("2005-05-04")
                );

        Assertions.assertThrows(NoSuchElementException.class, () -> result1.get());
        Assertions.assertThrows(NoSuchElementException.class, () -> result2.get());
        Assertions.assertThrows(NoSuchElementException.class, () -> result3.get());
        Assertions.assertThrows(NoSuchElementException.class, () -> result4.get());
    }
}
