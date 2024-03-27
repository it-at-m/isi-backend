package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungDto;
import de.muenchen.isi.api.dto.bauratendatei.BauratendateiInputDto;
import de.muenchen.isi.api.mapper.BauratendateiApiMapperImpl;
import de.muenchen.isi.domain.model.bauratendatei.BauratendateiInputModel;
import de.muenchen.isi.domain.service.BauratendateiInputService;
import java.util.List;
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
class BauratendateiInputValidatorTest {

    @Mock
    private BauratendateiInputService bauratendateiInputService;

    private BauratendateiInputValidator bauratendateiInputValidator;

    @BeforeEach
    public void beforeEach() {
        bauratendateiInputValidator =
            new BauratendateiInputValidator(bauratendateiInputService, new BauratendateiApiMapperImpl());
        Mockito.reset(bauratendateiInputService);
    }

    @Test
    void isValidHasBauratendateiInputIsNull() {
        final var abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungDto();
        abfragevariante.setHasBauratendateiInput(null);
        abfragevariante.setBauratendateiInputBasis(null);
        abfragevariante.setBauratendateiInput(null);

        Mockito.when(bauratendateiInputService.equals(Mockito.any(), Mockito.any())).thenReturn(false);

        var result = bauratendateiInputValidator.isValid(abfragevariante, null);
        assertThat(result, is(true));

        Mockito.verify(bauratendateiInputService, Mockito.times(0)).equals(Mockito.any(), Mockito.anyList());
    }

    @Test
    void isValidHasBauratendateiInputIsFalse() {
        final var abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungDto();
        abfragevariante.setHasBauratendateiInput(false);
        abfragevariante.setBauratendateiInputBasis(null);
        abfragevariante.setBauratendateiInput(null);

        Mockito.when(bauratendateiInputService.equals(Mockito.any(), Mockito.any())).thenReturn(false);

        var result = bauratendateiInputValidator.isValid(abfragevariante, null);
        assertThat(result, is(true));

        Mockito.verify(bauratendateiInputService, Mockito.times(0)).equals(Mockito.any(), Mockito.anyList());
    }

    @Test
    void isValidHasBauratendateiInputIsTrueAndEqualsIsFalse() {
        final var abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungDto();
        abfragevariante.setHasBauratendateiInput(true);
        abfragevariante.setBauratendateiInputBasis(new BauratendateiInputDto());
        abfragevariante.setBauratendateiInput(List.of(new BauratendateiInputDto()));

        Mockito
            .when(
                bauratendateiInputService.equals(new BauratendateiInputModel(), List.of(new BauratendateiInputModel()))
            )
            .thenReturn(false);

        var result = bauratendateiInputValidator.isValid(abfragevariante, null);
        assertThat(result, is(false));

        Mockito
            .verify(bauratendateiInputService, Mockito.times(1))
            .equals(new BauratendateiInputModel(), List.of(new BauratendateiInputModel()));
    }

    @Test
    void isValidHasBauratendateiInputIsTrueAndEqualsIsTrue() {
        final var abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungDto();
        abfragevariante.setHasBauratendateiInput(true);
        abfragevariante.setBauratendateiInputBasis(new BauratendateiInputDto());
        abfragevariante.setBauratendateiInput(List.of(new BauratendateiInputDto()));

        Mockito
            .when(
                bauratendateiInputService.equals(new BauratendateiInputModel(), List.of(new BauratendateiInputModel()))
            )
            .thenReturn(true);

        var result = bauratendateiInputValidator.isValid(abfragevariante, null);
        assertThat(result, is(true));

        Mockito
            .verify(bauratendateiInputService, Mockito.times(1))
            .equals(new BauratendateiInputModel(), List.of(new BauratendateiInputModel()));
    }
}
