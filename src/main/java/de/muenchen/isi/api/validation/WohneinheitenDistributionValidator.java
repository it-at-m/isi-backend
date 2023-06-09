package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageerstellungAbfragevarianteAngelegtDto;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class WohneinheitenDistributionValidator
    extends DistributionValidator
    implements ConstraintValidator<WohneinheitenDistributionValid, AbfrageerstellungAbfragevarianteAngelegtDto> {

    /**
     * Validiert für die im Parameter gegebene Abfragevariante die über Baugebiete bzw. Bauraten verteilten Wohneinheiten.
     *
     * @param value als AbfragevarianteDto
     * @param context in welchem die Validierung stattfindet
     * @return true, falls die Anzahl der Wohneinheiten in der Abfragevariante der Summe der Wohneinheiten in den nicht technischen Baugebieten entspricht.
     * True, falls die Anzahl der Wohneinheiten in der Abfragevariante der Summe der Wohneinheiten in den Bauraten für technische Baugebiete entspricht.
     * True, falls keine Baugebiete vorhanden sind.
     * Andernfalls false.
     */
    @Override
    public boolean isValid(
        final AbfrageerstellungAbfragevarianteAngelegtDto value,
        final ConstraintValidatorContext context
    ) {
        boolean isValid = true;

        final List<BaugebietDto> nonTechnicalBaugebiete = getNonTechnicalBaugebiete(value);
        final List<BaurateDto> bauratenFromAllTechnicalBaugebiete = getBauratenFromAllTechnicalBaugebiete(value);

        final boolean containsNonTechnicalBaugebiet = CollectionUtils.isNotEmpty(nonTechnicalBaugebiete);
        final boolean containsBauratenInTechnicalBaugebiet = CollectionUtils.isNotEmpty(
            bauratenFromAllTechnicalBaugebiete
        );

        final var wohneinheitenAbfragevariante = ObjectUtils.isEmpty(value.getGesamtanzahlWe())
            ? 0
            : value.getGesamtanzahlWe();

        if (containsNonTechnicalBaugebiet) {
            final var sumVerteilteWohneinheitenBaugebiete = nonTechnicalBaugebiete
                .stream()
                .map(baugebiet -> ObjectUtils.isEmpty(baugebiet.getGesamtanzahlWe()) ? 0 : baugebiet.getGesamtanzahlWe()
                )
                .reduce(0, Integer::sum);

            isValid = wohneinheitenAbfragevariante == sumVerteilteWohneinheitenBaugebiete;
        } else if (containsBauratenInTechnicalBaugebiet) {
            final var sumVerteilteWohneinheitenBauraten = bauratenFromAllTechnicalBaugebiete
                .stream()
                .map(baurate -> ObjectUtils.isEmpty(baurate.getAnzahlWeGeplant()) ? 0 : baurate.getAnzahlWeGeplant())
                .reduce(0, Integer::sum);

            isValid = wohneinheitenAbfragevariante == sumVerteilteWohneinheitenBauraten;
        }
        return isValid;
    }
}
