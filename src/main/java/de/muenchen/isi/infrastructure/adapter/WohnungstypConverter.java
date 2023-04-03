package de.muenchen.isi.infrastructure.adapter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import de.muenchen.isi.infrastructure.entity.enums.Wohnungstyp;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WohnungstypConverter extends AbstractBeanField {

    @Override
    protected Object convert(final String value) throws CsvDataTypeMismatchException {
        final Optional<Wohnungstyp> optWohnungstyp = Wohnungstyp.findByBezeichnung(value);
        if (optWohnungstyp.isPresent()) {
            return optWohnungstyp.get();
        } else {
            final var message = "Kein gültiger Wohnungstyp: " + value;
            log.error(message);
            throw new CsvDataTypeMismatchException(message);
        }
    }
}
