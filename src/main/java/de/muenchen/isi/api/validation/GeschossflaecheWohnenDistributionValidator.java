package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfragevarianteAngelegtDto;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class GeschossflaecheWohnenDistributionValidator
    extends DistributionValidator
    implements ConstraintValidator<GeschossflaecheWohnenDistributionValid, AbfragevarianteAngelegtDto> {

    /**
     * Validiert für die im Parameter gegebene Abfragevariante die über Baugebiete bzw. Bauraten verteilte Geschossfläche Wohnen.
     *
     * @param value als AbfragevarianteDto
     * @param context in welchem die Validierung stattfindet
     * @return true, falls die Geschossfläche Wohnen in der Abfragevariante der Summe der Geschossfläche Wohnen in den nicht technischen Baugebieten entspricht.
     * True, falls die Geschossfläche Wohnen in der Abfragevariante der Summe der Geschossfläche Wohnen in den Bauraten für technische Baugebiete entspricht.
     * True, falls keine Baugebiete vorhanden sind.
     * Andernfalls false.
     */
    @Override
    public boolean isValid(final AbfragevarianteAngelegtDto value, final ConstraintValidatorContext context) {
        if (ObjectUtils.isNotEmpty(value.getGesamtanzahlWe())) {
            return true;
        }

        boolean isValid = true;

        final List<BaugebietDto> nonTechnicalBaugebiete = getNonTechnicalBaugebiete(value);
        final List<BaurateDto> bauratenFromAllTechnicalBaugebiete = getBauratenFromAllTechnicalBaugebiete(value);

        final boolean containsNonTechnicalBaugebiet = CollectionUtils.isNotEmpty(nonTechnicalBaugebiete);
        final boolean containsBauratenInTechnicalBaugebiet = CollectionUtils.isNotEmpty(
            bauratenFromAllTechnicalBaugebiete
        );

        final var geschossflaecheWohnenAbfragevariante = ObjectUtils.isEmpty(value.getGeschossflaecheWohnen())
            ? BigDecimal.ZERO
            : value.getGeschossflaecheWohnen();

        if (containsNonTechnicalBaugebiet) {
            final var sumVerteilteGeschossflaecheWohnenBaugebiete = nonTechnicalBaugebiete
                .stream()
                .map(baugebiet ->
                    ObjectUtils.isEmpty(baugebiet.getGfWohnenGeplant())
                        ? BigDecimal.ZERO
                        : baugebiet.getGfWohnenGeplant()
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            isValid = sumVerteilteGeschossflaecheWohnenBaugebiete.compareTo(geschossflaecheWohnenAbfragevariante) == 0;
        } else if (containsBauratenInTechnicalBaugebiet) {
            final var sumVerteilteGeschossflaecheWohnenBauraten = bauratenFromAllTechnicalBaugebiete
                .stream()
                .map(baurate ->
                    ObjectUtils.isEmpty(baurate.getGfWohnenGeplant()) ? BigDecimal.ZERO : baurate.getGfWohnenGeplant()
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            isValid = sumVerteilteGeschossflaecheWohnenBauraten.compareTo(geschossflaecheWohnenAbfragevariante) == 0;
        }
        return isValid;
    }
}
