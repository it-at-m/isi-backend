package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.bauratendatei.WithBauratendateiInputs;
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
    implements ConstraintValidator<HasFoerdermixRequiredSum, WithBauratendateiInputs> {

    @Override
    public boolean isValid(
        final WithBauratendateiInputs value,
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
        final var woheinheitenSum = new HashMap<String, HashMap<String, BigDecimal>>();

        for (final var input : inputs) {
            // Vergleich räumlicher Daten
            if (!inputBasis.getGrundschulsprengel().containsAll(input.getGrundschulsprengel())) {
                return false;
            }
            if (!inputBasis.getMittelschulsprengel().containsAll(input.getMittelschulsprengel())) {
                return false;
            }
            if (!inputBasis.getViertel().containsAll(input.getViertel())) {
                return false;
            }

            // Summieren der Wohneinheiten
            for (final var wohneinheiten : input.getWohneinheiten()) {
                woheinheitenSum.merge(
                    wohneinheiten.getFoerderart(),
                    new HashMap<>(Map.of(wohneinheiten.getJahr(), wohneinheiten.getWohneinheiten())),
                    (present, current) -> {
                        present.merge(wohneinheiten.getJahr(), wohneinheiten.getWohneinheiten(), BigDecimal::add);
                        return present;
                    }
                );
            }
        }

        // Vergleich Wohneinheiten
        for (final var wohneinheiten : inputBasis.getWohneinheiten()) {
            if (
                woheinheitenSum.get(wohneinheiten.getFoerderart()) == null ||
                woheinheitenSum.get(wohneinheiten.getFoerderart()).get(wohneinheiten.getJahr()) == null ||
                !woheinheitenSum
                    .get(wohneinheiten.getFoerderart())
                    .get(wohneinheiten.getJahr())
                    .equals(wohneinheiten.getWohneinheiten())
            ) {
                return false;
            }
        }

        return true;
    }
}
