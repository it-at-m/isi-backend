package de.muenchen.isi.infrastructure.repository.stammdaten;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
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
import org.springframework.dao.DataIntegrityViolationException;
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
        bildungseinrichtung1.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERGARTEN);
        bildungseinrichtung1.setGruppenstaerke(50);
        bildungseinrichtung1.setVersorgungsquotePlanungsursaechlich(new BigDecimal("0.650"));
        bildungseinrichtung1.setVersorgungsquoteSobonUrsaechlich(new BigDecimal("0.650"));
        bildungseinrichtung1.setGueltigAb(LocalDate.parse("2000-01-01"));

        VersorgungsquoteGruppenstaerke bildungseinrichtung2 = new VersorgungsquoteGruppenstaerke();
        bildungseinrichtung2.setId(UUID.randomUUID());
        bildungseinrichtung2.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        bildungseinrichtung2.setGruppenstaerke(75);
        bildungseinrichtung2.setVersorgungsquotePlanungsursaechlich(new BigDecimal("0.750"));
        bildungseinrichtung2.setVersorgungsquoteSobonUrsaechlich(new BigDecimal("0.750"));
        bildungseinrichtung2.setGueltigAb(LocalDate.parse("2005-06-10"));

        VersorgungsquoteGruppenstaerke bildungseinrichtung3 = new VersorgungsquoteGruppenstaerke();
        bildungseinrichtung3.setId(UUID.randomUUID());
        bildungseinrichtung3.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.GRUNDSCHULE);
        bildungseinrichtung3.setGruppenstaerke(100);
        bildungseinrichtung3.setVersorgungsquotePlanungsursaechlich(new BigDecimal("0.540"));
        bildungseinrichtung3.setVersorgungsquoteSobonUrsaechlich(new BigDecimal("0.540"));
        bildungseinrichtung3.setGueltigAb(LocalDate.parse("2010-01-01"));

        this.versorgungsquoteGruppenstaerkeRepository.saveAll(
                List.of(bildungseinrichtung1, bildungseinrichtung2, bildungseinrichtung3)
            );

        Optional<VersorgungsquoteGruppenstaerke> result1 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByInfrastruktureinrichtungTypAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung1.getInfrastruktureinrichtungTyp(),
                    LocalDate.parse("2003-05-05")
                );

        Optional<VersorgungsquoteGruppenstaerke> result2 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByInfrastruktureinrichtungTypAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung2.getInfrastruktureinrichtungTyp(),
                    LocalDate.parse("2005-06-10")
                );

        Optional<VersorgungsquoteGruppenstaerke> result3 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByInfrastruktureinrichtungTypAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung2.getInfrastruktureinrichtungTyp(),
                    LocalDate.parse("2007-10-15")
                );

        Optional<VersorgungsquoteGruppenstaerke> result4 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByInfrastruktureinrichtungTypAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung3.getInfrastruktureinrichtungTyp(),
                    LocalDate.parse("2011-06-14")
                );

        Optional<VersorgungsquoteGruppenstaerke> result5 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByInfrastruktureinrichtungTypAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung1.getInfrastruktureinrichtungTyp(),
                    LocalDate.parse("2005-06-10")
                );

        Optional<VersorgungsquoteGruppenstaerke> result6 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByInfrastruktureinrichtungTypAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung2.getInfrastruktureinrichtungTyp(),
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
    void noSuchElementExceptionUmlegungFoerderartenbyBezeichnungAndDatum() {
        VersorgungsquoteGruppenstaerke bildungseinrichtung1 = new VersorgungsquoteGruppenstaerke();
        bildungseinrichtung1.setId(UUID.randomUUID());
        bildungseinrichtung1.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERGARTEN);
        bildungseinrichtung1.setGruppenstaerke(50);
        bildungseinrichtung1.setVersorgungsquotePlanungsursaechlich(new BigDecimal("0.650"));
        bildungseinrichtung1.setVersorgungsquoteSobonUrsaechlich(new BigDecimal("0.650"));
        bildungseinrichtung1.setGueltigAb(LocalDate.parse("2000-01-01"));

        VersorgungsquoteGruppenstaerke bildungseinrichtung2 = new VersorgungsquoteGruppenstaerke();
        bildungseinrichtung2.setId(UUID.randomUUID());
        bildungseinrichtung2.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        bildungseinrichtung2.setGruppenstaerke(75);
        bildungseinrichtung2.setVersorgungsquotePlanungsursaechlich(new BigDecimal("0.750"));
        bildungseinrichtung2.setVersorgungsquoteSobonUrsaechlich(new BigDecimal("0.750"));
        bildungseinrichtung2.setGueltigAb(LocalDate.parse("2005-06-10"));

        this.versorgungsquoteGruppenstaerkeRepository.saveAll(List.of(bildungseinrichtung1, bildungseinrichtung2));

        Optional<VersorgungsquoteGruppenstaerke> result1 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByInfrastruktureinrichtungTypAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung1.getInfrastruktureinrichtungTyp(),
                    LocalDate.parse("1999-06-14")
                );

        Optional<VersorgungsquoteGruppenstaerke> result2 =
            this.versorgungsquoteGruppenstaerkeRepository.findFirstByInfrastruktureinrichtungTypAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    bildungseinrichtung2.getInfrastruktureinrichtungTyp(),
                    LocalDate.parse("2005-05-04")
                );

        Assertions.assertThrows(NoSuchElementException.class, () -> result1.get());
        Assertions.assertThrows(NoSuchElementException.class, () -> result2.get());
    }

    @Test
    void percentValicationCheckTest() {
        VersorgungsquoteGruppenstaerke value0 = createBildungseinrichtung(
            new BigDecimal("0.500"),
            LocalDate.parse("2000-01-01")
        );
        Assertions.assertDoesNotThrow(() -> this.versorgungsquoteGruppenstaerkeRepository.save(value0));
        VersorgungsquoteGruppenstaerke value1 = createBildungseinrichtung(
            new BigDecimal("0"),
            LocalDate.parse("2001-01-01")
        );
        Assertions.assertDoesNotThrow(() -> this.versorgungsquoteGruppenstaerkeRepository.save(value1));
        VersorgungsquoteGruppenstaerke value2 = createBildungseinrichtung(
            new BigDecimal("0.0"),
            LocalDate.parse("2002-01-01")
        );
        Assertions.assertDoesNotThrow(() -> this.versorgungsquoteGruppenstaerkeRepository.save(value2));
        VersorgungsquoteGruppenstaerke value3 = createBildungseinrichtung(
            new BigDecimal("0.00"),
            LocalDate.parse("2003-01-01")
        );
        Assertions.assertDoesNotThrow(() -> this.versorgungsquoteGruppenstaerkeRepository.save(value3));
        VersorgungsquoteGruppenstaerke value4 = createBildungseinrichtung(
            new BigDecimal("0.000"),
            LocalDate.parse("2004-01-01")
        );
        Assertions.assertDoesNotThrow(() -> this.versorgungsquoteGruppenstaerkeRepository.save(value4));
        VersorgungsquoteGruppenstaerke value5 = createBildungseinrichtung(
            new BigDecimal("1"),
            LocalDate.parse("2005-01-01")
        );
        Assertions.assertDoesNotThrow(() -> this.versorgungsquoteGruppenstaerkeRepository.save(value5));
        VersorgungsquoteGruppenstaerke value6 = createBildungseinrichtung(
            new BigDecimal("1.0"),
            LocalDate.parse("2006-01-01")
        );
        Assertions.assertDoesNotThrow(() -> this.versorgungsquoteGruppenstaerkeRepository.save(value6));
        VersorgungsquoteGruppenstaerke value7 = createBildungseinrichtung(
            new BigDecimal("1.00"),
            LocalDate.parse("2007-01-01")
        );
        Assertions.assertDoesNotThrow(() -> this.versorgungsquoteGruppenstaerkeRepository.save(value7));
        VersorgungsquoteGruppenstaerke value8 = createBildungseinrichtung(
            new BigDecimal("1.000"),
            LocalDate.parse("2008-01-01")
        );
        Assertions.assertDoesNotThrow(() -> this.versorgungsquoteGruppenstaerkeRepository.save(value8));
        VersorgungsquoteGruppenstaerke value9 = createBildungseinrichtung(
            new BigDecimal("1.01"),
            LocalDate.parse("2009-01-01")
        );
        Assertions.assertThrows(
            DataIntegrityViolationException.class,
            () -> this.versorgungsquoteGruppenstaerkeRepository.save(value9)
        );
        VersorgungsquoteGruppenstaerke value10 = createBildungseinrichtung(
            new BigDecimal("1.1"),
            LocalDate.parse("2010-01-01")
        );
        Assertions.assertThrows(
            DataIntegrityViolationException.class,
            () -> this.versorgungsquoteGruppenstaerkeRepository.save(value10)
        );
        VersorgungsquoteGruppenstaerke value11 = createBildungseinrichtung(
            new BigDecimal("1.001"),
            LocalDate.parse("2011-01-01")
        );
        Assertions.assertThrows(
            DataIntegrityViolationException.class,
            () -> this.versorgungsquoteGruppenstaerkeRepository.save(value11)
        );
        VersorgungsquoteGruppenstaerke value12 = createBildungseinrichtung(
            new BigDecimal("-0.001"),
            LocalDate.parse("2012-01-01")
        );
        Assertions.assertThrows(
            DataIntegrityViolationException.class,
            () -> this.versorgungsquoteGruppenstaerkeRepository.save(value12)
        );
        VersorgungsquoteGruppenstaerke value13 = createBildungseinrichtung(
            new BigDecimal("-0.01"),
            LocalDate.parse("2013-01-01")
        );
        Assertions.assertThrows(
            DataIntegrityViolationException.class,
            () -> this.versorgungsquoteGruppenstaerkeRepository.save(value13)
        );
        VersorgungsquoteGruppenstaerke value14 = createBildungseinrichtung(
            new BigDecimal("-0.1"),
            LocalDate.parse("2014-01-01")
        );
        Assertions.assertThrows(
            DataIntegrityViolationException.class,
            () -> this.versorgungsquoteGruppenstaerkeRepository.save(value14)
        );
    }

    private VersorgungsquoteGruppenstaerke createBildungseinrichtung(BigDecimal value, LocalDate gueltigAb) {
        VersorgungsquoteGruppenstaerke versorgungsquoteGruppenstaerke = new VersorgungsquoteGruppenstaerke();
        versorgungsquoteGruppenstaerke.setId(UUID.randomUUID());
        versorgungsquoteGruppenstaerke.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        versorgungsquoteGruppenstaerke.setGruppenstaerke(50);
        versorgungsquoteGruppenstaerke.setVersorgungsquotePlanungsursaechlich(value);
        versorgungsquoteGruppenstaerke.setVersorgungsquoteSobonUrsaechlich(value);
        versorgungsquoteGruppenstaerke.setGueltigAb(gueltigAb);
        return versorgungsquoteGruppenstaerke;
    }
}
