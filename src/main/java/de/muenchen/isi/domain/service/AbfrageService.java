package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.model.AbfrageResponseModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageResponseModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.InfrastrukturabfrageRequestModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbfrageService {

    private final AbfrageDomainMapper abfrageDomainMapper;

    private final InfrastrukturabfrageRepository infrastrukturabfrageRepository;

    private final DokumentService dokumentService;

    /**
     * Die Methode gibt alle {@link InfrastrukturabfrageResponseModel} als Liste zurück.
     *
     * @return Liste an {@link InfrastrukturabfrageResponseModel}.
     */
    public List<InfrastrukturabfrageResponseModel> getInfrastrukturabfragen() {
        return this.infrastrukturabfrageRepository.findAllByOrderByAbfrageFristStellungnahmeDesc()
                .map(this.abfrageDomainMapper::entity2Model)
                .collect(Collectors.toList());
    }

    /**
     * Die Methode gibt ein {@link InfrastrukturabfrageResponseModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link InfrastrukturabfrageResponseModel}.
     * @return {@link InfrastrukturabfrageResponseModel}.
     * @throws EntityNotFoundException falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageResponseModel#getId()} nicht gefunden wird.
     */
    public InfrastrukturabfrageResponseModel getInfrastrukturabfrageById(final UUID id) throws EntityNotFoundException {
        final var optEntity = this.infrastrukturabfrageRepository.findById(id);
        final var entity = optEntity.orElseThrow(() -> {
            final var message = "Infrastrukturabfrage nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.abfrageDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode speichert ein {@link InfrastrukturabfrageResponseModel}.
     *
     * @param abfrage zum Speichern
     * @return das gespeicherte {@link InfrastrukturabfrageResponseModel}
     * @throws UniqueViolationException   falls der Name der Abfrage oder der Abfragevariante bereits vorhanden ist
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public InfrastrukturabfrageResponseModel saveInfrastrukturabfrage(final InfrastrukturabfrageResponseModel abfrage)
            throws UniqueViolationException, OptimisticLockingException {
        if (abfrage.getId() == null) {
            abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        }
        var abfrageEntity = this.abfrageDomainMapper.model2entity(abfrage);
        final var saved =
                this.infrastrukturabfrageRepository.findByAbfrage_NameAbfrageIgnoreCase(
                        abfrageEntity.getAbfrage().getNameAbfrage()
                );
        if ((saved.isPresent() && saved.get().getId().equals(abfrageEntity.getId())) || saved.isEmpty()) {
            try {
                abfrageEntity = this.infrastrukturabfrageRepository.saveAndFlush(abfrageEntity);
            } catch (final ObjectOptimisticLockingFailureException exception) {
                final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
                throw new OptimisticLockingException(message, exception);
            } catch (final DataIntegrityViolationException exception) {
                final var message =
                        "Der angegebene Name der Abfragevariante ist schon vorhanden, bitte wählen Sie daher einen anderen Namen und speichern Sie die Abfrage erneut.";
                throw new UniqueViolationException(message);
            }
            return this.abfrageDomainMapper.entity2Model(abfrageEntity);
        } else {
            throw new UniqueViolationException(
                    "Der angegebene Name der Abfrage ist schon vorhanden, bitte wählen Sie daher einen anderen Namen und speichern Sie die Abfrage erneut."
            );
        }
    }

    /**
     * Diese Methode updated ein {@link InfrastrukturabfrageResponseModel}. Diese muss sich im Status {@link StatusAbfrage#ANGELEGT} befinden.
     *
     * @param abfrage zum Updaten
     * @return das geupdatete {@link InfrastrukturabfrageResponseModel}
     * @throws EntityNotFoundException           falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageResponseModel#getId()} nicht gefunden wird
     * @throws UniqueViolationException          falls der Name der Abfrage {@link InfrastrukturabfrageResponseModel#getAbfrage().getNameAbfrage} ()} bereits vorhanden ist
     * @throws OptimisticLockingException        falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     * @throws AbfrageStatusNotAllowedException  falls sich die Abfrage nicht in einem zulässigen Status befindet
     * @throws FileHandlingFailedException
     * @throws FileHandlingWithS3FailedException
     */
    public InfrastrukturabfrageResponseModel patchAbfrageAngelegt(final InfrastrukturabfrageRequestModel abfrage)
            throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        final var originalAbfrageDb = this.getInfrastrukturabfrageById(abfrage.getId());
        this.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                originalAbfrageDb.getAbfrage(),
                StatusAbfrage.ANGELEGT
        );
        dokumentService.deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
                abfrage.getAbfrage().getDokumente(),
                originalAbfrageDb.getAbfrage().getDokumente()
        );
        abfrage.getAbfrage().setStatusAbfrage(originalAbfrageDb.getAbfrage().getStatusAbfrage());
        final var abfrageToSave = this.abfrageDomainMapper.request2Reponse(abfrage, originalAbfrageDb);
        return this.saveInfrastrukturabfrage(abfrageToSave);
    }

    /**
     * Diese Methode updated ein {@link InfrastrukturabfrageResponseModel}. Diese muss sich im Status {@link StatusAbfrage#ANGELEGT} befinden.
     *
     * @param abfrage zum Updaten
     * @return das geupdatete {@link InfrastrukturabfrageResponseModel}
     * @throws EntityNotFoundException           falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageResponseModel#getId()} nicht gefunden wird
     * @throws UniqueViolationException          falls der Name der Abfrage {@link InfrastrukturabfrageResponseModel#getAbfrage().getNameAbfrage} ()} bereits vorhanden ist
     * @throws OptimisticLockingException        falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     * @throws AbfrageStatusNotAllowedException  falls sich die Abfrage nicht in einem zulässigen Status befindet
     * @throws FileHandlingFailedException
     * @throws FileHandlingWithS3FailedException
     */
    public InfrastrukturabfrageResponseModel patchAbfragevarianteSetRelevant(final UUID abfrageId, final UUID abfragevarianteId)
            throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        final var abfrage = this.getInfrastrukturabfrageById(abfrageId);
        this.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                abfrage.getAbfrage(),
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG
        );
        dokumentService.deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
                abfrage.getAbfrage().getDokumente(),
                abfrage.getAbfrage().getDokumente()
        );

        final var abfragevariante = abfrage.getAbfragevarianten()
                .stream()
                .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevarianteId))
                .findFirst()
                .orElseThrow(() -> {
                    final var message = "Abfragevariante wurde nicht gefunden";
                    log.error(message);
                    return new EntityNotFoundException(message);
                });
        abfragevariante.setRelevant(true);
        abfrage.getAbfrage().setStatusAbfrage(abfrage.getAbfrage().getStatusAbfrage());
        return this.saveInfrastrukturabfrage(abfrage);
    }

    /**
     * Diese Methode updated ein {@link InfrastrukturabfrageResponseModel}. Diese muss sich im Status {@link StatusAbfrage#ANGELEGT} befinden.
     *
     * @param abfrage zum Updaten
     * @return das geupdatete {@link InfrastrukturabfrageResponseModel}
     * @throws EntityNotFoundException           falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageResponseModel#getId()} nicht gefunden wird
     * @throws UniqueViolationException          falls der Name der Abfrage {@link InfrastrukturabfrageResponseModel#getAbfrage().getNameAbfrage} ()} bereits vorhanden ist
     * @throws OptimisticLockingException        falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     * @throws AbfrageStatusNotAllowedException  falls sich die Abfrage nicht in einem zulässigen Status befindet
     * @throws FileHandlingFailedException
     * @throws FileHandlingWithS3FailedException
     */
    public InfrastrukturabfrageResponseModel patchAbfragevarianteUnsetRelevant(final UUID abfrageId, final UUID abfragevarianteId)
            throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        final var abfrage = this.getInfrastrukturabfrageById(abfrageId);
        this.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                abfrage.getAbfrage(),
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG
        );
        dokumentService.deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
                abfrage.getAbfrage().getDokumente(),
                abfrage.getAbfrage().getDokumente()
        );

        final var abfragevariante = abfrage.getAbfragevarianten()
                .stream()
                .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevarianteId))
                .findFirst()
                .orElseThrow(() -> {
                    final var message = "Abfragevariante wurde nicht gefunden";
                    log.error(message);
                    return new EntityNotFoundException(message);
                });
        abfragevariante.setRelevant(false);
        abfrage.getAbfrage().setStatusAbfrage(abfrage.getAbfrage().getStatusAbfrage());
        return this.saveInfrastrukturabfrage(abfrage);
    }

    /**
     * Diese Methode führt für eine Abfrage, die durch die {@link InfrastrukturabfrageResponseModel#getId()} identifiziert ist, eine Statusänderung durch.
     *
     * @param id            {@link InfrastrukturabfrageResponseModel#getId()} der Abfrage zum Updaten
     * @param statusAbfrage neuer {@link StatusAbfrage}
     * @return das geupdatete {@link InfrastrukturabfrageResponseModel}
     * @throws EntityNotFoundException    falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageResponseModel#getId()} nicht gefunden wird
     * @throws UniqueViolationException   falls der Name der Abfrage {@link InfrastrukturabfrageResponseModel#getAbfrage().getNameAbfrage} ()} bereits vorhanden ist
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public InfrastrukturabfrageResponseModel changeStatusAbfrage(final UUID id, final StatusAbfrage statusAbfrage)
            throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException {
        final var originalAbfrageDb = this.getInfrastrukturabfrageById(id);
        originalAbfrageDb.getAbfrage().setStatusAbfrage(statusAbfrage);
        return this.saveInfrastrukturabfrage(originalAbfrageDb);
    }

    /**
     * Diese Methode löscht ein {@link InfrastrukturabfrageResponseModel}.
     *
     * @param id zum Identifizieren des {@link InfrastrukturabfrageResponseModel}.
     * @throws EntityNotFoundException     falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageResponseModel#getId()} nicht gefunden wird.
     * @throws EntityIsReferencedException falls ein {@link BauvorhabenModel} in der Abfrage referenziert wird.
     */
    public void deleteInfrasturkturabfrageById(final UUID id)
            throws EntityNotFoundException, EntityIsReferencedException {
        final var abfrage = this.getInfrastrukturabfrageById(id);
        this.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(abfrage.getAbfrage());
        this.infrastrukturabfrageRepository.deleteById(id);
    }

    /**
     * Enthält das im Parameter gegebene {@link AbfrageResponseModel} ein {@link BauvorhabenModel},
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param abfrage zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link AbfrageResponseModel} ein {@link BauvorhabenModel} referenziert.
     */
    protected void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(final AbfrageResponseModel abfrage)
            throws EntityIsReferencedException {
        final var bauvorhaben = abfrage.getBauvorhaben();
        if (ObjectUtils.isNotEmpty(bauvorhaben)) {
            final var message =
                    "Die Abfrage " +
                            abfrage.getNameAbfrage() +
                            " referenziert das Bauvorhaben " +
                            bauvorhaben.getNameVorhaben() +
                            ".";
            log.error(message);
            throw new EntityIsReferencedException(message);
        }
    }

    /**
     * Enthält das im Parameter gegebene {@link AbfrageResponseModel} einen ungültigen Status {@link StatusAbfrage},
     * wird eine {@link AbfrageStatusNotAllowedException} geworfen.
     *
     * @param abfrage       zum Prüfen.
     * @param statusAbfrage gültiger Status.
     * @throws AbfrageStatusNotAllowedException falls das {@link AbfrageResponseModel} einen unzulässigen Status hat
     */
    protected void throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
            final AbfrageResponseModel abfrage,
            final StatusAbfrage statusAbfrage
    ) throws AbfrageStatusNotAllowedException {
        if (abfrage.getStatusAbfrage() != statusAbfrage) {
            final var message =
                    "Die Abfrage " +
                            abfrage.getNameAbfrage() +
                            " ist im Status " +
                            abfrage.getStatusAbfrage().toString() +
                            ". Der gültige Status wäre " +
                            statusAbfrage.toString() +
                            ".";
            log.error(message);
            throw new AbfrageStatusNotAllowedException(message);
        }
    }
}
