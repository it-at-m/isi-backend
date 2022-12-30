package de.muenchen.isi.infrastructure.adapter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import de.muenchen.isi.infrastructure.entity.enums.Einrichtungstyp;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class EinrichtungstypConverter extends AbstractBeanField {

    @Override
    protected Object convert(final String value) throws CsvDataTypeMismatchException {
        final Optional<Einrichtungstyp> optWohnungstyp = Einrichtungstyp.findByBezeichnung(value);
        if (optWohnungstyp.isPresent()) {
            return optWohnungstyp.get();
        } else {
            final var message = "Kein gültiger Einrichtungstyp: " + value;
            log.error(message);
            throw new CsvDataTypeMismatchException(message);
        }
    }

}
