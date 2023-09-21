package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfragevarianteAngelegtDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TechnicalAttributesValidator
    implements ConstraintValidator<TechnicalAttributesValid, AbfragevarianteAngelegtDto> {

    @Override
    public boolean isValid(AbfragevarianteAngelegtDto abfragevarianteDto, ConstraintValidatorContext context) {
        if (abfragevarianteDto == null) {
            return true; // Wenn die DTO null ist, überspringe die Validierung
        }

        return (
            isValidForOption1(abfragevarianteDto) ||
            isValidForOption2(abfragevarianteDto) ||
            isValidForOption3(abfragevarianteDto)
        );
    }

    private boolean isValidForOption1(AbfragevarianteAngelegtDto abfragevarianteDto) {
        return (
            (abfragevarianteDto.getBauabschnitte() != null &&
                abfragevarianteDto
                    .getBauabschnitte()
                    .stream()
                    .anyMatch(bauabschnitt ->
                        !bauabschnitt.getTechnical() &&
                        bauabschnitt.getBaugebiete() != null &&
                        bauabschnitt
                            .getBaugebiete()
                            .stream()
                            .anyMatch(baugebiet ->
                                !baugebiet.getTechnical() &&
                                baugebiet.getBauraten() != null &&
                                !baugebiet.getBauraten().isEmpty()
                            )
                    )) &&
            hasBauraten(abfragevarianteDto)
        );
    }

    private boolean isValidForOption2(AbfragevarianteAngelegtDto abfragevarianteDto) {
        return (
            (abfragevarianteDto.getBauabschnitte() != null &&
                abfragevarianteDto
                    .getBauabschnitte()
                    .stream()
                    .anyMatch(bauabschnitt ->
                        bauabschnitt.getTechnical() &&
                        bauabschnitt.getBaugebiete() != null &&
                        bauabschnitt
                            .getBaugebiete()
                            .stream()
                            .anyMatch(baugebiet ->
                                !baugebiet.getTechnical() &&
                                baugebiet.getBauraten() != null &&
                                !baugebiet.getBauraten().isEmpty()
                            )
                    )) &&
            hasBauraten(abfragevarianteDto)
        );
    }

    private boolean isValidForOption3(AbfragevarianteAngelegtDto abfragevarianteDto) {
        return (
            (abfragevarianteDto.getBauabschnitte() != null &&
                abfragevarianteDto
                    .getBauabschnitte()
                    .stream()
                    .allMatch(bauabschnitt ->
                        bauabschnitt.getTechnical() &&
                        bauabschnitt.getBaugebiete() != null &&
                        bauabschnitt
                            .getBaugebiete()
                            .stream()
                            .allMatch(baugebiet ->
                                baugebiet.getTechnical() &&
                                baugebiet.getBauraten() != null &&
                                !baugebiet.getBauraten().isEmpty()
                            )
                    )) &&
            hasBauraten(abfragevarianteDto)
        );
    }

    private boolean hasBauraten(AbfragevarianteAngelegtDto abfragevarianteDto) {
        return (
            abfragevarianteDto.getBauabschnitte() != null &&
            abfragevarianteDto
                .getBauabschnitte()
                .stream()
                .flatMap(bauabschnitt -> bauabschnitt.getBaugebiete().stream())
                .flatMap(baugebiet -> baugebiet.getBauraten().stream())
                .findAny()
                .isPresent()
        );
    }
}
