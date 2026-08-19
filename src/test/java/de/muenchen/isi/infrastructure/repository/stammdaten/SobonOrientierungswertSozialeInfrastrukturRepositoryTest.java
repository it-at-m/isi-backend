package de.muenchen.isi.infrastructure.repository.stammdaten;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonOrientierungswertSozialeInfrastruktur;
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
public class SobonOrientierungswertSozialeInfrastrukturRepositoryTest {

    @Autowired
    private SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository;

    @BeforeEach
    public void beforeEach() {
        this.sobonOrientierungswertSozialeInfrastrukturRepository.deleteAll();
    }

    @Test
    void findSobonOrientierungswertSozialeInfrastruktur() {
        SobonOrientierungswertSozialeInfrastruktur orientierungswert1 = createOrientierungswert(
            "Test1",
            LocalDate.parse("2021-01-01"),
            "2021",
            InfrastruktureinrichtungTyp.GRUNDSCHULE,
            Altersklasse.NULL_ZWEI,
            new BigDecimal("1000.123400000000000"),
            new BigDecimal("1500.234500000000000"),
            new BigDecimal("2000.345600000000000"),
            new BigDecimal("2500.456700000000000"),
            new BigDecimal("3000.567800000000000"),
            new BigDecimal("3500.678900000000000"),
            new BigDecimal("4000.789000000000000"),
            new BigDecimal("4500.890100000000000"),
            new BigDecimal("5000.901200000000000"),
            new BigDecimal("5500.012300000000000"),
            new BigDecimal("6000.896500000000000")
        );

        SobonOrientierungswertSozialeInfrastruktur orientierungswert2 = createOrientierungswert(
            "Test2",
            LocalDate.parse("2020-05-05"),
            "2020",
            InfrastruktureinrichtungTyp.KINDERGARTEN,
            Altersklasse.DREI_SECHSEINHALB,
            new BigDecimal("1000.123400000000000"),
            new BigDecimal("2000.234500000000000"),
            new BigDecimal("2500.345600000000000"),
            new BigDecimal("3000.456700000000000"),
            new BigDecimal("3500.567800000000000"),
            new BigDecimal("4000.678900000000000"),
            new BigDecimal("4500.789000000000000"),
            new BigDecimal("5000.890100000000000"),
            new BigDecimal("5500.901200000000000"),
            new BigDecimal("6000.012300000000000"),
            new BigDecimal("6500.123400000000000")
        );

        SobonOrientierungswertSozialeInfrastruktur orientierungswert3 = createOrientierungswert(
            "Test3",
            LocalDate.parse("2019-12-01"),
            "2019",
            InfrastruktureinrichtungTyp.GS_NACHMITTAG_BETREUUNG,
            Altersklasse.SECHSEINHALB_NEUNEINHALB,
            new BigDecimal("1000.123400000000000"),
            new BigDecimal("3000.345600000000000"),
            new BigDecimal("3500.456700000000000"),
            new BigDecimal("4000.567800000000000"),
            new BigDecimal("4500.678900000000000"),
            new BigDecimal("5000.789000000000000"),
            new BigDecimal("5500.890100000000000"),
            new BigDecimal("6000.901200000000000"),
            new BigDecimal("6500.012300000000000"),
            new BigDecimal("7000.123400000000000"),
            new BigDecimal("8000.513400000000000")
        );

        this.sobonOrientierungswertSozialeInfrastrukturRepository.saveAll(
            List.of(orientierungswert1, orientierungswert2, orientierungswert3)
        );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result1 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndJahrBezeichnung(
                orientierungswert1.getEinrichtungstyp(),
                orientierungswert1.getFoerderartBezeichnung(),
                "2021"
            );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result2 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndJahrBezeichnung(
                orientierungswert2.getEinrichtungstyp(),
                orientierungswert2.getFoerderartBezeichnung(),
                "2020"
            );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result3 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndJahrBezeichnung(
                orientierungswert3.getEinrichtungstyp(),
                orientierungswert3.getFoerderartBezeichnung(),
                "2019"
            );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result4 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndJahrBezeichnung(
                InfrastruktureinrichtungTyp.GS_NACHMITTAG_BETREUUNG,
                orientierungswert3.getFoerderartBezeichnung(),
                "2019"
            );

        assertThat(result1.isPresent(), is(true));
        assertThat(result1.get(), is(orientierungswert1));

        assertThat(result2.isPresent(), is(true));
        assertThat(result2.get(), is(orientierungswert2));

        assertThat(result3.isPresent(), is(true));
        assertThat(result3.get(), is(orientierungswert3));

        assertThat(result4.isPresent(), is(true));
        assertThat(result4.get(), is(orientierungswert3));
    }

    @Test
    void NoSuchElementExceptionSobonOrientierungswertSozialeInfrastruktur() {
        SobonOrientierungswertSozialeInfrastruktur orientierungswert1 = createOrientierungswert(
            "Test1",
            LocalDate.parse("2021-01-01"),
            "2021",
            InfrastruktureinrichtungTyp.GRUNDSCHULE,
            Altersklasse.NULL_ZWEI,
            new BigDecimal("1000.123400000000000"),
            new BigDecimal("1500.234500000000000"),
            new BigDecimal("2000.345600000000000"),
            new BigDecimal("2500.456700000000000"),
            new BigDecimal("3000.567800000000000"),
            new BigDecimal("3500.678900000000000"),
            new BigDecimal("4000.789000000000000"),
            new BigDecimal("4500.890100000000000"),
            new BigDecimal("5000.901200000000000"),
            new BigDecimal("5500.012300000000000"),
            new BigDecimal("6500.123400000000000")
        );

        SobonOrientierungswertSozialeInfrastruktur orientierungswert2 = createOrientierungswert(
            "Test2",
            LocalDate.parse("2020-05-05"),
            "2020",
            InfrastruktureinrichtungTyp.KINDERGARTEN,
            Altersklasse.DREI_SECHSEINHALB,
            new BigDecimal("1000.123400000000000"),
            new BigDecimal("2000.234500000000000"),
            new BigDecimal("2500.345600000000000"),
            new BigDecimal("3000.456700000000000"),
            new BigDecimal("3500.567800000000000"),
            new BigDecimal("4000.678900000000000"),
            new BigDecimal("4500.789000000000000"),
            new BigDecimal("5000.890100000000000"),
            new BigDecimal("5500.901200000000000"),
            new BigDecimal("6000.012300000000000"),
            new BigDecimal("7000.791300000000000")
        );

        SobonOrientierungswertSozialeInfrastruktur orientierungswert3 = createOrientierungswert(
            "Test3",
            LocalDate.parse("2019-12-01"),
            "2019",
            InfrastruktureinrichtungTyp.GS_NACHMITTAG_BETREUUNG,
            Altersklasse.SECHSEINHALB_NEUNEINHALB,
            new BigDecimal("1000.123400000000000"),
            new BigDecimal("3000.345600000000000"),
            new BigDecimal("3500.456700000000000"),
            new BigDecimal("4000.567800000000000"),
            new BigDecimal("4500.678900000000000"),
            new BigDecimal("5000.789000000000000"),
            new BigDecimal("5500.890100000000000"),
            new BigDecimal("6000.901200000000000"),
            new BigDecimal("6500.012300000000000"),
            new BigDecimal("7000.123400000000000"),
            new BigDecimal("8200.432300000000000")
        );

        this.sobonOrientierungswertSozialeInfrastrukturRepository.saveAll(
            List.of(orientierungswert1, orientierungswert2, orientierungswert3)
        );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result1 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndJahrBezeichnung(
                orientierungswert1.getEinrichtungstyp(),
                orientierungswert1.getFoerderartBezeichnung(),
                "2019"
            );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result2 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndJahrBezeichnung(
                InfrastruktureinrichtungTyp.UNSPECIFIED,
                orientierungswert1.getFoerderartBezeichnung(),
                orientierungswert1.getJahrBezeichnung()
            );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result4 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndJahrBezeichnung(
                orientierungswert1.getEinrichtungstyp(),
                "NonexistentFoerderart",
                orientierungswert1.getJahrBezeichnung()
            );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result6 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndFoerderartBezeichnungAndJahrBezeichnung(
                InfrastruktureinrichtungTyp.GS_NACHMITTAG_BETREUUNG,
                orientierungswert3.getFoerderartBezeichnung(),
                "2020"
            );

        Assertions.assertThrows(NoSuchElementException.class, () -> result1.get());
        Assertions.assertThrows(NoSuchElementException.class, () -> result2.get());
        Assertions.assertThrows(NoSuchElementException.class, () -> result4.get());
    }

    private SobonOrientierungswertSozialeInfrastruktur createOrientierungswert(
        String foerderartBezeichnung,
        LocalDate gueltigAb,
        String jahrBezeichnung,
        InfrastruktureinrichtungTyp einrichtungstyp,
        Altersklasse altersklasse,
        BigDecimal einwohnerJahr1nachErsterstellung,
        BigDecimal einwohnerJahr2nachErsterstellung,
        BigDecimal einwohnerJahr3nachErsterstellung,
        BigDecimal einwohnerJahr4nachErsterstellung,
        BigDecimal einwohnerJahr5nachErsterstellung,
        BigDecimal einwohnerJahr6nachErsterstellung,
        BigDecimal einwohnerJahr7nachErsterstellung,
        BigDecimal einwohnerJahr8nachErsterstellung,
        BigDecimal einwohnerJahr9nachErsterstellung,
        BigDecimal einwohnerJahr10nachErsterstellung,
        BigDecimal stammwertArbeitsgruppe
    ) {
        SobonOrientierungswertSozialeInfrastruktur orientierungswert = new SobonOrientierungswertSozialeInfrastruktur();
        orientierungswert.setGueltigAb(gueltigAb);
        orientierungswert.setJahrBezeichnung(jahrBezeichnung);
        orientierungswert.setEinrichtungstyp(einrichtungstyp);
        orientierungswert.setAltersklasse(altersklasse);
        orientierungswert.setFoerderartBezeichnung(foerderartBezeichnung);
        orientierungswert.setEinwohnerJahr1nachErsterstellung(einwohnerJahr1nachErsterstellung);
        orientierungswert.setEinwohnerJahr2nachErsterstellung(einwohnerJahr2nachErsterstellung);
        orientierungswert.setEinwohnerJahr3nachErsterstellung(einwohnerJahr3nachErsterstellung);
        orientierungswert.setEinwohnerJahr4nachErsterstellung(einwohnerJahr4nachErsterstellung);
        orientierungswert.setEinwohnerJahr5nachErsterstellung(einwohnerJahr5nachErsterstellung);
        orientierungswert.setEinwohnerJahr6nachErsterstellung(einwohnerJahr6nachErsterstellung);
        orientierungswert.setEinwohnerJahr7nachErsterstellung(einwohnerJahr7nachErsterstellung);
        orientierungswert.setEinwohnerJahr8nachErsterstellung(einwohnerJahr8nachErsterstellung);
        orientierungswert.setEinwohnerJahr9nachErsterstellung(einwohnerJahr9nachErsterstellung);
        orientierungswert.setEinwohnerJahr10nachErsterstellung(einwohnerJahr10nachErsterstellung);
        orientierungswert.setStammwertArbeitsgruppe(stammwertArbeitsgruppe);
        return orientierungswert;
    }
}
