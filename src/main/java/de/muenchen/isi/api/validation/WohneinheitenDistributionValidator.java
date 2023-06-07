package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageerstellungAbfragevarianteAngelegtDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class WohneinheitenDistributionValidator
    implements ConstraintValidator<WohneinheitenDistributionValid, AbfrageerstellungAbfragevarianteAngelegtDto> {

    /**
     * Prüft, ob die Summe der über die Baugebiete verteilten Wohneinheiten der Anzahl der Wohneinheiten in der Abfragevariante entspricht.
     *
     * @param value als AbfragevarianteDto
     * @param context in welchem die Validierung stattfindet
     * @return true falls die Anzahl der Wohneinheiten in der Abfragevariante der Summe der Wohneinheiten in den Baugebieten entspricht oder falls keine Baugebiete vorhanden sind. Andernfalls false.
     */
    @Override
    public boolean isValid(
        final AbfrageerstellungAbfragevarianteAngelegtDto value,
        final ConstraintValidatorContext context
    ) {
        boolean isValid = true;

        final boolean containsNonTechnicalBaugebiet = CollectionUtils
            .emptyIfNull(value.getBauabschnitte())
            .stream()
            .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
            .anyMatch(baugebiet -> BooleanUtils.isFalse(baugebiet.getTechnical()));

        final boolean containsBauratenInTechnicalBaugebiet = CollectionUtils
            .emptyIfNull(value.getBauabschnitte())
            .stream()
            .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
            .filter(baugebiet -> BooleanUtils.isTrue(baugebiet.getTechnical()))
            .flatMap(baugebiet -> CollectionUtils.emptyIfNull(baugebiet.getBauraten()).stream())
            .findFirst()
            .isPresent();

        if (containsNonTechnicalBaugebiet || containsBauratenInTechnicalBaugebiet) {
            final var wohneinheitenAbfragevariante = ObjectUtils.isEmpty(value.getGesamtanzahlWe())
                ? 0
                : value.getGesamtanzahlWe();

            final var sumVerteilteWohneinheitenBaugebiete = CollectionUtils
                .emptyIfNull(value.getBauabschnitte())
                .stream()
                .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
                .filter(baugebiet -> BooleanUtils.isFalse(baugebiet.getTechnical()))
                .map(baugebiet -> ObjectUtils.isEmpty(baugebiet.getGesamtanzahlWe()) ? 0 : baugebiet.getGesamtanzahlWe()
                )
                .reduce(0, Integer::sum);

            final var sumVerteilteWohneinheitenBauraten = CollectionUtils
                .emptyIfNull(value.getBauabschnitte())
                .stream()
                .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
                .filter(baugebiet -> BooleanUtils.isTrue(baugebiet.getTechnical()))
                .flatMap(baugebiet -> CollectionUtils.emptyIfNull(baugebiet.getBauraten()).stream())
                .map(baurate -> ObjectUtils.isEmpty(baurate.getAnzahlWeGeplant()) ? 0 : baurate.getAnzahlWeGeplant())
                .reduce(0, Integer::sum);

            final var sumVerteilteWohneinheiten = Integer.max(
                sumVerteilteWohneinheitenBaugebiete,
                sumVerteilteWohneinheitenBauraten
            );

            isValid = wohneinheitenAbfragevariante == sumVerteilteWohneinheiten;
        }
        return isValid;
    }
}
