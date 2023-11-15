package de.muenchen.isi.infrastructure.repository.stammdaten;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.Einrichtungstyp;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonOrientierungswertSozialeInfrastruktur;
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
            Einrichtungstyp.GRUNDSCHULE,
            Altersklasse.NULL_ZWEI,
            new BigDecimal("1000.1234"),
            new BigDecimal("1500.2345"),
            new BigDecimal("2000.3456"),
            new BigDecimal("2500.4567"),
            new BigDecimal("3000.5678"),
            new BigDecimal("3500.6789"),
            new BigDecimal("4000.7890"),
            new BigDecimal("4500.8901"),
            new BigDecimal("5000.9012"),
            new BigDecimal("5500.0123"),
            new BigDecimal("6000.8965")
        );

        SobonOrientierungswertSozialeInfrastruktur orientierungswert2 = createOrientierungswert(
            "Test2",
            LocalDate.parse("2020-05-05"),
            Einrichtungstyp.KINDERGARTEN,
            Altersklasse.DREI_SECHSEINHALB,
            new BigDecimal("1000.1234"),
            new BigDecimal("2000.2345"),
            new BigDecimal("2500.3456"),
            new BigDecimal("3000.4567"),
            new BigDecimal("3500.5678"),
            new BigDecimal("4000.6789"),
            new BigDecimal("4500.7890"),
            new BigDecimal("5000.8901"),
            new BigDecimal("5500.9012"),
            new BigDecimal("6000.0123"),
            new BigDecimal("6500.1234")
        );

        SobonOrientierungswertSozialeInfrastruktur orientierungswert3 = createOrientierungswert(
            "Test3",
            LocalDate.parse("2019-12-01"),
            Einrichtungstyp.KINDERHORT,
            Altersklasse.SECHSEINHALB_NEUNEINHALB,
            new BigDecimal("1000.1234"),
            new BigDecimal("3000.3456"),
            new BigDecimal("3500.4567"),
            new BigDecimal("4000.5678"),
            new BigDecimal("4500.6789"),
            new BigDecimal("5000.7890"),
            new BigDecimal("5500.8901"),
            new BigDecimal("6000.9012"),
            new BigDecimal("6500.0123"),
            new BigDecimal("7000.1234"),
            new BigDecimal("8000.5134")
        );

        this.sobonOrientierungswertSozialeInfrastrukturRepository.saveAll(
                List.of(orientierungswert1, orientierungswert2, orientierungswert3)
            );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result1 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndAltersklasseAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    orientierungswert1.getEinrichtungstyp(),
                    orientierungswert1.getAltersklasse(),
                    orientierungswert1.getFoerderartBezeichnung(),
                    LocalDate.parse("2022-06-03")
                );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result2 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndAltersklasseAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    orientierungswert2.getEinrichtungstyp(),
                    orientierungswert2.getAltersklasse(),
                    orientierungswert2.getFoerderartBezeichnung(),
                    LocalDate.parse("2020-05-05")
                );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result3 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndAltersklasseAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    orientierungswert3.getEinrichtungstyp(),
                    orientierungswert3.getAltersklasse(),
                    orientierungswert3.getFoerderartBezeichnung(),
                    LocalDate.parse("2020-05-04")
                );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result4 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndAltersklasseAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    Einrichtungstyp.KINDERHORT,
                    Altersklasse.SECHSEINHALB_NEUNEINHALB,
                    orientierungswert3.getFoerderartBezeichnung(),
                    LocalDate.parse("2019-12-01")
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
            Einrichtungstyp.GRUNDSCHULE,
            Altersklasse.NULL_ZWEI,
            new BigDecimal("1000.1234"),
            new BigDecimal("1500.2345"),
            new BigDecimal("2000.3456"),
            new BigDecimal("2500.4567"),
            new BigDecimal("3000.5678"),
            new BigDecimal("3500.6789"),
            new BigDecimal("4000.7890"),
            new BigDecimal("4500.8901"),
            new BigDecimal("5000.9012"),
            new BigDecimal("5500.0123"),
            new BigDecimal("6500.1234")
        );

        SobonOrientierungswertSozialeInfrastruktur orientierungswert2 = createOrientierungswert(
            "Test2",
            LocalDate.parse("2020-05-05"),
            Einrichtungstyp.KINDERGARTEN,
            Altersklasse.DREI_SECHSEINHALB,
            new BigDecimal("1000.1234"),
            new BigDecimal("2000.2345"),
            new BigDecimal("2500.3456"),
            new BigDecimal("3000.4567"),
            new BigDecimal("3500.5678"),
            new BigDecimal("4000.6789"),
            new BigDecimal("4500.7890"),
            new BigDecimal("5000.8901"),
            new BigDecimal("5500.9012"),
            new BigDecimal("6000.0123"),
            new BigDecimal("7000.7913")
        );

        SobonOrientierungswertSozialeInfrastruktur orientierungswert3 = createOrientierungswert(
            "Test3",
            LocalDate.parse("2019-12-01"),
            Einrichtungstyp.KINDERHORT,
            Altersklasse.SECHSEINHALB_NEUNEINHALB,
            new BigDecimal("1000.1234"),
            new BigDecimal("3000.3456"),
            new BigDecimal("3500.4567"),
            new BigDecimal("4000.5678"),
            new BigDecimal("4500.6789"),
            new BigDecimal("5000.7890"),
            new BigDecimal("5500.8901"),
            new BigDecimal("6000.9012"),
            new BigDecimal("6500.0123"),
            new BigDecimal("7000.1234"),
            new BigDecimal("8200.4323")
        );

        this.sobonOrientierungswertSozialeInfrastrukturRepository.saveAll(
                List.of(orientierungswert1, orientierungswert2, orientierungswert3)
            );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result1 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndAltersklasseAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    orientierungswert1.getEinrichtungstyp(),
                    orientierungswert1.getAltersklasse(),
                    orientierungswert1.getFoerderartBezeichnung(),
                    LocalDate.parse("2019-12-31")
                );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result2 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndAltersklasseAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    Einrichtungstyp.N_N,
                    orientierungswert1.getAltersklasse(),
                    orientierungswert1.getFoerderartBezeichnung(),
                    orientierungswert1.getGueltigAb()
                );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result3 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndAltersklasseAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    orientierungswert1.getEinrichtungstyp(),
                    Altersklasse.ZEHNEINHALB_FUENFZEHN,
                    orientierungswert1.getFoerderartBezeichnung(),
                    orientierungswert1.getGueltigAb()
                );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result4 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndAltersklasseAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    orientierungswert1.getEinrichtungstyp(),
                    orientierungswert1.getAltersklasse(),
                    "NonexistentFoerderart",
                    orientierungswert1.getGueltigAb()
                );

        Optional<SobonOrientierungswertSozialeInfrastruktur> result6 =
            this.sobonOrientierungswertSozialeInfrastrukturRepository.findFirstByEinrichtungstypAndAltersklasseAndFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    Einrichtungstyp.KINDERHORT,
                    Altersklasse.DREI_SECHSEINHALB,
                    orientierungswert3.getFoerderartBezeichnung(),
                    LocalDate.parse("2020-01-01")
                );

        Assertions.assertThrows(NoSuchElementException.class, () -> result1.get());
        Assertions.assertThrows(NoSuchElementException.class, () -> result2.get());
        Assertions.assertThrows(NoSuchElementException.class, () -> result3.get());
        Assertions.assertThrows(NoSuchElementException.class, () -> result4.get());
        Assertions.assertThrows(NoSuchElementException.class, () -> result6.get());
    }

    private SobonOrientierungswertSozialeInfrastruktur createOrientierungswert(
        String foerderartBezeichnung,
        LocalDate gueltigAb,
        Einrichtungstyp einrichtungstyp,
        Altersklasse altersklasse,
        BigDecimal einwohnerJahr1NachErsterstellung,
        BigDecimal einwohnerJahr2NachErsterstellung,
        BigDecimal einwohnerJahr3NachErsterstellung,
        BigDecimal einwohnerJahr4NachErsterstellung,
        BigDecimal einwohnerJahr5NachErsterstellung,
        BigDecimal einwohnerJahr6NachErsterstellung,
        BigDecimal einwohnerJahr7NachErsterstellung,
        BigDecimal einwohnerJahr8NachErsterstellung,
        BigDecimal einwohnerJahr9NachErsterstellung,
        BigDecimal einwohnerJahr10NachErsterstellung,
        BigDecimal stammwertArbeitsgruppe
    ) {
        SobonOrientierungswertSozialeInfrastruktur orientierungswert = new SobonOrientierungswertSozialeInfrastruktur();
        orientierungswert.setId(UUID.randomUUID());
        orientierungswert.setGueltigAb(gueltigAb);
        orientierungswert.setEinrichtungstyp(einrichtungstyp);
        orientierungswert.setAltersklasse(altersklasse);
        orientierungswert.setFoerderartBezeichnung(foerderartBezeichnung);
        orientierungswert.setEinwohnerJahr1NachErsterstellung(einwohnerJahr1NachErsterstellung);
        orientierungswert.setEinwohnerJahr2NachErsterstellung(einwohnerJahr2NachErsterstellung);
        orientierungswert.setEinwohnerJahr3NachErsterstellung(einwohnerJahr3NachErsterstellung);
        orientierungswert.setEinwohnerJahr4NachErsterstellung(einwohnerJahr4NachErsterstellung);
        orientierungswert.setEinwohnerJahr5NachErsterstellung(einwohnerJahr5NachErsterstellung);
        orientierungswert.setEinwohnerJahr6NachErsterstellung(einwohnerJahr6NachErsterstellung);
        orientierungswert.setEinwohnerJahr7NachErsterstellung(einwohnerJahr7NachErsterstellung);
        orientierungswert.setEinwohnerJahr8NachErsterstellung(einwohnerJahr8NachErsterstellung);
        orientierungswert.setEinwohnerJahr9NachErsterstellung(einwohnerJahr9NachErsterstellung);
        orientierungswert.setEinwohnerJahr10NachErsterstellung(einwohnerJahr10NachErsterstellung);
        orientierungswert.setStammwertArbeitsgruppe(stammwertArbeitsgruppe);
        return orientierungswert;
    }
}
