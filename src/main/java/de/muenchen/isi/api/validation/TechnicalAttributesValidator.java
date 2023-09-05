package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import de.muenchen.isi.api.dto.BaugebietDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TechnicalAttributesValidator implements ConstraintValidator<TechnicalAttributesValid, AbfragevarianteDto> {

    @Override
    public boolean isValid(AbfragevarianteDto abfragevarianteDto, ConstraintValidatorContext context) {
        if (abfragevarianteDto == null) {
            return true; // Wenn die DTO null ist, überspringe die Validierung
        }

        return isValidForOption1(abfragevarianteDto) ||
                isValidForOption2(abfragevarianteDto) ||
                isValidForOption3(abfragevarianteDto);
    }

    // Möglichkeit 1: Die Bauraten sind über fachliche Bauabschnitte und Baugebiete an die Abfragevariante angebunden
    private boolean isValidForOption1(AbfragevarianteDto abfragevarianteDto) {
        return abfragevarianteDto.getBauabschnitte() != null &&
                abfragevarianteDto.getBauabschnitte().stream()
                        .anyMatch(bauabschnitt -> !bauabschnitt.getTechnical() &&
                                bauabschnitt.getBaugebiete() != null &&
                                bauabschnitt.getBaugebiete().stream()
                                        .anyMatch(baugebiet -> !baugebiet.getTechnical() &&
                                                baugebiet.getBauraten() != null &&
                                                !baugebiet.getBauraten().isEmpty()));
    }

    // Möglichkeit 2: Die Bauraten sind über fachliche Baugebiete an die Abfragevariante angebunden
    private boolean isValidForOption2(AbfragevarianteDto abfragevarianteDto) {
        return abfragevarianteDto.getBauabschnitte() != null &&
                abfragevarianteDto.getBauabschnitte().stream()
                        .anyMatch(bauabschnitt -> bauabschnitt.getTechnical() &&
                                bauabschnitt.getBaugebiete() != null &&
                                bauabschnitt.getBaugebiete().stream()
                                        .anyMatch(baugebiet -> !baugebiet.getTechnical() &&
                                                baugebiet.getBauraten() != null &&
                                                !baugebiet.getBauraten().isEmpty()));
    }

    // Möglichkeit 3: Die Bauraten sind direkt an die Abfragevariante angebunden (nur erlaubt, wenn Bauabschnitte und Baugebiete technisch sind)
    private boolean isValidForOption3(AbfragevarianteDto abfragevarianteDto) {
        return abfragevarianteDto.getBauabschnitte() != null &&
                abfragevarianteDto.getBauabschnitte().stream()
                        .allMatch(bauabschnitt -> bauabschnitt.getTechnical() &&
                                bauabschnitt.getBaugebiete() != null &&
                                bauabschnitt.getBaugebiete().stream()
                                        .allMatch(BaugebietDto::getTechnical));
    }
}
