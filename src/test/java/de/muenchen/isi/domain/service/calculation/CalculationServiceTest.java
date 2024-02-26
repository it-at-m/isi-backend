package de.muenchen.isi.domain.service.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.model.AbfragevarianteBaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteWeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.calculation.BedarfeForAbfragevarianteModel;
import de.muenchen.isi.domain.model.calculation.InfrastrukturbedarfProJahrModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerBedarfModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerSobonBedarfModel;
import de.muenchen.isi.domain.model.calculation.PersonenProJahrModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CalculationServiceTest {

    @Mock
    private PlanungsursaechlicheWohneinheitenService planungsursaechlicheWohneinheitenService;

    @Mock
    private SobonursaechlicheWohneinheitenService sobonursaechlicheWohneinheitenService;

    @Mock
    private InfrastrukturbedarfService infrastrukturbedarfService;

    private CalculationService calculationService;

    @BeforeEach
    public void beforeEach() {
        this.calculationService =
            Mockito.spy(
                new CalculationService(
                    planungsursaechlicheWohneinheitenService,
                    sobonursaechlicheWohneinheitenService,
                    infrastrukturbedarfService
                )
            );
        Mockito.reset(planungsursaechlicheWohneinheitenService, infrastrukturbedarfService, calculationService);
    }

    @Test
    void calculateBedarfeForEachAbfragevarianteOfAbfrageUnspecified() {
        final var abfrage = new WeiteresVerfahrenModel();
        abfrage.setArtAbfrage(ArtAbfrage.UNSPECIFIED);
        Assertions.assertThrows(
            CalculationException.class,
            () -> this.calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage)
        );
    }

    @Test
    void calculateBedarfeForEachAbfragevarianteOfAbfrageWeiteresVerfahren()
        throws CalculationException, UserRoleNotAllowedException, EntityNotFoundException {
        final var abfrage = new WeiteresVerfahrenModel();
        abfrage.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        abfrage.setAbfragevariantenWeiteresVerfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungWeiteresVerfahren(null);
        abfrage.setSobonRelevant(UncertainBoolean.UNSPECIFIED);
        var result = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        assertThat(result, is(new HashMap<UUID, BedarfeForAbfragevarianteModel>()));
        Mockito
            .verify(calculationService, Mockito.times(0))
            .calculateBedarfeForAbfragevariante(Mockito.any(), Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenWeiteresVerfahren(List.of());
        abfrage.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());
        result = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        assertThat(result, is(new HashMap<UUID, BedarfeForAbfragevarianteModel>()));
        Mockito
            .verify(calculationService, Mockito.times(0))
            .calculateBedarfeForAbfragevariante(Mockito.any(), Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenWeiteresVerfahren(null);
        var abfragevariante = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevariante.setId(UUID.randomUUID());
        abfragevariante.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfrage.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevariante));

        var expected = Map.of(abfragevariante.getId(), new BedarfeForAbfragevarianteModel());
        result = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        assertThat(result, is(expected));
        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateBedarfeForAbfragevariante(abfragevariante, UncertainBoolean.UNSPECIFIED);

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenSachbearbeitungWeiteresVerfahren(null);
        abfragevariante = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevariante.setId(UUID.randomUUID());
        abfragevariante.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfrage.setAbfragevariantenWeiteresVerfahren(List.of(abfragevariante));

        expected = Map.of(abfragevariante.getId(), new BedarfeForAbfragevarianteModel());
        result = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        assertThat(result, is(expected));
        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateBedarfeForAbfragevariante(abfragevariante, UncertainBoolean.UNSPECIFIED);

        Mockito.reset(calculationService);
        var abfragevariante1 = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevariante1.setId(UUID.randomUUID());
        abfragevariante1.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfrage.setAbfragevariantenWeiteresVerfahren(List.of(abfragevariante1));
        expected = new HashMap<>();
        expected.put(abfragevariante1.getId(), new BedarfeForAbfragevarianteModel());
        var abfragevariante2 = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevariante2.setId(UUID.randomUUID());
        abfragevariante2.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfrage.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevariante2));

        expected.put(abfragevariante2.getId(), new BedarfeForAbfragevarianteModel());
        result = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        assertThat(result, is(expected));
        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateBedarfeForAbfragevariante(abfragevariante1, UncertainBoolean.UNSPECIFIED);
        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateBedarfeForAbfragevariante(abfragevariante2, UncertainBoolean.UNSPECIFIED);
    }

    @Test
    void calculateBedarfeForEachAbfragevarianteOfAbfrageBaugenehmigungsverfahren()
        throws CalculationException, UserRoleNotAllowedException, EntityNotFoundException {
        final var abfrage = new BaugenehmigungsverfahrenModel();
        abfrage.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfrage.setAbfragevariantenBaugenehmigungsverfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(null);
        var result = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        assertThat(result, is(new HashMap<UUID, BedarfeForAbfragevarianteModel>()));
        Mockito
            .verify(calculationService, Mockito.times(0))
            .calculateBedarfeForAbfragevariante(Mockito.any(), Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBaugenehmigungsverfahren(List.of());
        abfrage.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());
        result = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        assertThat(result, is(new HashMap<UUID, BedarfeForAbfragevarianteModel>()));
        Mockito
            .verify(calculationService, Mockito.times(0))
            .calculateBedarfeForAbfragevariante(Mockito.any(), Mockito.any());

        Mockito.reset(calculationService);
        var abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevariante.setId(UUID.randomUUID());
        abfragevariante.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfrage.setAbfragevariantenBaugenehmigungsverfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of(abfragevariante));
        var expected = Map.of(abfragevariante.getId(), new BedarfeForAbfragevarianteModel());
        result = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        assertThat(result, is(expected));
        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateBedarfeForAbfragevariante(abfragevariante, UncertainBoolean.UNSPECIFIED);

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(null);
        abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevariante.setId(UUID.randomUUID());
        abfragevariante.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfrage.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevariante));
        expected = Map.of(abfragevariante.getId(), new BedarfeForAbfragevarianteModel());
        result = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        assertThat(result, is(expected));
        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateBedarfeForAbfragevariante(abfragevariante, UncertainBoolean.UNSPECIFIED);

        Mockito.reset(calculationService);
        var abfragevariante1 = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevariante1.setId(UUID.randomUUID());
        abfragevariante1.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfrage.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevariante1));
        expected = new HashMap<>();
        expected.put(abfragevariante1.getId(), new BedarfeForAbfragevarianteModel());
        var abfragevariante2 = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevariante2.setId(UUID.randomUUID());
        abfragevariante2.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfrage.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of(abfragevariante2));
        expected.put(abfragevariante2.getId(), new BedarfeForAbfragevarianteModel());
        result = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        assertThat(result, is(expected));
        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateBedarfeForAbfragevariante(abfragevariante1, UncertainBoolean.UNSPECIFIED);
        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateBedarfeForAbfragevariante(abfragevariante2, UncertainBoolean.UNSPECIFIED);
    }

    @Test
    void calculateBedarfeForEachAbfragevarianteOfAbfrageBauleitplanverfahren()
        throws CalculationException, UserRoleNotAllowedException, EntityNotFoundException {
        final var abfrage = new BauleitplanverfahrenModel();
        abfrage.setId(UUID.randomUUID());
        abfrage.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfrage.setAbfragevariantenBauleitplanverfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(null);
        abfrage.setSobonRelevant(UncertainBoolean.UNSPECIFIED);
        var result = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        assertThat(result, is(new HashMap<UUID, BedarfeForAbfragevarianteModel>()));
        Mockito
            .verify(calculationService, Mockito.times(0))
            .calculateBedarfeForAbfragevariante(Mockito.any(), Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBauleitplanverfahren(List.of());
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());
        result = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        assertThat(result, is(new HashMap<UUID, BedarfeForAbfragevarianteModel>()));
        Mockito
            .verify(calculationService, Mockito.times(0))
            .calculateBedarfeForAbfragevariante(Mockito.any(), Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBauleitplanverfahren(null);
        var abfragevariante = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante.setId(UUID.randomUUID());
        abfragevariante.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevariante));

        var expected = Map.of(abfragevariante.getId(), new BedarfeForAbfragevarianteModel());
        result = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        assertThat(result, is(expected));
        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateBedarfeForAbfragevariante(abfragevariante, UncertainBoolean.UNSPECIFIED);

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(null);
        abfragevariante = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante.setId(UUID.randomUUID());
        abfragevariante.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfrage.setAbfragevariantenBauleitplanverfahren(List.of(abfragevariante));

        expected = Map.of(abfragevariante.getId(), new BedarfeForAbfragevarianteModel());
        result = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        assertThat(result, is(expected));
        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateBedarfeForAbfragevariante(abfragevariante, UncertainBoolean.UNSPECIFIED);

        Mockito.reset(calculationService);
        var abfragevariante1 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante1.setId(UUID.randomUUID());
        abfragevariante1.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfrage.setAbfragevariantenBauleitplanverfahren(List.of(abfragevariante1));
        expected = new HashMap<>();
        expected.put(abfragevariante1.getId(), new BedarfeForAbfragevarianteModel());
        var abfragevariante2 = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante2.setId(UUID.randomUUID());
        abfragevariante2.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevariante2));

        expected.put(abfragevariante2.getId(), new BedarfeForAbfragevarianteModel());
        result = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        assertThat(result, is(expected));
        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateBedarfeForAbfragevariante(abfragevariante1, UncertainBoolean.UNSPECIFIED);
        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateBedarfeForAbfragevariante(abfragevariante2, UncertainBoolean.UNSPECIFIED);
    }

    @Test
    void calculateBedarfeForAbfragevarianteUnspecified() {
        final var abfragevarianteBauleitplanverfahren = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteBauleitplanverfahren.setArtAbfragevariante(ArtAbfrage.UNSPECIFIED);
        Assertions.assertThrows(
            CalculationException.class,
            () ->
                this.calculationService.calculateBedarfeForAbfragevariante(
                        abfragevarianteBauleitplanverfahren,
                        UncertainBoolean.UNSPECIFIED
                    )
        );
    }

    @Test
    void calculateBedarfeForAbfragevarianteWeiteresVerfahren()
        throws CalculationException, UserRoleNotAllowedException, EntityNotFoundException {
        final var bauabschnitte = List.of(new BauabschnittModel());
        final var sobonOrientierungswertJahr = SobonOrientierungswertJahr.JAHR_2017;
        final var stammdatenGueltigAb = LocalDate.of(2020, 5, 30);
        final var sobonGf = BigDecimal.ZERO;

        final var abfrage = new WeiteresVerfahrenModel();
        abfrage.setSobonRelevant(UncertainBoolean.TRUE);

        final var abfragevarianteWeiteresVerfahrenModel = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteWeiteresVerfahrenModel.setId(UUID.randomUUID());
        abfragevarianteWeiteresVerfahrenModel.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfragevarianteWeiteresVerfahrenModel.setBauabschnitte(bauabschnitte);
        abfragevarianteWeiteresVerfahrenModel.setSobonOrientierungswertJahr(sobonOrientierungswertJahr);
        abfragevarianteWeiteresVerfahrenModel.setStammdatenGueltigAb(stammdatenGueltigAb);
        abfragevarianteWeiteresVerfahrenModel.setGfWohnenSobonUrsaechlich(sobonGf);
        abfragevarianteWeiteresVerfahrenModel.setIsASobonBerechnung(true);
        abfragevarianteWeiteresVerfahrenModel.setSobonFoerdermix(new FoerdermixModel());

        final var langfristigerPlanungsursaechlicherBedarf = new LangfristigerBedarfModel();
        langfristigerPlanungsursaechlicherBedarf.setWohneinheiten(
            List.of(new WohneinheitenProFoerderartProJahrModel())
        );

        final var langfristigerSobonursaechlicherBedarf = new LangfristigerSobonBedarfModel();
        langfristigerSobonursaechlicherBedarf.setWohneinheiten(List.of(new WohneinheitenProFoerderartProJahrModel()));

        Mockito
            .doReturn(langfristigerPlanungsursaechlicherBedarf)
            .when(calculationService)
            .calculateLangfristigerPlanungsursaechlicherBedarf(
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );

        Mockito
            .doReturn(langfristigerSobonursaechlicherBedarf)
            .when(calculationService)
            .calculateLangfristigerSobonursaechlicherBedarf(
                sobonGf,
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb,
                abfragevarianteWeiteresVerfahrenModel.getSobonFoerdermix()
            );

        final var bedarfeForAbfragevariante = calculationService.calculateBedarfeForAbfragevariante(
            abfragevarianteWeiteresVerfahrenModel,
            UncertainBoolean.TRUE
        );

        final var expected = new BedarfeForAbfragevarianteModel();
        expected.setLangfristigerPlanungsursaechlicherBedarf(langfristigerPlanungsursaechlicherBedarf);
        expected.setLangfristigerSobonursaechlicherBedarf(langfristigerSobonursaechlicherBedarf);

        assertThat(bedarfeForAbfragevariante, is(expected));

        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateLangfristigerPlanungsursaechlicherBedarf(
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );

        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateLangfristigerSobonursaechlicherBedarf(
                sobonGf,
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb,
                abfragevarianteWeiteresVerfahrenModel.getSobonFoerdermix()
            );
    }

    @Test
    void calculateBedarfeForAbfragevarianteBaugenehmigungsverfahren()
        throws CalculationException, UserRoleNotAllowedException, EntityNotFoundException {
        final var bauabschnitte = List.of(new BauabschnittModel());
        final var sobonOrientierungswertJahr = SobonOrientierungswertJahr.JAHR_2017;
        final var stammdatenGueltigAb = LocalDate.of(2020, 5, 30);
        final var sobonGf = BigDecimal.ZERO;

        final var abfragevarianteBaugenehmigungsverfahrenModel = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevarianteBaugenehmigungsverfahrenModel.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragevarianteBaugenehmigungsverfahrenModel.setBauabschnitte(bauabschnitte);
        abfragevarianteBaugenehmigungsverfahrenModel.setSobonOrientierungswertJahr(sobonOrientierungswertJahr);
        abfragevarianteBaugenehmigungsverfahrenModel.setStammdatenGueltigAb(stammdatenGueltigAb);

        final var langfristigerPlanungsursaechlicherBedarf = new LangfristigerBedarfModel();
        langfristigerPlanungsursaechlicherBedarf.setWohneinheiten(
            List.of(new WohneinheitenProFoerderartProJahrModel())
        );

        final var langfristigerSobonursaechlicherBedarf = new LangfristigerSobonBedarfModel();
        langfristigerSobonursaechlicherBedarf.setWohneinheiten(List.of(new WohneinheitenProFoerderartProJahrModel()));

        Mockito
            .doReturn(langfristigerPlanungsursaechlicherBedarf)
            .when(calculationService)
            .calculateLangfristigerPlanungsursaechlicherBedarf(
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );

        Mockito
            .doReturn(langfristigerSobonursaechlicherBedarf)
            .when(calculationService)
            .calculateLangfristigerSobonursaechlicherBedarf(
                sobonGf,
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );

        final var bedarfeForAbfragevariante = calculationService.calculateBedarfeForAbfragevariante(
            abfragevarianteBaugenehmigungsverfahrenModel,
            UncertainBoolean.UNSPECIFIED
        );

        final var expected = new BedarfeForAbfragevarianteModel();
        expected.setLangfristigerPlanungsursaechlicherBedarf(langfristigerPlanungsursaechlicherBedarf);
        expected.setLangfristigerSobonursaechlicherBedarf(langfristigerSobonursaechlicherBedarf);

        assertThat(bedarfeForAbfragevariante, is(expected));

        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateLangfristigerPlanungsursaechlicherBedarf(
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );

        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateLangfristigerSobonursaechlicherBedarf(
                sobonGf,
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );
    }

    @Test
    void calculateBedarfeForAbfragevarianteBauleitplanverfahren()
        throws CalculationException, UserRoleNotAllowedException, EntityNotFoundException {
        final var bauabschnitte = List.of(new BauabschnittModel());
        final var sobonOrientierungswertJahr = SobonOrientierungswertJahr.JAHR_2017;
        final var stammdatenGueltigAb = LocalDate.of(2020, 5, 30);
        final var sobonGf = BigDecimal.ZERO;

        final var abfrage = new BauleitplanverfahrenModel();
        abfrage.setSobonRelevant(UncertainBoolean.TRUE);

        final var abfragevarianteBauleitplanverfahren = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteBauleitplanverfahren.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevarianteBauleitplanverfahren.setBauabschnitte(bauabschnitte);
        abfragevarianteBauleitplanverfahren.setSobonOrientierungswertJahr(sobonOrientierungswertJahr);
        abfragevarianteBauleitplanverfahren.setStammdatenGueltigAb(stammdatenGueltigAb);
        abfragevarianteBauleitplanverfahren.setGfWohnenSobonUrsaechlich(sobonGf);
        abfragevarianteBauleitplanverfahren.setIsASobonBerechnung(true);
        abfragevarianteBauleitplanverfahren.setSobonFoerdermix(new FoerdermixModel());

        final var langfristigerPlanungsursaechlicherBedarf = new LangfristigerBedarfModel();
        langfristigerPlanungsursaechlicherBedarf.setWohneinheiten(
            List.of(new WohneinheitenProFoerderartProJahrModel())
        );

        final var langfristigerSobonursaechlicherBedarf = new LangfristigerSobonBedarfModel();
        langfristigerSobonursaechlicherBedarf.setWohneinheiten(List.of(new WohneinheitenProFoerderartProJahrModel()));

        Mockito
            .doReturn(langfristigerPlanungsursaechlicherBedarf)
            .when(calculationService)
            .calculateLangfristigerPlanungsursaechlicherBedarf(
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );

        Mockito
            .doReturn(langfristigerSobonursaechlicherBedarf)
            .when(calculationService)
            .calculateLangfristigerSobonursaechlicherBedarf(
                sobonGf,
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb,
                abfragevarianteBauleitplanverfahren.getSobonFoerdermix()
            );

        final var bedarfeForAbfragevariante = calculationService.calculateBedarfeForAbfragevariante(
            abfragevarianteBauleitplanverfahren,
            UncertainBoolean.TRUE
        );

        final var expected = new BedarfeForAbfragevarianteModel();
        expected.setLangfristigerPlanungsursaechlicherBedarf(langfristigerPlanungsursaechlicherBedarf);
        expected.setLangfristigerSobonursaechlicherBedarf(langfristigerSobonursaechlicherBedarf);

        assertThat(bedarfeForAbfragevariante, is(expected));

        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateLangfristigerPlanungsursaechlicherBedarf(
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );

        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateLangfristigerSobonursaechlicherBedarf(
                sobonGf,
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb,
                abfragevarianteBauleitplanverfahren.getSobonFoerdermix()
            );
    }

    @Test
    void calculateLangfristigerPlanungsursaechlicherBedarfReturnNull() throws CalculationException {
        var result = calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(null, null, null);
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                null,
                SobonOrientierungswertJahr.JAHR_2017,
                LocalDate.now()
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                new ArrayList<>(),
                SobonOrientierungswertJahr.JAHR_2017,
                LocalDate.now()
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                List.of(new BauabschnittModel()),
                null,
                null
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                List.of(new BauabschnittModel()),
                SobonOrientierungswertJahr.JAHR_2017,
                null
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                List.of(new BauabschnittModel()),
                null,
                LocalDate.now()
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                List.of(new BauabschnittModel()),
                SobonOrientierungswertJahr.STANDORTABFRAGE,
                LocalDate.now()
            );
        assertThat(result, is(nullValue()));
    }

    @Test
    void calculateLangfristigerPlanungsursaechlicherBedarf() throws CalculationException {
        final var bauabschnitte = List.of(new BauabschnittModel());
        final var sobonOrientierungswertJahr = SobonOrientierungswertJahr.JAHR_2017;
        final var stammdatenGueltigAb = LocalDate.of(2020, 5, 30);

        final var wohneinheiten = List.of(new WohneinheitenProFoerderartProJahrModel());

        Mockito
            .when(
                this.planungsursaechlicheWohneinheitenService.calculatePlanungsursaechlicheWohneinheiten(
                        bauabschnitte,
                        sobonOrientierungswertJahr,
                        stammdatenGueltigAb
                    )
            )
            .thenReturn(wohneinheiten);

        final var bedarfeProJahrKinderkrippe = List.of(new InfrastrukturbedarfProJahrModel());

        Mockito
            .when(
                this.infrastrukturbedarfService.calculateBedarfForKinderkrippe(
                        wohneinheiten,
                        sobonOrientierungswertJahr,
                        InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH,
                        stammdatenGueltigAb
                    )
            )
            .thenReturn(bedarfeProJahrKinderkrippe);

        final var bedarfeProJahrKindergarten = List.of(
            new InfrastrukturbedarfProJahrModel(),
            new InfrastrukturbedarfProJahrModel()
        );

        Mockito
            .when(
                this.infrastrukturbedarfService.calculateBedarfForKindergarten(
                        wohneinheiten,
                        sobonOrientierungswertJahr,
                        InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH,
                        stammdatenGueltigAb
                    )
            )
            .thenReturn(bedarfeProJahrKindergarten);

        final var alleEinwohnerProJahr = List.of(new PersonenProJahrModel(), new PersonenProJahrModel());

        Mockito
            .when(this.infrastrukturbedarfService.calculateAlleEinwohner(wohneinheiten, sobonOrientierungswertJahr))
            .thenReturn(alleEinwohnerProJahr);

        final var result = calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
            bauabschnitte,
            sobonOrientierungswertJahr,
            stammdatenGueltigAb
        );

        final var expected = new LangfristigerBedarfModel();

        expected.setWohneinheiten(wohneinheiten);
        expected.setBedarfKinderkrippe(bedarfeProJahrKinderkrippe);
        expected.setBedarfKindergarten(bedarfeProJahrKindergarten);
        expected.setAlleEinwohner(alleEinwohnerProJahr);

        assertThat(result, is(expected));
    }

    @Test
    void calculateLangfristigerSobonursaechlicherBedarfReturnNull() throws CalculationException {
        var result = calculationService.calculateLangfristigerSobonursaechlicherBedarf(null, null, null, null, null);
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerSobonursaechlicherBedarf(
                null,
                List.of(new BauabschnittModel()),
                SobonOrientierungswertJahr.JAHR_2017,
                LocalDate.now(),
                new FoerdermixModel()
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerSobonursaechlicherBedarf(
                BigDecimal.ZERO,
                null,
                SobonOrientierungswertJahr.JAHR_2017,
                LocalDate.now(),
                new FoerdermixModel()
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerSobonursaechlicherBedarf(
                BigDecimal.ZERO,
                List.of(new BauabschnittModel()),
                null,
                LocalDate.now(),
                new FoerdermixModel()
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerSobonursaechlicherBedarf(
                BigDecimal.ZERO,
                List.of(new BauabschnittModel()),
                SobonOrientierungswertJahr.JAHR_2017,
                null,
                new FoerdermixModel()
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerSobonursaechlicherBedarf(
                BigDecimal.ZERO,
                new ArrayList<>(),
                SobonOrientierungswertJahr.JAHR_2017,
                LocalDate.now(),
                new FoerdermixModel()
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerSobonursaechlicherBedarf(
                BigDecimal.ZERO,
                List.of(new BauabschnittModel()),
                SobonOrientierungswertJahr.STANDORTABFRAGE,
                LocalDate.now(),
                new FoerdermixModel()
            );
        assertThat(result, is(nullValue()));
    }

    @Test
    void calculateLangfristigerSobonursaechlicherBedarf() throws CalculationException {
        final var sobonGf = BigDecimal.ZERO;
        final var bauabschnitte = List.of(new BauabschnittModel());
        final var sobonOrientierungswertJahr = SobonOrientierungswertJahr.JAHR_2017;
        final var stammdatenGueltigAb = LocalDate.of(2020, 5, 30);
        final var foerdermix = new FoerdermixModel();
        final var wohneinheiten = List.of(new WohneinheitenProFoerderartProJahrModel());

        Mockito
            .when(
                this.sobonursaechlicheWohneinheitenService.calculateSobonursaechlicheWohneinheiten(
                        sobonGf,
                        bauabschnitte,
                        sobonOrientierungswertJahr,
                        stammdatenGueltigAb,
                        foerdermix
                    )
            )
            .thenReturn(wohneinheiten);

        final var bedarfeProJahrKinderkrippe = List.of(new InfrastrukturbedarfProJahrModel());

        Mockito
            .when(
                this.infrastrukturbedarfService.calculateBedarfForKinderkrippe(
                        wohneinheiten,
                        sobonOrientierungswertJahr,
                        InfrastrukturbedarfService.ArtInfrastrukturbedarf.SOBON_URSAECHLICH,
                        stammdatenGueltigAb
                    )
            )
            .thenReturn(bedarfeProJahrKinderkrippe);

        final var bedarfeProJahrKindergarten = List.of(
            new InfrastrukturbedarfProJahrModel(),
            new InfrastrukturbedarfProJahrModel()
        );

        Mockito
            .when(
                this.infrastrukturbedarfService.calculateBedarfForKindergarten(
                        wohneinheiten,
                        sobonOrientierungswertJahr,
                        InfrastrukturbedarfService.ArtInfrastrukturbedarf.SOBON_URSAECHLICH,
                        stammdatenGueltigAb
                    )
            )
            .thenReturn(bedarfeProJahrKindergarten);

        final var bedarfeProJahrGsNachmittagBetreuung = List.of(
            new InfrastrukturbedarfProJahrModel(),
            new InfrastrukturbedarfProJahrModel()
        );

        Mockito
            .when(
                this.infrastrukturbedarfService.calculateBedarfForGsNachmittagBetreuung(
                        wohneinheiten,
                        sobonOrientierungswertJahr,
                        stammdatenGueltigAb
                    )
            )
            .thenReturn(bedarfeProJahrGsNachmittagBetreuung);

        final var bedarfeProJahrGrundschule = List.of(
            new InfrastrukturbedarfProJahrModel(),
            new InfrastrukturbedarfProJahrModel()
        );

        Mockito
            .when(
                this.infrastrukturbedarfService.calculateBedarfForGrundschule(
                        wohneinheiten,
                        sobonOrientierungswertJahr,
                        stammdatenGueltigAb
                    )
            )
            .thenReturn(bedarfeProJahrGrundschule);

        final var alleEinwohnerProJahr = List.of(new PersonenProJahrModel(), new PersonenProJahrModel());

        Mockito
            .when(this.infrastrukturbedarfService.calculateAlleEinwohner(wohneinheiten, sobonOrientierungswertJahr))
            .thenReturn(alleEinwohnerProJahr);

        final var result = calculationService.calculateLangfristigerSobonursaechlicherBedarf(
            sobonGf,
            bauabschnitte,
            sobonOrientierungswertJahr,
            stammdatenGueltigAb,
            foerdermix
        );

        final var expected = new LangfristigerSobonBedarfModel();

        expected.setWohneinheiten(wohneinheiten);
        expected.setBedarfKinderkrippe(bedarfeProJahrKinderkrippe);
        expected.setBedarfKindergarten(bedarfeProJahrKindergarten);
        expected.setBedarfGsNachmittagBetreuung(bedarfeProJahrGsNachmittagBetreuung);
        expected.setBedarfGrundschule(bedarfeProJahrGrundschule);
        expected.setAlleEinwohner(alleEinwohnerProJahr);

        assertThat(result, is(expected));
    }

    @Test
    void hasRightCoonditionsForLangfristigeSobonBerechnung()
        throws UserRoleNotAllowedException, EntityNotFoundException {
        var isAbfrageSobonRelevant = UncertainBoolean.TRUE;
        final var abfragevariante = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante.setIsASobonBerechnung(true);
        abfragevariante.setGfWohnenSobonUrsaechlich(BigDecimal.ZERO);
        abfragevariante.setSobonFoerdermix(new FoerdermixModel());

        assertThat(
            this.calculationService.hasRightCoonditionsForLangfristigeSobonBerechnung(
                    abfragevariante,
                    isAbfrageSobonRelevant
                ),
            is(true)
        );

        isAbfrageSobonRelevant = UncertainBoolean.FALSE;
        assertThat(
            this.calculationService.hasRightCoonditionsForLangfristigeSobonBerechnung(
                    abfragevariante,
                    isAbfrageSobonRelevant
                ),
            is(false)
        );

        isAbfrageSobonRelevant = UncertainBoolean.UNSPECIFIED;
        assertThat(
            this.calculationService.hasRightCoonditionsForLangfristigeSobonBerechnung(
                    abfragevariante,
                    isAbfrageSobonRelevant
                ),
            is(false)
        );

        isAbfrageSobonRelevant = UncertainBoolean.TRUE;
        abfragevariante.setSobonFoerdermix(null);
        assertThat(
            this.calculationService.hasRightCoonditionsForLangfristigeSobonBerechnung(
                    abfragevariante,
                    isAbfrageSobonRelevant
                ),
            is(false)
        );
        abfragevariante.setSobonFoerdermix(new FoerdermixModel());
        abfragevariante.setIsASobonBerechnung(false);
        assertThat(
            this.calculationService.hasRightCoonditionsForLangfristigeSobonBerechnung(
                    abfragevariante,
                    isAbfrageSobonRelevant
                ),
            is(false)
        );
        abfragevariante.setIsASobonBerechnung(true);
        abfragevariante.setGfWohnenSobonUrsaechlich(null);
        assertThat(
            this.calculationService.hasRightCoonditionsForLangfristigeSobonBerechnung(
                    abfragevariante,
                    isAbfrageSobonRelevant
                ),
            is(false)
        );

        isAbfrageSobonRelevant = UncertainBoolean.FALSE;
        abfragevariante.setIsASobonBerechnung(false);
        abfragevariante.setSobonOrientierungswertJahr(null);
        assertThat(
            this.calculationService.hasRightCoonditionsForLangfristigeSobonBerechnung(
                    abfragevariante,
                    isAbfrageSobonRelevant
                ),
            is(false)
        );
    }
}
