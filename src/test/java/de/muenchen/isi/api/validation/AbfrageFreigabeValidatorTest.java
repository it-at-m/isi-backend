package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import de.muenchen.isi.api.dto.abfrageAngelegt.AbfrageAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.BauleitplanverfahrenAngelegtDto;
import de.muenchen.isi.api.mapper.AbfrageApiMapper;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AbfrageFreigabeValidatorTest {

    @Mock
    private AbfrageApiMapper abfrageApiMapper;

    @Mock
    private Validator validator;

    private AbfrageFreigabeValidator abfrageFreigabeValidator;

    @BeforeEach
    void beforeEach() {
        this.abfrageFreigabeValidator = new AbfrageFreigabeValidator(abfrageApiMapper, validator);
    }

    @Test
    void validateVollstaendigeAbfrage() {
        final var abfrage = createAbfrage(StatusAbfrage.ANGELEGT);
        final var dto = new BauleitplanverfahrenAngelegtDto();
        when(abfrageApiMapper.model2AngelegtDto(abfrage)).thenReturn(dto);
        when(validator.validate(dto, Default.class, Vollstaendig.class)).thenReturn(Set.of());

        assertDoesNotThrow(() -> abfrageFreigabeValidator.validate(abfrage));

        verify(validator).validate(dto, Default.class, Vollstaendig.class);
    }

    @Test
    void validateUnvollstaendigeAbfrage() {
        final var abfrage = createAbfrage(StatusAbfrage.ANGELEGT);
        final var dto = new BauleitplanverfahrenAngelegtDto();
        when(abfrageApiMapper.model2AngelegtDto(abfrage)).thenReturn(dto);
        final Set<ConstraintViolation<AbfrageAngelegtDto>> violations = Set.of(
            createViolation("verfahrensstand", "Der Wert darf nicht 'UNSPECIFIED' sein.")
        );
        when(validator.validate(any(AbfrageAngelegtDto.class), any(Class[].class))).thenReturn(violations);

        final var exception = assertThrows(ConstraintViolationException.class, () ->
            abfrageFreigabeValidator.validate(abfrage)
        );

        assertThat(exception.getMessage(), containsString("Pflichtangaben fehlen"));
        assertThat(exception.getMessage(), containsString("verfahrensstand: Der Wert darf nicht 'UNSPECIFIED' sein."));
    }

    @Test
    void validateNichtImStatusAngelegt() {
        final var abfrage = createAbfrage(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);

        assertDoesNotThrow(() -> abfrageFreigabeValidator.validate(abfrage));

        verifyNoInteractions(abfrageApiMapper, validator);
    }

    private static BauleitplanverfahrenModel createAbfrage(final StatusAbfrage statusAbfrage) {
        final var abfrage = new BauleitplanverfahrenModel();
        abfrage.setStatusAbfrage(statusAbfrage);
        return abfrage;
    }

    @SuppressWarnings("unchecked")
    private static ConstraintViolation<AbfrageAngelegtDto> createViolation(final String path, final String message) {
        final ConstraintViolation<AbfrageAngelegtDto> violation = mock(ConstraintViolation.class);
        final Path propertyPath = mock(Path.class);
        when(propertyPath.toString()).thenReturn(path);
        when(violation.getPropertyPath()).thenReturn(propertyPath);
        when(violation.getMessage()).thenReturn(message);
        return violation;
    }
}
