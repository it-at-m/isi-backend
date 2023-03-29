package de.muenchen.isi.infrastructure.adapter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SobonVerfahrensgrundsaetzeJahrConverter extends AbstractBeanField {

    @Override
    protected Object convert(final String value) throws CsvDataTypeMismatchException {
        final Optional<SobonVerfahrensgrundsaetzeJahr> optWohnungstyp =
            SobonVerfahrensgrundsaetzeJahr.findByBezeichnung(value);
        if (optWohnungstyp.isPresent()) {
            return optWohnungstyp.get();
        } else {
            final var message = "Kein gültiges SobonVerfahrensgrundsaetzeJahr: " + value;
            log.error(message);
            throw new CsvDataTypeMismatchException(message);
        }
    }
}
