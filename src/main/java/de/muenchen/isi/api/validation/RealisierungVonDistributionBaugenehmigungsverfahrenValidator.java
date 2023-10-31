package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteBaugenehmigungsverfahrenAngelegtDto;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class RealisierungVonDistributionBaugenehmigungsverfahrenValidator
    extends DistributionValidator
    implements
        ConstraintValidator<
            RealisierungVonDistributionBaugenehmigungsverfahrenValid,
            AbfragevarianteBaugenehmigungsverfahrenAngelegtDto
        > {

    /**
     * @param value object to validate
     * @param context context in which the constraint is evaluate
     * @return true falls das Realisierungsjahr der Abfragevariante vor oder gleich der Realisierungsjahre der Baugebiete oder einer Baurate.
     */
    @Override
    public boolean isValid(
        final AbfragevarianteBaugenehmigungsverfahrenAngelegtDto value,
        final ConstraintValidatorContext context
    ) {
        boolean isValid = true;

        final List<BaugebietDto> nonTechnicalBaugebiete = getNonTechnicalBaugebiete(value.getBauabschnitte());
        final List<BaurateDto> bauratenFromAllTechnicalBaugebiete = getBauratenFromAllTechnicalBaugebiete(
            value.getBauabschnitte()
        );

        final boolean containsNonTechnicalBaugebiet = CollectionUtils.isNotEmpty(nonTechnicalBaugebiete);
        final boolean containsBauratenInTechnicalBaugebiet = CollectionUtils.isNotEmpty(
            bauratenFromAllTechnicalBaugebiete
        );

        if (containsNonTechnicalBaugebiet) {
            final Optional<Integer> minJahrBaugebiete = nonTechnicalBaugebiete
                .stream()
                .map(BaugebietDto::getRealisierungVon)
                .filter(Objects::nonNull)
                .min(Integer::compareTo);

            isValid = minJahrBaugebiete.isEmpty() || value.getRealisierungVon().compareTo(minJahrBaugebiete.get()) <= 0;
        } else if (containsBauratenInTechnicalBaugebiet) {
            final Optional<Integer> minJahrBauraten = bauratenFromAllTechnicalBaugebiete
                .stream()
                .map(BaurateDto::getJahr)
                .filter(Objects::nonNull)
                .min(Integer::compareTo);

            isValid = minJahrBauraten.isEmpty() || value.getRealisierungVon().compareTo(minJahrBauraten.get()) <= 0;
        }
        return isValid;
    }
}
