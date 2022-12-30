package de.muenchen.isi.infrastructure.adapter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class AltersklasseConverter extends AbstractBeanField {

    @Override
    protected Object convert(final String value) throws CsvDataTypeMismatchException {
        final Optional<Altersklasse> optWohnungstyp = Altersklasse.findByBezeichnung(value);
        if (optWohnungstyp.isPresent()) {
            return optWohnungstyp.get();
        } else {
            final var message = "Keine gültige Altersklasse: " + value;
            log.error(message);
            throw new CsvDataTypeMismatchException(message);
        }
    }

}
