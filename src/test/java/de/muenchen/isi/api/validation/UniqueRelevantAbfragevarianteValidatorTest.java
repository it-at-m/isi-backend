package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.AbfragevarianteResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UniqueRelevantAbfragevarianteValidatorTest {

    private final UniqueRelevantAbfragevarianteValidator uniqueRelevantAbfragevarianteValidator =
            new UniqueRelevantAbfragevarianteValidator();

    @Test
    void isValid() {
        AbfragevarianteResponseDto abfragevarianteDto = new AbfragevarianteResponseDto();
        abfragevarianteDto.setRelevant(false);
        abfragevarianteDto.setAbfragevariantenName("Variante 1");

        AbfragevarianteResponseDto abfragevarianteDto1 = new AbfragevarianteResponseDto();
        abfragevarianteDto1.setRelevant(true);
        abfragevarianteDto1.setAbfragevariantenName("Variante 2");

        AbfragevarianteResponseDto abfragevarianteDto2 = new AbfragevarianteResponseDto();
        abfragevarianteDto2.setRelevant(false);
        abfragevarianteDto.setAbfragevariantenName("Variante 3");

        List<AbfragevarianteResponseDto> abfragevarianteDtoList = new ArrayList<>();

        abfragevarianteDtoList.add(abfragevarianteDto);
        abfragevarianteDtoList.add(abfragevarianteDto1);
        abfragevarianteDtoList.add(abfragevarianteDto2);

        assertThat(this.uniqueRelevantAbfragevarianteValidator.isValid(abfragevarianteDtoList, null), is(true));
    }

    @Test
    void isNotValid() {
        AbfragevarianteResponseDto abfragevarianteDto = new AbfragevarianteResponseDto();
        abfragevarianteDto.setRelevant(false);
        abfragevarianteDto.setAbfragevariantenName("Variante 1");

        AbfragevarianteResponseDto abfragevarianteDto1 = new AbfragevarianteResponseDto();
        abfragevarianteDto1.setRelevant(true);
        abfragevarianteDto1.setAbfragevariantenName("Variante 2");

        AbfragevarianteResponseDto abfragevarianteDto2 = new AbfragevarianteResponseDto();
        abfragevarianteDto2.setRelevant(true);
        abfragevarianteDto.setAbfragevariantenName("Variante 3");

        List<AbfragevarianteResponseDto> abfragevarianteDtoList = new ArrayList<>();

        abfragevarianteDtoList.add(abfragevarianteDto);
        abfragevarianteDtoList.add(abfragevarianteDto1);
        abfragevarianteDtoList.add(abfragevarianteDto2);

        assertThat(this.uniqueRelevantAbfragevarianteValidator.isValid(abfragevarianteDtoList, null), is(false));
    }
}
