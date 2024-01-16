package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.BauabschnittDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class TechnicalAttributesValidator
    implements ConstraintValidator<TechnicalAttributesValid, List<BauabschnittDto>> {

    @Override
    public boolean isValid(List<BauabschnittDto> bauabschnitte, ConstraintValidatorContext context) {
        if (CollectionUtils.isEmpty(bauabschnitte)) {
            return true;
        }

        return (
            isValidForOption1(bauabschnitte) || isValidForOption2(bauabschnitte) || isValidForOption3(bauabschnitte)
        );
    }

    private boolean isValidForOption1(final List<BauabschnittDto> bauabschnitte) {
        return (
            (bauabschnitte
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
            hasBauraten(bauabschnitte)
        );
    }

    private boolean isValidForOption2(final List<BauabschnittDto> bauabschnitte) {
        return (
            (bauabschnitte
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
            hasBauraten(bauabschnitte)
        );
    }

    private boolean isValidForOption3(final List<BauabschnittDto> bauabschnitte) {
        return (
            (bauabschnitte
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
            hasBauraten(bauabschnitte)
        );
    }

    private boolean hasBauraten(final List<BauabschnittDto> bauabschnitte) {
        return (
            bauabschnitte
                .stream()
                .flatMap(bauabschnitt -> bauabschnitt.getBaugebiete().stream())
                .flatMap(baugebiet -> baugebiet.getBauraten().stream())
                .findAny()
                .isPresent()
        );
    }
}
