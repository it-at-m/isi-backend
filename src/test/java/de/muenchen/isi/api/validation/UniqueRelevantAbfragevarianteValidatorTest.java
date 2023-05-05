package de.muenchen.isi.api.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UniqueRelevantAbfragevarianteValidatorTest {

    private final UniqueRelevantAbfragevarianteValidator uniqueRelevantAbfragevarianteValidator =
        new UniqueRelevantAbfragevarianteValidator();

    @Test
    void isValid() {
        AbfragevarianteDto abfragevarianteDto = new AbfragevarianteDto();
        abfragevarianteDto.setRelevant(false);
        abfragevarianteDto.setAbfragevariantenName("Variante 1");

        AbfragevarianteDto abfragevarianteDto1 = new AbfragevarianteDto();
        abfragevarianteDto1.setRelevant(true);
        abfragevarianteDto1.setAbfragevariantenName("Variante 2");

        AbfragevarianteDto abfragevarianteDto2 = new AbfragevarianteDto();
        abfragevarianteDto2.setRelevant(false);
        abfragevarianteDto.setAbfragevariantenName("Variante 3");

        List<AbfragevarianteDto> abfragevarianteDtoList = new ArrayList<>();

        abfragevarianteDtoList.add(abfragevarianteDto);
        abfragevarianteDtoList.add(abfragevarianteDto1);
        abfragevarianteDtoList.add(abfragevarianteDto2);

        assertThat(this.uniqueRelevantAbfragevarianteValidator.isValid(abfragevarianteDtoList, null), is(true));
    }

    @Test
    void isNotValid() {
        AbfragevarianteDto abfragevarianteDto = new AbfragevarianteDto();
        abfragevarianteDto.setRelevant(false);
        abfragevarianteDto.setAbfragevariantenName("Variante 1");

        AbfragevarianteDto abfragevarianteDto1 = new AbfragevarianteDto();
        abfragevarianteDto1.setRelevant(true);
        abfragevarianteDto1.setAbfragevariantenName("Variante 2");

        AbfragevarianteDto abfragevarianteDto2 = new AbfragevarianteDto();
        abfragevarianteDto2.setRelevant(true);
        abfragevarianteDto.setAbfragevariantenName("Variante 3");

        List<AbfragevarianteDto> abfragevarianteDtoList = new ArrayList<>();

        abfragevarianteDtoList.add(abfragevarianteDto);
        abfragevarianteDtoList.add(abfragevarianteDto1);
        abfragevarianteDtoList.add(abfragevarianteDto2);

        assertThat(this.uniqueRelevantAbfragevarianteValidator.isValid(abfragevarianteDtoList, null), is(false));
    }
}
