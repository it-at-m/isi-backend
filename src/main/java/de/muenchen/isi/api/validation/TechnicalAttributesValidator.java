package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.BaugebietDto;
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

    // Möglichkeit 1: Die Bauraten sind über fachliche Bauabschnitte und Baugebiete an die Abfragevariante angebunden
    // Abfragevariante -> Bauabschnitte (nicht technisch) -> Baugebiete (nicht technisch) -> Bauraten
    private boolean isValidForOption1(final AbfragevarianteAngelegtDto abfragevarianteDto) {
        return (
            abfragevarianteDto.getBauabschnitte() != null &&
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
                )
        );
    }

    // Möglichkeit 2: Die Bauraten sind über fachliche Baugebiete an die Abfragevariante angebunden
    // Abfragevariante -> Bauabschnitt (technisch) -> Baugebiete (nicht technisch) -> Bauraten
    private boolean isValidForOption2(final AbfragevarianteAngelegtDto abfragevarianteDto) {
        return (
            abfragevarianteDto.getBauabschnitte() != null &&
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
                )
        );
    }

    // Möglichkeit 3: Die Bauraten sind direkt an die Abfragevariante angebunden (nur erlaubt, wenn Bauabschnitte und Baugebiete technisch sind)
    // Abfragevariante -> Bauabschnitt (technisch) -> Baugebiet (technisch) -> Bauraten
    private boolean isValidForOption3(final AbfragevarianteAngelegtDto abfragevarianteDto) {
        return (
            abfragevarianteDto.getBauabschnitte() != null &&
            abfragevarianteDto
                .getBauabschnitte()
                .stream()
                .allMatch(bauabschnitt ->
                    bauabschnitt.getTechnical() &&
                    bauabschnitt.getBaugebiete() != null &&
                    bauabschnitt.getBaugebiete().stream().allMatch(BaugebietDto::getTechnical)
                )
        );
    }
}
