package de.muenchen.isi.infrastructure.adapter;

import com.opencsv.bean.AbstractBeanField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JahrConverter extends AbstractBeanField {

    @Override
    protected Object convert(String value) {
        final var dateTimeFormatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy")
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter();
        return LocalDate.parse(value, dateTimeFormatter);
    }
}
