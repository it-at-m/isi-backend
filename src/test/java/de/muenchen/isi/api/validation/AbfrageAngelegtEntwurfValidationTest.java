package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.BauleitplanverfahrenAngelegtDto;
import de.muenchen.isi.domain.service.stammdaten.FileInformationStammService;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Prüft, dass eine Abfrage im Status ANGELEGT ohne Pflichtangaben als Entwurf gespeichert werden kann
 * (Gruppe {@link Default}) und die Pflichtangaben erst bei der Freigabe (Gruppe {@link Vollstaendig}) geprüft werden.
 */
class AbfrageAngelegtEntwurfValidationTest {

    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        final var configuration = Validation.byDefaultProvider().configure();
        final var defaultFactory = configuration.getDefaultConstraintValidatorFactory();
        final var hasAllowedNumberOfDocumentsValidator = new HasAllowedNumberOfDocumentsValidator(
            new FileInformationStammService(1024L, 30L, List.of())
        );
        final var constraintValidatorFactory = new ConstraintValidatorFactory() {
            @Override
            public <T extends ConstraintValidator<?, ?>> T getInstance(final Class<T> key) {
                return key == HasAllowedNumberOfDocumentsValidator.class
                    ? key.cast(hasAllowedNumberOfDocumentsValidator)
                    : defaultFactory.getInstance(key);
            }

            @Override
            public void releaseInstance(final ConstraintValidator<?, ?> instance) {
                defaultFactory.releaseInstance(instance);
            }
        };
        validator = configuration
            .constraintValidatorFactory(constraintValidatorFactory)
            .buildValidatorFactory()
            .getValidator();
    }

    @Test
    void entwurfOhnePflichtangabenIsValid() {
        assertThat(propertyPaths(validator.validate(createEntwurf())), is(empty()));
    }

    @Test
    void entwurfMitUnvollstaendigerAbfragevarianteIsValid() {
        final var entwurf = createEntwurf();
        entwurf.getAbfragevariantenBauleitplanverfahren().add(createAbfragevarianteEntwurf());
        assertThat(propertyPaths(validator.validate(entwurf)), is(empty()));
    }

    @Test
    void entwurfOhneNameIsInvalid() {
        final var entwurf = createEntwurf();
        entwurf.setName(" ");
        assertThat(propertyPaths(validator.validate(entwurf)), containsInAnyOrder("name"));
    }

    @Test
    void entwurfMitZuLangemNameIsInvalid() {
        final var entwurf = createEntwurf();
        entwurf.setName("a".repeat(71));
        assertThat(propertyPaths(validator.validate(entwurf)), containsInAnyOrder("name"));
    }

    @Test
    void entwurfMitMehrAlsFuenfAbfragevariantenIsInvalid() {
        final var entwurf = createEntwurf();
        for (int index = 0; index < 6; index++) {
            entwurf.getAbfragevariantenBauleitplanverfahren().add(createAbfragevarianteEntwurf());
        }
        assertThat(
            propertyPaths(validator.validate(entwurf)),
            containsInAnyOrder("abfragevariantenBauleitplanverfahren")
        );
    }

    @Test
    void entwurfOhneAbfragevariantenIsInvalidForFreigabe() {
        final var paths = propertyPaths(validator.validate(createEntwurf(), Default.class, Vollstaendig.class));
        assertThat(
            paths,
            containsInAnyOrder("", "sobonRelevant", "verfahrensstand", "abfragevariantenBauleitplanverfahren")
        );
    }

    @Test
    void entwurfMitUnvollstaendigerAbfragevarianteIsInvalidForFreigabe() {
        final var entwurf = createEntwurf();
        entwurf.getAbfragevariantenBauleitplanverfahren().add(createAbfragevarianteEntwurf());
        final var paths = propertyPaths(validator.validate(entwurf, Default.class, Vollstaendig.class));
        assertThat(paths, hasItem("abfragevariantenBauleitplanverfahren[0].name"));
        assertThat(paths, hasItem("abfragevariantenBauleitplanverfahren[0].planart"));
        assertThat(paths, hasItem("abfragevariantenBauleitplanverfahren[0].realisierungVon"));
        assertThat(paths, hasItem("abfragevariantenBauleitplanverfahren[0].bauabschnitte"));
    }

    /**
     * Ein Entwurf mit den Werten, die das Frontend für nicht ausgefüllte Felder übermittelt.
     */
    private static BauleitplanverfahrenAngelegtDto createEntwurf() {
        final var entwurf = new BauleitplanverfahrenAngelegtDto();
        entwurf.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        entwurf.setName("Entwurf");
        entwurf.setSobonRelevant(UncertainBoolean.UNSPECIFIED);
        entwurf.setVerfahrensstand(Verfahrensstand.UNSPECIFIED);
        entwurf.setFristBearbeitung(LocalDate.EPOCH);
        entwurf.setMitzeichnungBeschlussentwurf(UncertainBoolean.UNSPECIFIED);
        entwurf.setStart42Verfahren(null);
        entwurf.setStart42VerfahrenDatumUnbekannt(false);
        entwurf.setAbfragevariantenBauleitplanverfahren(new ArrayList<>());
        return entwurf;
    }

    private static AbfragevarianteBauleitplanverfahrenAngelegtDto createAbfragevarianteEntwurf() {
        final var abfragevariante = new AbfragevarianteBauleitplanverfahrenAngelegtDto();
        abfragevariante.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevariante.setAbfragevariantenNr(1);
        abfragevariante.setName("");
        abfragevariante.setPlanart(new ArrayList<>());
        abfragevariante.setRealisierungVon(null);
        abfragevariante.setGfWohnenSonderwohnformen(false);
        abfragevariante.setWeSonderwohnformen(false);
        abfragevariante.setBauabschnitte(new ArrayList<>());
        return abfragevariante;
    }

    private static List<String> propertyPaths(final Set<? extends ConstraintViolation<?>> violations) {
        return violations
            .stream()
            .map(violation -> violation.getPropertyPath().toString())
            .collect(Collectors.toList());
    }
}
