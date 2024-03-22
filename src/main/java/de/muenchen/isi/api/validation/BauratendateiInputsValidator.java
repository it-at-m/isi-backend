package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.bauratendatei.WithBauratendateiInputsDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class BauratendateiInputsValidator
    implements ConstraintValidator<HasFoerdermixRequiredSum, WithBauratendateiInputsDto> {

    @Override
    public boolean isValid(
        final WithBauratendateiInputsDto value,
        final ConstraintValidatorContext constraintValidatorContext
    ) {
        final var hasInputs = value.getHasBauratendateiInputs();
        final var inputBasis = value.getBauratendateiInputBasis();
        final var inputs = value.getBauratendateiInputs();

        // Vergleich nicht möglich
        if (hasInputs == null || !hasInputs || inputBasis == null || inputs == null || inputs.isEmpty()) {
            return true;
        }

        // Mapping von Förderart zu Jahr zu Summe Wohneinheiten
        final var wohneinheitenSum = new HashMap<String, HashMap<String, BigDecimal>>();

        for (final var input : inputs) {
            // Vergleich räumlicher Daten
            if (
                inputBasis.getGrundschulsprengel() != null &&
                !inputBasis.getGrundschulsprengel().containsAll(input.getGrundschulsprengel())
            ) {
                return false;
            }
            if (
                inputBasis.getMittelschulsprengel() != null &&
                !inputBasis.getMittelschulsprengel().containsAll(input.getMittelschulsprengel())
            ) {
                return false;
            }
            if (inputBasis.getViertel() != null && !inputBasis.getViertel().containsAll(input.getViertel())) {
                return false;
            }

            // Summieren der Wohneinheiten
            if (input.getWohneinheiten() != null) {
                for (final var wohneinheiten : input.getWohneinheiten()) {
                    wohneinheitenSum.merge(
                        wohneinheiten.getFoerderart(),
                        new HashMap<>(Map.of(wohneinheiten.getJahr(), wohneinheiten.getWohneinheiten())),
                        (present, current) -> {
                            present.merge(wohneinheiten.getJahr(), wohneinheiten.getWohneinheiten(), BigDecimal::add);
                            return present;
                        }
                    );
                }
            }
        }

        // Vergleich Wohneinheiten
        if (inputBasis.getWohneinheiten() != null) {
            for (final var wohneinheiten : inputBasis.getWohneinheiten()) {
                if (
                    !wohneinheitenSum.containsKey(wohneinheiten.getFoerderart()) ||
                    !wohneinheitenSum.get(wohneinheiten.getFoerderart()).containsKey(wohneinheiten.getJahr()) ||
                    !wohneinheitenSum
                        .get(wohneinheiten.getFoerderart())
                        .get(wohneinheiten.getJahr())
                        .equals(wohneinheiten.getWohneinheiten())
                ) {
                    return false;
                }
            }
        }

        return true;
    }
}