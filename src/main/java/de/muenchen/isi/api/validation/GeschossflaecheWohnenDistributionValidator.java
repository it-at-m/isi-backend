package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageerstellungAbfragevarianteAngelegtDto;
import java.math.BigDecimal;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class GeschossflaecheWohnenDistributionValidator
    implements
        ConstraintValidator<GeschossflaecheWohnenDistributionValid, AbfrageerstellungAbfragevarianteAngelegtDto> {

    /**
     * Prüft, ob die Summe der über die Baugebiete verteilten Geschossfläche Wohnen der Geschossfläche Wohnen in der Abfragevariante entspricht.
     *
     * @param value als AbfragevarianteDto
     * @param context in welchem die Validierung stattfindet
     * @return true falls die Geschossfläche Wohnen in der Abfragevariante der Summe der Geschossfläche Wohnen in den Baugebieten entspricht oder falls keine Baugebiete vorhanden sind. Andernfalls false.
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
            final var geschossflaecheWohnenAbfragevariante = ObjectUtils.isEmpty(value.getGeschossflaecheWohnen())
                ? BigDecimal.ZERO
                : value.getGeschossflaecheWohnen();

            final var sumVerteilteGeschossflaecheWohnenBaugebiete = CollectionUtils
                .emptyIfNull(value.getBauabschnitte())
                .stream()
                .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
                .filter(baugebiet -> BooleanUtils.isFalse(baugebiet.getTechnical()))
                .map(baugebiet ->
                    ObjectUtils.isEmpty(baugebiet.getGeschossflaecheWohnen())
                        ? BigDecimal.ZERO
                        : baugebiet.getGeschossflaecheWohnen()
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            final var sumVerteilteGeschossflaecheWohnenBauraten = CollectionUtils
                .emptyIfNull(value.getBauabschnitte())
                .stream()
                .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
                .filter(baugebiet -> BooleanUtils.isTrue(baugebiet.getTechnical()))
                .flatMap(baugebiet -> CollectionUtils.emptyIfNull(baugebiet.getBauraten()).stream())
                .map(baurate ->
                    ObjectUtils.isEmpty(baurate.getGeschossflaecheWohnenGeplant())
                        ? BigDecimal.ZERO
                        : baurate.getGeschossflaecheWohnenGeplant()
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            final var sumVerteilteGeschossflaecheWohnen = sumVerteilteGeschossflaecheWohnenBaugebiete.max(
                sumVerteilteGeschossflaecheWohnenBauraten
            );

            isValid = sumVerteilteGeschossflaecheWohnen.compareTo(geschossflaecheWohnenAbfragevariante) == 0;
        }
        return isValid;
    }
}
