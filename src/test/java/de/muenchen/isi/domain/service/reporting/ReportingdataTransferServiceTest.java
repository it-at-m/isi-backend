package de.muenchen.isi.domain.service.reporting;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.ReportingException;
import de.muenchen.isi.domain.mapper.ReportingApiDomainMapper;
import de.muenchen.isi.domain.mapper.ReportingApiDomainMapperImpl;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.calculation.BedarfeForAbfragevarianteModel;
import de.muenchen.isi.domain.model.calculation.InfrastrukturbedarfProJahrModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerBedarfModel;
import de.muenchen.isi.domain.model.calculation.PersonenProJahrModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.repository.reporting.ReportingdataTransferRepository;
import de.muenchen.isi.reporting.client.model.AbfrageDto;
import de.muenchen.isi.reporting.client.model.AbfragevarianteBauleitplanverfahrenDto;
import de.muenchen.isi.reporting.client.model.BaugenehmigungsverfahrenDto;
import de.muenchen.isi.reporting.client.model.BauleitplanverfahrenDto;
import de.muenchen.isi.reporting.client.model.InfrastrukturbedarfProJahrDto;
import de.muenchen.isi.reporting.client.model.LangfristigerBedarfDto;
import de.muenchen.isi.reporting.client.model.PersonenProJahrDto;
import de.muenchen.isi.reporting.client.model.WeiteresVerfahrenDto;
import de.muenchen.isi.reporting.client.model.WohneinheitenProFoerderartProJahrDto;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReportingdataTransferServiceTest {

    private ReportingdataTransferService reportingdataTransferService;

    private ReportingApiDomainMapper reportingApiDomainMapper = new ReportingApiDomainMapperImpl();

    @Mock
    private ReportingdataTransferRepository reportingdataTransferRepository;

    @BeforeEach
    public void beforeEach() {
        this.reportingdataTransferService =
            Mockito.spy(new ReportingdataTransferService(reportingApiDomainMapper, reportingdataTransferRepository));
        Mockito.reset(reportingdataTransferRepository);
    }

    @Test
    void transferAbfrageAndBedarfeBauleitplanverfahren() throws ReportingException {
        var abfrageModel = new BauleitplanverfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new BauleitplanverfahrenDto();
        abfrageReportingDto.setAbfragevariantenBauleitplanverfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.BAULEITPLANVERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenReturn(abfrageReportingDto);
        reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante);
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(1)).transferAbfrage(abfrageReportingDto);
    }

    @Test
    void transferAbfrageAndBedarfeExceptionAtAddBedarfeToAbfrageBauleitplanverfahren() throws ReportingException {
        var abfrageModel = new BauleitplanverfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new BauleitplanverfahrenDto();
        abfrageReportingDto.setAbfragevariantenBauleitplanverfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.BAULEITPLANVERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenThrow(new ReportingException("test"));
        Assertions.assertThrows(
            ReportingException.class,
            () -> reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante)
        );
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(0)).transferAbfrage(Mockito.any());
    }

    @Test
    void transferAbfrageAndBedarfeExceptionAtTransferAbfrageBauleitplanverfahren() throws ReportingException {
        var abfrageModel = new BauleitplanverfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new BauleitplanverfahrenDto();
        abfrageReportingDto.setAbfragevariantenBauleitplanverfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.BAULEITPLANVERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenReturn(abfrageReportingDto);
        Mockito
            .doThrow(new ReportingException("test"))
            .when(reportingdataTransferService)
            .transferAbfrage(abfrageReportingDto);
        Assertions.assertThrows(
            ReportingException.class,
            () -> reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante)
        );
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(1)).transferAbfrage(abfrageReportingDto);
    }

    @Test
    void transferAbfrageAndBedarfeBaugenehmigungsverfahren() throws ReportingException {
        var abfrageModel = new BaugenehmigungsverfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new BaugenehmigungsverfahrenDto();
        abfrageReportingDto.setAbfragevariantenBaugenehmigungsverfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.BAUGENEHMIGUNGSVERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenReturn(abfrageReportingDto);
        reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante);
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(1)).transferAbfrage(abfrageReportingDto);
    }

    @Test
    void transferAbfrageAndBedarfeExceptionAtAddBedarfeToAbfrageBaugenehmigungsverfahren() throws ReportingException {
        var abfrageModel = new BaugenehmigungsverfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new BaugenehmigungsverfahrenDto();
        abfrageReportingDto.setAbfragevariantenBaugenehmigungsverfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.BAUGENEHMIGUNGSVERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenThrow(new ReportingException("test"));
        Assertions.assertThrows(
            ReportingException.class,
            () -> reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante)
        );
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(0)).transferAbfrage(Mockito.any());
    }

    @Test
    void transferAbfrageAndBedarfeExceptionAtTransferAbfrageBaugenehmigungsverfahren() throws ReportingException {
        var abfrageModel = new BaugenehmigungsverfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new BaugenehmigungsverfahrenDto();
        abfrageReportingDto.setAbfragevariantenBaugenehmigungsverfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.BAUGENEHMIGUNGSVERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenReturn(abfrageReportingDto);
        Mockito
            .doThrow(new ReportingException("test"))
            .when(reportingdataTransferService)
            .transferAbfrage(abfrageReportingDto);
        Assertions.assertThrows(
            ReportingException.class,
            () -> reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante)
        );
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(1)).transferAbfrage(abfrageReportingDto);
    }

    @Test
    void transferAbfrageAndBedarfeWeitersVerfahren() throws ReportingException {
        var abfrageModel = new WeiteresVerfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new WeiteresVerfahrenDto();
        abfrageReportingDto.setAbfragevariantenWeiteresVerfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.WEITERES_VERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenReturn(abfrageReportingDto);
        reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante);
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(1)).transferAbfrage(abfrageReportingDto);
    }

    @Test
    void transferAbfrageAndBedarfeExceptionAtAddBedarfeToAbfrageWeitersVerfahren() throws ReportingException {
        var abfrageModel = new WeiteresVerfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new WeiteresVerfahrenDto();
        abfrageReportingDto.setAbfragevariantenWeiteresVerfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.WEITERES_VERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenThrow(new ReportingException("test"));
        Assertions.assertThrows(
            ReportingException.class,
            () -> reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante)
        );
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(0)).transferAbfrage(Mockito.any());
    }

    @Test
    void transferAbfrageAndBedarfeExceptionAtTransferAbfrageWeitersVerfahren() throws ReportingException {
        var abfrageModel = new WeiteresVerfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new WeiteresVerfahrenDto();
        abfrageReportingDto.setAbfragevariantenWeiteresVerfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.WEITERES_VERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenReturn(abfrageReportingDto);
        Mockito
            .doThrow(new ReportingException("test"))
            .when(reportingdataTransferService)
            .transferAbfrage(abfrageReportingDto);
        Assertions.assertThrows(
            ReportingException.class,
            () -> reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante)
        );
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(1)).transferAbfrage(abfrageReportingDto);
    }

    @Test
    void addBedarfeToAbfrageBauleitplanverfahren() throws ReportingException {
        var abfrage = new BauleitplanverfahrenDto();
        abfrage.setId(UUID.randomUUID());
        abfrage.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.BAULEITPLANVERFAHREN);
        var abfragevariante1 = new AbfragevarianteBauleitplanverfahrenDto();
        abfragevariante1.setId(UUID.randomUUID());
        abfragevariante1.setName("Variante 1");
        abfragevariante1.setAbfragevariantenNr(1);
        var abfragevariante2 = new AbfragevarianteBauleitplanverfahrenDto();
        abfragevariante2.setId(UUID.randomUUID());
        abfragevariante2.setName("Variante 2");
        abfragevariante2.setAbfragevariantenNr(2);
        abfrage.setAbfragevariantenBauleitplanverfahren(List.of(abfragevariante1));
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevariante2));

        var bedarf = new BedarfeForAbfragevarianteModel();
        var planungsursaechlichBedarf = new LangfristigerBedarfModel();
        var wohneinheiten = new WohneinheitenProFoerderartProJahrModel();
        wohneinheiten.setJahr("2020");
        wohneinheiten.setFoerderart("forderart1");
        wohneinheiten.setWohneinheiten(BigDecimal.valueOf(10));
        planungsursaechlichBedarf.setWohneinheiten(List.of(wohneinheiten));
        var infrastrukturbedarf = new InfrastrukturbedarfProJahrModel();
        infrastrukturbedarf.setJahr("2021");
        infrastrukturbedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(100));
        infrastrukturbedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(80));
        infrastrukturbedarf.setAnzahlGruppen(BigDecimal.valueOf(10));
        planungsursaechlichBedarf.setBedarfKinderkrippe(List.of(infrastrukturbedarf));
        infrastrukturbedarf = new InfrastrukturbedarfProJahrModel();
        infrastrukturbedarf.setJahr("2022");
        infrastrukturbedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(101));
        infrastrukturbedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(81));
        infrastrukturbedarf.setAnzahlGruppen(BigDecimal.valueOf(11));
        planungsursaechlichBedarf.setBedarfKindergarten(List.of(infrastrukturbedarf));
        var personen = new PersonenProJahrModel();
        personen.setJahr("2023");
        personen.setAnzahlPersonenGesamt(BigDecimal.valueOf(102));
        planungsursaechlichBedarf.setAlleEinwohner(List.of(personen));
        bedarf.setLangfristigerPlanungsursaechlicherBedarf(planungsursaechlichBedarf);

        var sobonursaechlichBedarf = new LangfristigerBedarfModel();
        wohneinheiten = new WohneinheitenProFoerderartProJahrModel();
        wohneinheiten.setJahr("2024");
        wohneinheiten.setFoerderart("forderart2");
        wohneinheiten.setWohneinheiten(BigDecimal.valueOf(20));
        sobonursaechlichBedarf.setWohneinheiten(List.of(wohneinheiten));
        infrastrukturbedarf = new InfrastrukturbedarfProJahrModel();
        infrastrukturbedarf.setJahr("2025");
        infrastrukturbedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(200));
        infrastrukturbedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(70));
        infrastrukturbedarf.setAnzahlGruppen(BigDecimal.valueOf(5));
        sobonursaechlichBedarf.setBedarfKinderkrippe(List.of(infrastrukturbedarf));
        infrastrukturbedarf = new InfrastrukturbedarfProJahrModel();
        infrastrukturbedarf.setJahr("2026");
        infrastrukturbedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(201));
        infrastrukturbedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(71));
        infrastrukturbedarf.setAnzahlGruppen(BigDecimal.valueOf(6));
        sobonursaechlichBedarf.setBedarfKindergarten(List.of(infrastrukturbedarf));
        personen = new PersonenProJahrModel();
        personen.setJahr("2027");
        personen.setAnzahlPersonenGesamt(BigDecimal.valueOf(201));
        sobonursaechlichBedarf.setAlleEinwohner(List.of(personen));
        bedarf.setLangfristigerSobonursaechlicherBedarf(sobonursaechlichBedarf);

        final var bedarfForEachAbfragevariante = new HashMap<UUID, BedarfeForAbfragevarianteModel>();
        bedarfForEachAbfragevariante.put(abfragevariante1.getId(), bedarf);

        bedarf = new BedarfeForAbfragevarianteModel();
        planungsursaechlichBedarf = new LangfristigerBedarfModel();
        wohneinheiten = new WohneinheitenProFoerderartProJahrModel();
        wohneinheiten.setJahr("1020");
        wohneinheiten.setFoerderart("forderart3");
        wohneinheiten.setWohneinheiten(BigDecimal.valueOf(600));
        planungsursaechlichBedarf.setWohneinheiten(List.of(wohneinheiten));
        infrastrukturbedarf = new InfrastrukturbedarfProJahrModel();
        infrastrukturbedarf.setJahr("1021");
        infrastrukturbedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(6000));
        infrastrukturbedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(800));
        infrastrukturbedarf.setAnzahlGruppen(BigDecimal.valueOf(100));
        planungsursaechlichBedarf.setBedarfKinderkrippe(List.of(infrastrukturbedarf));
        infrastrukturbedarf = new InfrastrukturbedarfProJahrModel();
        infrastrukturbedarf.setJahr("1022");
        infrastrukturbedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(1001));
        infrastrukturbedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(801));
        infrastrukturbedarf.setAnzahlGruppen(BigDecimal.valueOf(110));
        planungsursaechlichBedarf.setBedarfKindergarten(List.of(infrastrukturbedarf));
        personen = new PersonenProJahrModel();
        personen.setJahr("1023");
        personen.setAnzahlPersonenGesamt(BigDecimal.valueOf(1030));
        planungsursaechlichBedarf.setAlleEinwohner(List.of(personen));
        bedarf.setLangfristigerPlanungsursaechlicherBedarf(planungsursaechlichBedarf);

        sobonursaechlichBedarf = new LangfristigerBedarfModel();
        wohneinheiten = new WohneinheitenProFoerderartProJahrModel();
        wohneinheiten.setJahr("1024");
        wohneinheiten.setFoerderart("forderart4");
        wohneinheiten.setWohneinheiten(BigDecimal.valueOf(88));
        sobonursaechlichBedarf.setWohneinheiten(List.of(wohneinheiten));
        infrastrukturbedarf = new InfrastrukturbedarfProJahrModel();
        infrastrukturbedarf.setJahr("1025");
        infrastrukturbedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(222));
        infrastrukturbedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(999));
        infrastrukturbedarf.setAnzahlGruppen(BigDecimal.valueOf(87));
        sobonursaechlichBedarf.setBedarfKinderkrippe(List.of(infrastrukturbedarf));
        infrastrukturbedarf = new InfrastrukturbedarfProJahrModel();
        infrastrukturbedarf.setJahr("1026");
        infrastrukturbedarf.setAnzahlPersonenGesamt(BigDecimal.valueOf(999));
        infrastrukturbedarf.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(998));
        infrastrukturbedarf.setAnzahlGruppen(BigDecimal.valueOf(8));
        sobonursaechlichBedarf.setBedarfKindergarten(List.of(infrastrukturbedarf));
        personen = new PersonenProJahrModel();
        personen.setJahr("1027");
        personen.setAnzahlPersonenGesamt(BigDecimal.valueOf(235));
        sobonursaechlichBedarf.setAlleEinwohner(List.of(personen));
        bedarf.setLangfristigerSobonursaechlicherBedarf(sobonursaechlichBedarf);

        bedarfForEachAbfragevariante.put(abfragevariante2.getId(), bedarf);

        final var result = reportingdataTransferService.addBedarfeToAbfrage(abfrage, bedarfForEachAbfragevariante);

        var expected = new BauleitplanverfahrenDto();
        expected.setId(abfrage.getId());
        expected.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.BAULEITPLANVERFAHREN);
        var abfragevariante1expected = new AbfragevarianteBauleitplanverfahrenDto();
        abfragevariante1expected.setId(abfragevariante1.getId());
        abfragevariante1expected.setName("Variante 1");
        abfragevariante1expected.setAbfragevariantenNr(1);
        var abfragevariante2expected = new AbfragevarianteBauleitplanverfahrenDto();
        abfragevariante2expected.setId(abfragevariante2.getId());
        abfragevariante2expected.setName("Variante 2");
        abfragevariante2expected.setAbfragevariantenNr(2);
        expected.setAbfragevariantenBauleitplanverfahren(List.of(abfragevariante1expected));
        expected.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevariante2expected));

        var planungsursaechlichBedarfDto = new LangfristigerBedarfDto();
        var wohneinheitenDto = new WohneinheitenProFoerderartProJahrDto();
        wohneinheitenDto.setJahr("2020");
        wohneinheitenDto.setFoerderart("forderart1");
        wohneinheitenDto.setWohneinheiten(BigDecimal.valueOf(10));
        planungsursaechlichBedarfDto.setWohneinheiten(List.of(wohneinheitenDto));
        var infrastrukturbedarfDto = new InfrastrukturbedarfProJahrDto();
        infrastrukturbedarfDto.setJahr("2021");
        infrastrukturbedarfDto.setAnzahlPersonenGesamt(BigDecimal.valueOf(100));
        infrastrukturbedarfDto.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(80));
        infrastrukturbedarfDto.setAnzahlGruppen(BigDecimal.valueOf(10));
        planungsursaechlichBedarfDto.setBedarfKinderkrippe(List.of(infrastrukturbedarfDto));
        infrastrukturbedarfDto = new InfrastrukturbedarfProJahrDto();
        infrastrukturbedarfDto.setJahr("2022");
        infrastrukturbedarfDto.setAnzahlPersonenGesamt(BigDecimal.valueOf(101));
        infrastrukturbedarfDto.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(81));
        infrastrukturbedarfDto.setAnzahlGruppen(BigDecimal.valueOf(11));
        planungsursaechlichBedarfDto.setBedarfKindergarten(List.of(infrastrukturbedarfDto));
        var personenDto = new PersonenProJahrDto();
        personenDto.setJahr("2023");
        personenDto.setAnzahlPersonenGesamt(BigDecimal.valueOf(102));
        planungsursaechlichBedarfDto.setAlleEinwohner(List.of(personenDto));
        expected
            .getAbfragevariantenBauleitplanverfahren()
            .get(0)
            .setLangfristigerPlanungsursaechlicherBedarf(planungsursaechlichBedarfDto);

        var sobonursaechlichBedarfDto = new LangfristigerBedarfDto();
        wohneinheitenDto = new WohneinheitenProFoerderartProJahrDto();
        wohneinheitenDto.setJahr("2024");
        wohneinheitenDto.setFoerderart("forderart2");
        wohneinheitenDto.setWohneinheiten(BigDecimal.valueOf(20));
        sobonursaechlichBedarfDto.setWohneinheiten(List.of(wohneinheitenDto));
        infrastrukturbedarfDto = new InfrastrukturbedarfProJahrDto();
        infrastrukturbedarfDto.setJahr("2025");
        infrastrukturbedarfDto.setAnzahlPersonenGesamt(BigDecimal.valueOf(200));
        infrastrukturbedarfDto.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(70));
        infrastrukturbedarfDto.setAnzahlGruppen(BigDecimal.valueOf(5));
        sobonursaechlichBedarfDto.setBedarfKinderkrippe(List.of(infrastrukturbedarfDto));
        infrastrukturbedarfDto = new InfrastrukturbedarfProJahrDto();
        infrastrukturbedarfDto.setJahr("2026");
        infrastrukturbedarfDto.setAnzahlPersonenGesamt(BigDecimal.valueOf(201));
        infrastrukturbedarfDto.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(71));
        infrastrukturbedarfDto.setAnzahlGruppen(BigDecimal.valueOf(6));
        sobonursaechlichBedarfDto.setBedarfKindergarten(List.of(infrastrukturbedarfDto));
        personenDto = new PersonenProJahrDto();
        personenDto.setJahr("2027");
        personenDto.setAnzahlPersonenGesamt(BigDecimal.valueOf(201));
        sobonursaechlichBedarfDto.setAlleEinwohner(List.of(personenDto));
        expected
            .getAbfragevariantenBauleitplanverfahren()
            .get(0)
            .setLangfristigerSobonursaechlicherBedarf(sobonursaechlichBedarfDto);

        planungsursaechlichBedarfDto = new LangfristigerBedarfDto();
        wohneinheitenDto = new WohneinheitenProFoerderartProJahrDto();
        wohneinheitenDto.setJahr("1020");
        wohneinheitenDto.setFoerderart("forderart3");
        wohneinheitenDto.setWohneinheiten(BigDecimal.valueOf(600));
        planungsursaechlichBedarfDto.setWohneinheiten(List.of(wohneinheitenDto));
        infrastrukturbedarfDto = new InfrastrukturbedarfProJahrDto();
        infrastrukturbedarfDto.setJahr("1021");
        infrastrukturbedarfDto.setAnzahlPersonenGesamt(BigDecimal.valueOf(6000));
        infrastrukturbedarfDto.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(800));
        infrastrukturbedarfDto.setAnzahlGruppen(BigDecimal.valueOf(100));
        planungsursaechlichBedarfDto.setBedarfKinderkrippe(List.of(infrastrukturbedarfDto));
        infrastrukturbedarfDto = new InfrastrukturbedarfProJahrDto();
        infrastrukturbedarfDto.setJahr("1022");
        infrastrukturbedarfDto.setAnzahlPersonenGesamt(BigDecimal.valueOf(1001));
        infrastrukturbedarfDto.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(801));
        infrastrukturbedarfDto.setAnzahlGruppen(BigDecimal.valueOf(110));
        planungsursaechlichBedarfDto.setBedarfKindergarten(List.of(infrastrukturbedarfDto));
        personenDto = new PersonenProJahrDto();
        personenDto.setJahr("1023");
        personenDto.setAnzahlPersonenGesamt(BigDecimal.valueOf(1030));
        planungsursaechlichBedarfDto.setAlleEinwohner(List.of(personenDto));
        expected
            .getAbfragevariantenSachbearbeitungBauleitplanverfahren()
            .get(0)
            .setLangfristigerPlanungsursaechlicherBedarf(planungsursaechlichBedarfDto);

        sobonursaechlichBedarfDto = new LangfristigerBedarfDto();
        wohneinheitenDto = new WohneinheitenProFoerderartProJahrDto();
        wohneinheitenDto.setJahr("1024");
        wohneinheitenDto.setFoerderart("forderart4");
        wohneinheitenDto.setWohneinheiten(BigDecimal.valueOf(88));
        sobonursaechlichBedarfDto.setWohneinheiten(List.of(wohneinheitenDto));
        infrastrukturbedarfDto = new InfrastrukturbedarfProJahrDto();
        infrastrukturbedarfDto.setJahr("1025");
        infrastrukturbedarfDto.setAnzahlPersonenGesamt(BigDecimal.valueOf(222));
        infrastrukturbedarfDto.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(999));
        infrastrukturbedarfDto.setAnzahlGruppen(BigDecimal.valueOf(87));
        sobonursaechlichBedarfDto.setBedarfKinderkrippe(List.of(infrastrukturbedarfDto));
        infrastrukturbedarfDto = new InfrastrukturbedarfProJahrDto();
        infrastrukturbedarfDto.setJahr("1026");
        infrastrukturbedarfDto.setAnzahlPersonenGesamt(BigDecimal.valueOf(999));
        infrastrukturbedarfDto.setAnzahlPersonenZuVersorgen(BigDecimal.valueOf(998));
        infrastrukturbedarfDto.setAnzahlGruppen(BigDecimal.valueOf(8));
        sobonursaechlichBedarfDto.setBedarfKindergarten(List.of(infrastrukturbedarfDto));
        personenDto = new PersonenProJahrDto();
        personenDto.setJahr("1027");
        personenDto.setAnzahlPersonenGesamt(BigDecimal.valueOf(235));
        sobonursaechlichBedarfDto.setAlleEinwohner(List.of(personenDto));
        expected
            .getAbfragevariantenSachbearbeitungBauleitplanverfahren()
            .get(0)
            .setLangfristigerSobonursaechlicherBedarf(sobonursaechlichBedarfDto);

        assertThat(result, is(expected));
    }

    @Test
    void transferAbfrage() throws ReportingException {
        final var abfrage = new AbfrageDto();
        reportingdataTransferService.transferAbfrage(abfrage);
        Mockito.verify(reportingdataTransferRepository, Mockito.times(1)).save(abfrage);
    }

    @Test
    void transferAbfrageException() throws ReportingException {
        final var abfrage = new AbfrageDto();
        Mockito
            .doThrow(new WebClientResponseException(500, "An error", null, null, null))
            .when(reportingdataTransferRepository)
            .save(abfrage);
        Assertions.assertThrows(ReportingException.class, () -> reportingdataTransferService.transferAbfrage(abfrage));
        Mockito.verify(reportingdataTransferRepository, Mockito.times(1)).save(abfrage);
    }

    @Test
    void deleteTransferedAbfrage() throws ReportingException {
        final var id = UUID.randomUUID();
        reportingdataTransferService.deleteTransferedAbfrage(id);
        Mockito.verify(reportingdataTransferRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteTransferedAbfrageException() {
        final var id = UUID.randomUUID();
        Mockito
            .doThrow(new WebClientResponseException(500, "An error", null, null, null))
            .when(reportingdataTransferRepository)
            .deleteById(id);
        Assertions.assertThrows(
            ReportingException.class,
            () -> reportingdataTransferService.deleteTransferedAbfrage(id)
        );
        Mockito.verify(reportingdataTransferRepository, Mockito.times(1)).deleteById(id);
    }
}
