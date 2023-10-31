package de.muenchen.isi.domain.service.stammdaten;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import de.muenchen.isi.domain.exception.CsvAttributeErrorException;
import de.muenchen.isi.domain.exception.FileImportFailedException;
import de.muenchen.isi.domain.mapper.StammdatenDomainMapper;
import de.muenchen.isi.infrastructure.csv.SobonOrientierungswertCsv;
import de.muenchen.isi.infrastructure.csv.StaedtebaulicheOrientierungswertCsv;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonJahr;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonOrientierungswert;
import de.muenchen.isi.infrastructure.entity.stammdaten.StaedtbaulicherOrientierungwert;
import de.muenchen.isi.infrastructure.repository.CsvRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.SobonJahrRepository;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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

    private final SobonJahrRepository sobonJahrRepository;

    private final StammdatenDomainMapper stammdatenDomainMapper;

    /**
     * Die Methode extrahiert aus der CSV-Datei im Parameter, die entsprechenden Entitäten
     * und persistiert diese in der Datenbank.
     *
     * @param csvImportFile mit den Informationen bezüglich {@link StaedtebaulicheOrientierungswertCsv}.
     * @throws FileImportFailedException  tritt auf, falls ein Fehler beim Import passiert.
     * @throws CsvAttributeErrorException tritt auf, falls ein Attribut in der CSV nicht gesetzt oder fehlerhaft ist.
     */
    public void importStaedtebaulicheOrientierungswerte(final UUID sobonJahrId, final MultipartFile csvImportFile)
        throws FileImportFailedException, CsvAttributeErrorException {
        Optional<SobonJahr> sobonJahrDb = this.sobonJahrRepository.findById(sobonJahrId);
        if (sobonJahrDb.isPresent()) {
            var sobonJahr = sobonJahrDb.get();
            try (final InputStreamReader csvInputStreamReader = new InputStreamReader(csvImportFile.getInputStream())) {
                final List<StaedtbaulicherOrientierungwert> entities =
                    this.csvRepository.readAllStaedtebaulicheOrientierungswertCsv(csvInputStreamReader)
                        .stream()
                        .map(this.stammdatenDomainMapper::csv2Entity)
                        .collect(Collectors.toList());
                sobonJahr.setStaedtebaulicheOrientierungswerte(entities);
                this.sobonJahrRepository.save(sobonJahr);
            } catch (final CsvDataTypeMismatchException | CsvRequiredFieldEmptyException exception) {
                log.error(exception.getMessage());
                throw new CsvAttributeErrorException(exception.getMessage(), exception);
            } catch (final IOException | DataAccessException exception) {
                final var message =
                    "Der Import einer CSV-Datei für StaedtebaulicheOrientierungswerte ist fehlgeschlagen.";
                log.error(message);
                log.error(exception.getMessage());
                throw new FileImportFailedException(message, exception);
            }
        }
    }

    /**
     * Die Methode extrahiert aus der CSV-Datei im Parameter, die entsprechenden Entitäten
     * und persistiert diese in der Datenbank.
     *
     * @param csvImportFile mit den Informationen bezüglich {@link SobonOrientierungswertCsv}.
     * @throws FileImportFailedException  tritt auf, falls ein Fehler beim Import passiert.
     * @throws CsvAttributeErrorException tritt auf, falls ein Attribut in der CSV nicht gesetzt oder fehlerhaft ist.
     */
    public void importSobonOrientierungswerteSozialeInfrastruktur(
        final UUID sobonJahrId,
        final MultipartFile csvImportFile
    ) throws FileImportFailedException, CsvAttributeErrorException {
        Optional<SobonJahr> sobonJahrDb = this.sobonJahrRepository.findById(sobonJahrId);
        if (sobonJahrDb.isPresent()) {
            try (final InputStreamReader csvInputStreamReader = new InputStreamReader(csvImportFile.getInputStream())) {
                var sobonJahr = sobonJahrDb.get();
                final List<SobonOrientierungswert> entities =
                    this.csvRepository.readAllSobonOrientierungswertSozialeInfrastrukturCsv(csvInputStreamReader)
                        .stream()
                        .map(this.stammdatenDomainMapper::csv2Entity)
                        .collect(Collectors.toList());
                sobonJahr.setSobonOrientierungswerte(entities);
                this.sobonJahrRepository.save(sobonJahr);
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
}
