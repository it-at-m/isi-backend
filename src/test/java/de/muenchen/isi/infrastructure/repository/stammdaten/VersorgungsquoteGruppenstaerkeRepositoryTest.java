package de.muenchen.isi.infrastructure.repository.stammdaten;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.infrastructure.entity.enums.Bildungseinrichtung;
import de.muenchen.isi.infrastructure.entity.stammdaten.VersorgungsquoteGruppenstaerke;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
public class VersorgungsquoteGruppenstaerkeRepositoryTest {

    @Autowired
    private VersorgungsquoteGruppenstaerkeRepository versorgungsquoteGruppenstaerkeRepository;

    @BeforeEach
    public void beforeEach() {
        this.versorgungsquoteGruppenstaerkeRepository.deleteAll();
    }

    @Test
    void findUmlegungFoerderartenbyBezeichnungAndDatum() {
        VersorgungsquoteGruppenstaerke bildungseinrichtung1 = new VersorgungsquoteGruppenstaerke();
        bildungseinrichtung1.setId(UUID.randomUUID());
        bildungseinrichtung1.setBildungseinrichtung(Bildungseinrichtung.KINDERGARTEN);
        bildungseinrichtung1.setGruppenstaerke(50);
        bildungseinrichtung1.setVersorgungsquotePlanungsursaechlich(new BigDecimal("0.650"));
        bildungseinrichtung1.setVersorgungsquoteSobonUrsaechlich(new BigDecimal("0.650"));
        bildungseinrichtung1.setGueltigAb(LocalDate.parse("2000-01-01"));

        VersorgungsquoteGruppenstaerke bildungseinrichtung2 = new VersorgungsquoteGruppenstaerke();
        bildungseinrichtung2.setId(UUID.randomUUID());
        bildungseinrichtung2.setBildungseinrichtung(Bildungseinrichtung.KINDERKRIPPE);
        bildungseinrichtung2.setGruppenstaerke(75);
        bildungseinrichtung2.setVersorgungsquotePlanungsursaechlich(new BigDecimal("0.750"));
        bildungseinrichtung2.setVersorgungsquoteSobonUrsaechlich(new BigDecimal("0.750"));
        bildungseinrichtung2.setGueltigAb(LocalDate.parse("2005-06-10"));

        VersorgungsquoteGruppenstaerke bildungseinrichtung3 = new VersorgungsquoteGruppenstaerke();
        bildungseinrichtung3.setId(UUID.randomUUID());
        bildungseinrichtung3.setBildungseinrichtung(Bildungseinrichtung.GRUNDSCHULE);
        bildungseinrichtung3.setGruppenstaerke(100);
        bildungseinrichtung3.setVersorgungsquotePlanungsursaechlich(new BigDecimal("0.540"));
        bildungseinrichtung3.setVersorgungsquoteSobonUrsaechlich(new BigDecimal("0.540"));
        bildungseinrichtung3.setGueltigAb(LocalDate.parse("2010-01-01"));

        this.versorgungsquoteGruppenstaerkeRepository.saveAll(
                List.of(bildungseinrichtung1, bildungseinrichtung2, bildungseinrichtung3)
            );

        Optional<VersorgungsquoteGruppenstaerke> result1 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByBildungseinrichtungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung1.getBildungseinrichtung(),
                    LocalDate.parse("2003-05-05")
                );

        Optional<VersorgungsquoteGruppenstaerke> result2 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByBildungseinrichtungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung2.getBildungseinrichtung(),
                    LocalDate.parse("2005-06-10")
                );

        Optional<VersorgungsquoteGruppenstaerke> result3 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByBildungseinrichtungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung2.getBildungseinrichtung(),
                    LocalDate.parse("2007-10-15")
                );

        Optional<VersorgungsquoteGruppenstaerke> result4 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByBildungseinrichtungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung3.getBildungseinrichtung(),
                    LocalDate.parse("2011-06-14")
                );

        Optional<VersorgungsquoteGruppenstaerke> result5 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByBildungseinrichtungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung1.getBildungseinrichtung(),
                    LocalDate.parse("2005-06-10")
                );

        Optional<VersorgungsquoteGruppenstaerke> result6 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByBildungseinrichtungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung2.getBildungseinrichtung(),
                    LocalDate.parse("2005-06-11")
                );

        assertThat(result1.get(), is(bildungseinrichtung1));
        assertThat(result2.get(), is(bildungseinrichtung2));
        assertThat(result3.get(), is(bildungseinrichtung2));
        assertThat(result4.get(), is(bildungseinrichtung3));
        assertThat(result5.get(), is(bildungseinrichtung1));
        assertThat(result6.get(), is(bildungseinrichtung2));
    }

    @Test
    void NoSuchElementExceptionUmlegungFoerderartenbyBezeichnungAndDatum() {
        VersorgungsquoteGruppenstaerke bildungseinrichtung1 = new VersorgungsquoteGruppenstaerke();
        bildungseinrichtung1.setId(UUID.randomUUID());
        bildungseinrichtung1.setBildungseinrichtung(Bildungseinrichtung.KINDERGARTEN);
        bildungseinrichtung1.setGruppenstaerke(50);
        bildungseinrichtung1.setVersorgungsquotePlanungsursaechlich(new BigDecimal("0.650"));
        bildungseinrichtung1.setVersorgungsquoteSobonUrsaechlich(new BigDecimal("0.650"));
        bildungseinrichtung1.setGueltigAb(LocalDate.parse("2000-01-01"));

        VersorgungsquoteGruppenstaerke bildungseinrichtung2 = new VersorgungsquoteGruppenstaerke();
        bildungseinrichtung2.setId(UUID.randomUUID());
        bildungseinrichtung2.setBildungseinrichtung(Bildungseinrichtung.KINDERKRIPPE);
        bildungseinrichtung2.setGruppenstaerke(75);
        bildungseinrichtung2.setVersorgungsquotePlanungsursaechlich(new BigDecimal("0.750"));
        bildungseinrichtung2.setVersorgungsquoteSobonUrsaechlich(new BigDecimal("0.750"));
        bildungseinrichtung2.setGueltigAb(LocalDate.parse("2005-06-10"));

        this.versorgungsquoteGruppenstaerkeRepository.saveAll(List.of(bildungseinrichtung1, bildungseinrichtung2));

        Optional<VersorgungsquoteGruppenstaerke> result1 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByBildungseinrichtungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung1.getBildungseinrichtung(),
                    LocalDate.parse("1999-06-14")
                );

        Optional<VersorgungsquoteGruppenstaerke> result2 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByBildungseinrichtungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung2.getBildungseinrichtung(),
                    LocalDate.parse("2005-05-04")
                );

        Assertions.assertThrows(NoSuchElementException.class, () -> result1.get());
        Assertions.assertThrows(NoSuchElementException.class, () -> result2.get());
    }
}
