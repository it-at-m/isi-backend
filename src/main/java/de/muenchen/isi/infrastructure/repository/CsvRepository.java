package de.muenchen.isi.infrastructure.repository;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import de.muenchen.isi.infrastructure.csv.SobonOrientierungswertSozialeInfrastrukturCsv;
import de.muenchen.isi.infrastructure.csv.StaedtebaulicheOrientierungswertCsv;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.stereotype.Repository;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

@Repository
public class CsvRepository {

    private static final Character CSV_SEPERATOR = ';';

    /**
     * Erstellt je Zeile in der CSV-Datei ein {@link StaedtebaulicheOrientierungswertCsv}.
     *
     * @param csvImportFile zum Auslesen der {@link StaedtebaulicheOrientierungswertCsv}s.
     * @return das {@link StaedtebaulicheOrientierungswertCsv} je Zeile.
     * @throws CsvDataTypeMismatchException   falls nicht der passende Typ in Feld steht.
     * @throws CsvRequiredFieldEmptyException falls ein Feld keinen Wert (null oder leerer String) beinhaltet.
     */
    public List<StaedtebaulicheOrientierungswertCsv> readAllStaedtebaulicheOrientierungswertCsv(final InputStreamReader csvImportFile) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        try {
            final var csvToBean = new CsvToBeanBuilder<StaedtebaulicheOrientierungswertCsv>(csvImportFile)
                    .withSeparator(CSV_SEPERATOR)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
                    .withType(StaedtebaulicheOrientierungswertCsv.class)
                    .build();
            return Collections.list(IteratorUtils.asEnumeration(csvToBean.iterator()));
        } catch (final RuntimeException exception) {
            throw this.checkAndCastAndThrowCorrectException(exception);
        }
    }

    /**
     * Erstellt je Zeile in der CSV-Datei ein {@link SobonOrientierungswertSozialeInfrastrukturCsv}.
     *
     * @param csvImportFile zum Auslesen der {@link SobonOrientierungswertSozialeInfrastrukturCsv}s.
     * @return das {@link SobonOrientierungswertSozialeInfrastrukturCsv} je Zeile.
     * @throws CsvDataTypeMismatchException   falls nicht der passende Typ in Feld steht.
     * @throws CsvRequiredFieldEmptyException falls ein Feld keinen Wert (null oder leerer String) beinhaltet.
     */
    public List<SobonOrientierungswertSozialeInfrastrukturCsv> readAllSobonOrientierungswertSozialeInfrastrukturCsv(final InputStreamReader csvImportFile) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        try {
            final var csvToBean = new CsvToBeanBuilder<SobonOrientierungswertSozialeInfrastrukturCsv>(csvImportFile)
                    .withSeparator(CSV_SEPERATOR)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
                    .withType(SobonOrientierungswertSozialeInfrastrukturCsv.class)
                    .build();
            return Collections.list(IteratorUtils.asEnumeration(csvToBean.iterator()));
        } catch (final RuntimeException exception) {
            throw this.checkAndCastAndThrowCorrectException(exception);
        }
    }

    /**
     * Diese Methode führt für die {@link RuntimeException} einen Cast auf {@link CsvDataTypeMismatchException}
     * und {@link CsvRequiredFieldEmptyException} durch falls dieser möglich ist, anderfalls wird die {@link RuntimeException}
     * zurückgegeben.
     *
     * @param exception zum Prüfen und gegebenfalls casten.
     * @return die im Parameter gegebene {@link RuntimeException} falls kein Cast und Throw der
     * Exceptions {@link CsvDataTypeMismatchException} und {@link CsvRequiredFieldEmptyException} möglich ist.
     * @throws CsvDataTypeMismatchException   falls nicht der passende Typ in Feld steht.
     * @throws CsvRequiredFieldEmptyException falls ein Feld keinen Wert (null oder leerer String) beinhaltet.
     */
    protected RuntimeException checkAndCastAndThrowCorrectException(final RuntimeException exception) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        final var cause = exception.getCause();
        if (cause.getClass().equals(CsvDataTypeMismatchException.class)) {
            throw (CsvDataTypeMismatchException) cause;
        } else if (cause.getClass().equals(CsvRequiredFieldEmptyException.class)) {
            throw (CsvRequiredFieldEmptyException) cause;
        } else {
            return exception;
        }
    }

}
