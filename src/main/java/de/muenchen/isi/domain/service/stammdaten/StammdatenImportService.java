package de.muenchen.isi.domain.service.stammdaten;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import de.muenchen.isi.domain.exception.CsvAttributeErrorException;
import de.muenchen.isi.domain.exception.FileImportFailedException;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapper;
import de.muenchen.isi.infrastructure.csv.PrognosedatenKitaPlbCsv;
import de.muenchen.isi.infrastructure.csv.SobonOrientierungswertSozialeInfrastrukturCsv;
import de.muenchen.isi.infrastructure.csv.StaedtebaulicheOrientierungswertCsv;
import de.muenchen.isi.infrastructure.repository.CsvRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.PrognoseKitaPlbRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.SobonOrientierungswertSozialeInfrastrukturRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class StammdatenImportService {

    private final CsvRepository csvRepository;

    private final StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    private final SobonOrientierungswertSozialeInfrastrukturRepository sobonOrientierungswertSozialeInfrastrukturRepository;

    private final PrognoseKitaPlbRepository prognoseKitaPlbRepository;

    private final StammdatenDomainMapper stammdatenDomainMapper;

    /**
     * Die Methode extrahiert aus der CSV-Datei im Parameter, die entsprechenden Entitäten
     * und persistiert diese in der Datenbank.
     *
     * @param csvImportFile mit den Informationen bezüglich {@link PrognosedatenKitaPlbCsv}.
     * @throws FileImportFailedException  tritt auf, falls ein Fehler beim Import passiert.
     * @throws CsvAttributeErrorException tritt auf, falls ein Attribut in der CSV nicht gesetzt oder fehlerhaft ist.
     */
    public void importPrognosedatenKitaPlb(final MultipartFile csvImportFile)
        throws FileImportFailedException, CsvAttributeErrorException {
        try (final var inputStream = csvImportFile.getInputStream()) {
            this.importPrognosedatenKitaPlb(inputStream);
        } catch (final IOException exception) {
            final var message = "Der Import einer CSV-Datei für Prognosedaten der KitaPlb ist fehlgeschlagen.";
            log.error(message);
            log.error(exception.getMessage());
            throw new FileImportFailedException(message, exception);
        }
    }

    /**
     * Die Methode extrahiert aus der CSV-Datei im Parameter, die entsprechenden Entitäten und persistiert diese in der Datenbank.
     *
     * @param csvImportFile mit den Informationen bezüglich {@link PrognosedatenKitaPlbCsv}.
     * @throws FileImportFailedException  tritt auf, falls ein Fehler beim Import passiert.
     * @throws CsvAttributeErrorException tritt auf, falls ein Attribut in der CSV nicht gesetzt oder fehlerhaft ist.
     */
    public void importPrognosedatenKitaPlb(final InputStream csvImportFile)
        throws FileImportFailedException, CsvAttributeErrorException {
        try (final InputStreamReader csvInputStreamReader = new InputStreamReader(csvImportFile)) {
            final var entities =
                this.csvRepository.readAllPrognosedatenKitaPlbCsv(csvInputStreamReader)
                    .stream()
                    .flatMap(stammdatenDomainMapper::csv2EntityForEachAltersgruppe)
                    .toList();
            prognoseKitaPlbRepository.saveAll(entities);
        } catch (final CsvDataTypeMismatchException | CsvRequiredFieldEmptyException exception) {
            log.error(exception.getMessage());
            throw new CsvAttributeErrorException(exception.getMessage(), exception);
        } catch (final IOException | DataAccessException exception) {
            final var message = "Der Import einer CSV-Datei für Prognosedaten der KitaPlb ist fehlgeschlagen.";
            log.error(message);
            log.error(exception.getMessage());
            throw new FileImportFailedException(message, exception);
        }
    }

    /**
     * Die Methode extrahiert aus der CSV-Datei im Parameter, die entsprechenden Entitäten
     * und persistiert diese in der Datenbank.
     *
     * @param csvImportFile mit den Informationen bezüglich {@link StaedtebaulicheOrientierungswertCsv}.
     * @throws FileImportFailedException  tritt auf, falls ein Fehler beim Import passiert.
     * @throws CsvAttributeErrorException tritt auf, falls ein Attribut in der CSV nicht gesetzt oder fehlerhaft ist.
     */
    public void importStaedtebaulicheOrientierungswerte(final MultipartFile csvImportFile)
        throws FileImportFailedException, CsvAttributeErrorException {
        try (final var inputStream = csvImportFile.getInputStream()) {
            this.importStaedtebaulicheOrientierungswerte(inputStream);
        } catch (final IOException exception) {
            final var message = "Der Import einer CSV-Datei für StaedtebaulicheOrientierungswerte ist fehlgeschlagen.";
            log.error(message);
            log.error(exception.getMessage());
            throw new FileImportFailedException(message, exception);
        }
    }

    /**
     * Die Methode extrahiert aus der CSV-Datei im Parameter, die entsprechenden Entitäten und persistiert diese in der Datenbank.
     *
     * @param csvImportFile mit den Informationen bezüglich {@link StaedtebaulicheOrientierungswertCsv}.
     * @throws FileImportFailedException  tritt auf, falls ein Fehler beim Import passiert.
     * @throws CsvAttributeErrorException tritt auf, falls ein Attribut in der CSV nicht gesetzt oder fehlerhaft ist.
     */
    public void importStaedtebaulicheOrientierungswerte(final InputStream csvImportFile)
        throws FileImportFailedException, CsvAttributeErrorException {
        try (final InputStreamReader csvInputStreamReader = new InputStreamReader(csvImportFile)) {
            final var entities =
                this.csvRepository.readAllStaedtebaulicheOrientierungswertCsv(csvInputStreamReader)
                    .stream()
                    .map(this.stammdatenDomainMapper::csv2Entity)
                    .toList();
            this.staedtebaulicheOrientierungswertRepository.saveAll(entities);
        } catch (final CsvDataTypeMismatchException | CsvRequiredFieldEmptyException exception) {
            log.error(exception.getMessage());
            throw new CsvAttributeErrorException(exception.getMessage(), exception);
        } catch (final IOException | DataAccessException exception) {
            final var message = "Der Import einer CSV-Datei für StaedtebaulicheOrientierungswerte ist fehlgeschlagen.";
            log.error(message);
            log.error(exception.getMessage());
            throw new FileImportFailedException(message, exception);
        }
    }

    /**
     * Die Methode extrahiert aus der CSV-Datei im Parameter, die entsprechenden Entitäten und persistiert diese in der Datenbank.
     *
     * @param csvImportFile mit den Informationen bezüglich {@link SobonOrientierungswertSozialeInfrastrukturCsv}.
     * @throws FileImportFailedException  tritt auf, falls ein Fehler beim Import passiert.
     * @throws CsvAttributeErrorException tritt auf, falls ein Attribut in der CSV nicht gesetzt oder fehlerhaft ist.
     */
    public void importSobonOrientierungswerteSozialeInfrastruktur(final MultipartFile csvImportFile)
        throws CsvAttributeErrorException, FileImportFailedException {
        try (final var inputStream = csvImportFile.getInputStream()) {
            this.importSobonOrientierungswerteSozialeInfrastruktur(inputStream);
        } catch (final IOException exception) {
            final var message =
                "Der Import einer CSV-Datei für SoBoNOrientierungswerteSozialeInfrastruktur ist fehlgeschlagen.";
            log.error(message);
            log.error(exception.getMessage());
            throw new FileImportFailedException(message, exception);
        }
    }

    /**
     * Die Methode extrahiert aus der CSV-Datei im Parameter, die entsprechenden Entitäten
     * und persistiert diese in der Datenbank.
     *
     * @param csvImportFile mit den Informationen bezüglich {@link SobonOrientierungswertSozialeInfrastrukturCsv}.
     * @throws FileImportFailedException  tritt auf, falls ein Fehler beim Import passiert.
     * @throws CsvAttributeErrorException tritt auf, falls ein Attribut in der CSV nicht gesetzt oder fehlerhaft ist.
     */
    public void importSobonOrientierungswerteSozialeInfrastruktur(final InputStream csvImportFile)
        throws FileImportFailedException, CsvAttributeErrorException {
        try (final InputStreamReader csvInputStreamReader = new InputStreamReader(csvImportFile)) {
            final var entities =
                this.csvRepository.readAllSobonOrientierungswertSozialeInfrastrukturCsv(csvInputStreamReader)
                    .stream()
                    .map(this.stammdatenDomainMapper::csv2Entity)
                    .toList();
            this.sobonOrientierungswertSozialeInfrastrukturRepository.saveAll(entities);
        } catch (final CsvDataTypeMismatchException | CsvRequiredFieldEmptyException exception) {
            log.error(exception.getMessage());
            throw new CsvAttributeErrorException(exception.getMessage(), exception);
        } catch (final IOException | DataAccessException exception) {
            final var message =
                "Der Import einer CSV-Datei für SoBoNOrientierungswerteSozialeInfrastruktur ist fehlgeschlagen.";
            log.error(message);
            log.error(exception.getMessage());
            throw new FileImportFailedException(message, exception);
        }
    }
}
