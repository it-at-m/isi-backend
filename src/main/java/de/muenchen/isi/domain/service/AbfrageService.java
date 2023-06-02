package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.BauvorhabenNotReferencedException;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageerstellungInfrastrukturabfrageAngelegtModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbfrageService {

    private final AbfrageDomainMapper abfrageDomainMapper;

    private final InfrastrukturabfrageRepository infrastrukturabfrageRepository;

    private final DokumentService dokumentService;

    /**
     * Die Methode gibt alle {@link InfrastrukturabfrageModel} als Liste zurück.
     *
     * @return Liste an {@link InfrastrukturabfrageModel}.
     */
    public List<InfrastrukturabfrageModel> getInfrastrukturabfragen() {
        return this.infrastrukturabfrageRepository.findAllByOrderByAbfrageFristStellungnahmeDesc()
            .map(this.abfrageDomainMapper::entity2Model)
            .collect(Collectors.toList());
    }

    /**
     * Die Methode gibt ein {@link InfrastrukturabfrageModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link InfrastrukturabfrageModel}.
     * @return {@link InfrastrukturabfrageModel}.
     * @throws EntityNotFoundException falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageModel#getId()} nicht gefunden wird.
     */
    public InfrastrukturabfrageModel getInfrastrukturabfrageById(final UUID id) throws EntityNotFoundException {
        final var optEntity = this.infrastrukturabfrageRepository.findById(id);
        final var entity = optEntity.orElseThrow(() -> {
            final var message = "Infrastrukturabfrage nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.abfrageDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode speichert ein {@link InfrastrukturabfrageModel}.
     *
     * @param abfrage zum Speichern
     * @return das gespeicherte {@link InfrastrukturabfrageModel}
     * @throws UniqueViolationException   falls der Name der Abfrage oder der Abfragevariante bereits vorhanden ist
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public InfrastrukturabfrageModel saveInfrastrukturabfrage(final InfrastrukturabfrageModel abfrage)
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
     * Diese Methode updated ein {@link InfrastrukturabfrageModel}. Diese muss sich im Status {@link StatusAbfrage#ANGELEGT} befinden.
     *
     * @param abfrage zum Updaten
     * @return das geupdatete {@link InfrastrukturabfrageModel}
     * @throws EntityNotFoundException           falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageModel#getId()} nicht gefunden wird
     * @throws UniqueViolationException          falls der Name der Abfrage {@link InfrastrukturabfrageModel#getAbfrage().getNameAbfrage} ()} bereits vorhanden ist
     * @throws OptimisticLockingException        falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     * @throws AbfrageStatusNotAllowedException  falls sich die Abfrage nicht in einem zulässigen Status befindet
     * @throws FileHandlingFailedException
     * @throws FileHandlingWithS3FailedException
     */
    @Transactional
    public InfrastrukturabfrageModel patchAbfrageAngelegt(
        @NotNull final AbfrageerstellungInfrastrukturabfrageAngelegtModel abfrage,
        final UUID id
    )
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        final var originalAbfrageDb = this.getInfrastrukturabfrageById(id);
        this.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                originalAbfrageDb.getAbfrage(),
                StatusAbfrage.ANGELEGT
            );
        dokumentService.deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
            abfrage.getAbfrage().getDokumente(),
            originalAbfrageDb.getAbfrage().getDokumente()
        );
        final var abfrageToSave = this.abfrageDomainMapper.request2Model(abfrage, originalAbfrageDb);
        return this.saveInfrastrukturabfrage(abfrageToSave);
    }

    /**
     * Diese Methode setzt eine {@link AbfragevarianteModel} auf Relevant. Diese muss sich im Status {@link StatusAbfrage#IN_BEARBEITUNG_SACHBEARBEITUNG} befinden.
     *
     * @param abfrageId         die Abfrage zum updaten
     * @param abfragevarianteId die Abfragevariante welche man Relevant setzten möchte
     * @return das geupdatete {@link InfrastrukturabfrageModel}
     * @throws EntityNotFoundException           falls die Abfrage oder Abfragevariante nicht gefunden wurde
     * @throws UniqueViolationException          falls es schon eine Abfragevariante Relevant ist
     * @throws OptimisticLockingException        falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     * @throws AbfrageStatusNotAllowedException  fall die Abfrage den falschen Status hat
     * @throws BauvorhabenNotReferencedException falls die Abfrage keinem Bauvorhaben dazugehört
     */
    public InfrastrukturabfrageModel setAbfragevarianteRelevant(final UUID abfrageId, final UUID abfragevarianteId)
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, BauvorhabenNotReferencedException {
        final var abfrage = this.getInfrastrukturabfrageById(abfrageId);
        this.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                abfrage.getAbfrage(),
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG
            );
        this.throwsBauvorhabenNotReferencedExceptionOrUniqueViolationExceptionIfRelevant(abfrage);
        final var abfragevariante = abfrage
            .getAbfragevarianten()
            .stream()
            .filter(abfragevarianteModel -> abfragevarianteModel.getId().equals(abfragevarianteId))
            .findFirst()
            .orElseThrow(() -> {
                final var message = "Abfragevariante wurde nicht gefunden";
                log.error(message);
                return new EntityNotFoundException(message);
            });
        abfragevariante.setRelevant(!abfragevariante.isRelevant());
        return this.saveInfrastrukturabfrage(abfrage);
    }

    /**
     * Diese Methode führt für eine Abfrage, die durch die {@link InfrastrukturabfrageModel#getId()} identifiziert ist, eine Statusänderung durch.
     *
     * @param id            {@link InfrastrukturabfrageModel#getId()} der Abfrage zum Updaten
     * @param statusAbfrage neuer {@link StatusAbfrage}
     * @return das geupdatete {@link InfrastrukturabfrageModel}
     * @throws EntityNotFoundException    falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageModel#getId()} nicht gefunden wird
     * @throws UniqueViolationException   falls der Name der Abfrage {@link InfrastrukturabfrageModel#getAbfrage().getNameAbfrage} ()} bereits vorhanden ist
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     */
    public InfrastrukturabfrageModel changeStatusAbfrage(final UUID id, final StatusAbfrage statusAbfrage)
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException {
        final var originalAbfrageDb = this.getInfrastrukturabfrageById(id);
        originalAbfrageDb.getAbfrage().setStatusAbfrage(statusAbfrage);
        return this.saveInfrastrukturabfrage(originalAbfrageDb);
    }

    /**
     * Diese Methode löscht ein {@link InfrastrukturabfrageModel}.
     *
     * @param id zum Identifizieren des {@link InfrastrukturabfrageModel}.
     * @throws EntityNotFoundException     falls die Abfrage identifiziert durch die {@link InfrastrukturabfrageModel#getId()} nicht gefunden wird.
     * @throws EntityIsReferencedException falls ein {@link BauvorhabenModel} in der Abfrage referenziert wird.
     */
    public void deleteInfrasturkturabfrageById(final UUID id)
        throws EntityNotFoundException, EntityIsReferencedException {
        final var abfrage = this.getInfrastrukturabfrageById(id);
        this.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(abfrage.getAbfrage());
        this.infrastrukturabfrageRepository.deleteById(id);
    }

    /**
     * Enthält das im Parameter gegebene {@link AbfrageModel} ein {@link BauvorhabenModel},
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param abfrage zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link AbfrageModel} ein {@link BauvorhabenModel} referenziert.
     */
    protected void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(final AbfrageModel abfrage)
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
     * Enthält das im Parameter gegebene {@link AbfrageModel} einen ungültigen Status {@link StatusAbfrage},
     * wird eine {@link AbfrageStatusNotAllowedException} geworfen.
     *
     * @param abfrage       zum Prüfen.
     * @param statusAbfrage gültiger Status.
     * @throws AbfrageStatusNotAllowedException falls das {@link AbfrageModel} einen unzulässigen Status hat
     */
    protected void throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
        final AbfrageModel abfrage,
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

    /**
     * Überprüft ob das Bauvorhaben wo die Abfrage zugeordnet ist keine andere Abfrage hat welche eine Relevante Abfragevariante hat.
     *
     * @param abfrage wo man eine Relevante Abfragevariante setzten möchte
     * @throws UniqueViolationException          falls schon eine Relevante Abfragevariante exisitiert
     * @throws BauvorhabenNotReferencedException falls die Abfrage keinem Bauvorhaben zugeordnet ist
     */

    private void throwsBauvorhabenNotReferencedExceptionOrUniqueViolationExceptionIfRelevant(
        final InfrastrukturabfrageModel abfrage
    ) throws UniqueViolationException, BauvorhabenNotReferencedException {
        if (abfrage.getAbfrage().getBauvorhaben() == null) {
            String message =
                "Die Abfrage ist keinem Bauvorhaben zugeordnet. Somit kann keine Abfragevariante als Relevant markiert werden.";
            log.error(message);
            throw new BauvorhabenNotReferencedException(message);
        }
        AtomicBoolean relevanteAbfrage = new AtomicBoolean(false);
        AtomicReference<String> abfrageName = new AtomicReference<>("");
        AtomicReference<String> abfragevarianteName = new AtomicReference<>("");
        this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenId(abfrage.getAbfrage().getBauvorhaben().getId())
            .forEach(infrastrukturabfrage -> {
                infrastrukturabfrage
                    .getAbfragevarianten()
                    .forEach(abfragevariante -> {
                        if (abfragevariante.isRelevant()) {
                            relevanteAbfrage.set(true);
                            abfrageName.set(infrastrukturabfrage.getAbfrage().getNameAbfrage());
                            abfragevarianteName.set(abfragevariante.getAbfragevariantenName());
                        }
                    });
            });

        if (relevanteAbfrage.get()) {
            var errorMessage =
                "Es gibt schon eine Relevante Abfragevariante bei der Abfrage: " +
                abfrageName +
                " - Abfragevariante: " +
                abfragevarianteName;
            log.error(errorMessage);
            throw new UniqueViolationException(errorMessage);
        }
    }
}
