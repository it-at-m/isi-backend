package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.ReportingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BaugenehmigungsverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.WeiteresVerfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungErfolgt.AbfrageBedarfsmeldungErfolgtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungErfolgt.BaugenehmigungsverfahrenBedarfsmeldungErfolgtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungErfolgt.BauleitplanverfahrenBedarfsmeldungErfolgtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungErfolgt.WeiteresVerfahrenBedarfsmeldungErfolgtModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.AbfrageInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.BaugenehmigungsverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.BauleitplanverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.WeiteresVerfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfrageInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.WeiteresVerfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerBedarfModel;
import de.muenchen.isi.domain.service.calculation.CalculationService;
import de.muenchen.isi.domain.service.common.BearbeitungshistorieService;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.domain.service.reporting.ReportingdataTransferService;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import de.muenchen.isi.infrastructure.repository.AbfragevarianteBaugenehmigungsverfahrenRepository;
import de.muenchen.isi.infrastructure.repository.AbfragevarianteBauleitplanverfahrenRepository;
import de.muenchen.isi.infrastructure.repository.AbfragevarianteWeiteresVerfahrenRepository;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbfrageService {

    private final AbfrageRepository abfrageRepository;

    private final AbfrageDomainMapper abfrageDomainMapper;

    private final BauvorhabenRepository bauvorhabenRepository;

    private final DokumentService dokumentService;

    private final AuthenticationUtils authenticationUtils;

    private final AbfragevarianteBauleitplanverfahrenRepository abfragevarianteBauleitplanverfahrenRepository;

    private final AbfragevarianteBaugenehmigungsverfahrenRepository abfragevarianteBaugenehmigungsverfahrenRepository;

    private final AbfragevarianteWeiteresVerfahrenRepository abfragevarianteWeiteresVerfahrenRepository;

    private final CalculationService calculationService;

    private final ReportingdataTransferService reportingdataTransferService;

    private final BearbeitungshistorieService bearbeitungshistorieService;

    /**
     * Die Methode gibt ein {@link AbfrageModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link AbfrageModel}.
     * @return {@link AbfrageModel}.
     * @throws EntityNotFoundException     falls die Abfrage identifiziert durch die {@link AbfrageModel#getId()} nicht gefunden wird.
     * @throws UserRoleNotAllowedException falls der User keine Berechtigung für die Abfrage hat.
     */
    public AbfrageModel getById(final UUID id) throws EntityNotFoundException, UserRoleNotAllowedException {
        final var optAbfrage = this.abfrageRepository.findById(id);
        final var abfrage = optAbfrage.orElseThrow(() -> {
            final var message = "Abfrage nicht gefunden.";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        throwUserRoleNotAllowedExceptionWhenRoleIsAnwenderAndAbfragestatusIsNotErledigt(abfrage);
        return this.abfrageDomainMapper.entity2Model(abfrage);
    }

    /**
     * Diese Methode speichert ein {@link AbfrageModel}.
     * Des Weiteren werden je Abfragevariante die planungs- und sobonursächlichen {@link LangfristigerBedarfModel} ermittelt
     * und samt mit der Abfrage an die Reportingschnittstelle übermittelt.
     *
     * @param abfrage zum Speichern
     * @return das gespeicherte {@link AbfrageModel}
     * @throws UniqueViolationException   falls der Name der Abfrage oder der Abfragevariante bereits vorhanden ist
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     * @throws EntityNotFoundException    falls das referenzierte Bauvorhaben nicht existiert.
     * @throws CalculationException       falls bei den Berechnungen ein Fehler auftritt.
     * @throws ReportingException         falls bei der Übermittlung an die Reportingschnittstelle ein Fehler auftritt.
     */
    public AbfrageModel save(final AbfrageModel abfrage)
        throws EntityNotFoundException, OptimisticLockingException, UniqueViolationException, CalculationException, ReportingException {
        if (abfrage.getId() == null) {
            abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
            abfrage.setSub(authenticationUtils.getUserSub());
            bearbeitungshistorieService.appendBearbeitungshistorieToAbfrage(abfrage);
        }
        var entity = this.abfrageDomainMapper.model2Entity(abfrage);
        final var saved = this.abfrageRepository.findByNameIgnoreCase(abfrage.getName());
        if ((saved.isPresent() && saved.get().getId().equals(entity.getId())) || saved.isEmpty()) {
            try {
                entity = this.abfrageRepository.saveAndFlush(entity);
            } catch (final ObjectOptimisticLockingFailureException exception) {
                final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
                throw new OptimisticLockingException(message, exception);
            } catch (final DataIntegrityViolationException exception) {
                final var message =
                    "Der angegebene Name der Abfragevariante ist schon vorhanden, bitte wählen Sie daher einen anderen Namen und speichern Sie die Abfrage erneut.";
                throw new UniqueViolationException(message, exception);
            }
            final var model = this.abfrageDomainMapper.entity2Model(entity);
            // Berechnen der langfristigen planungs- und sobonursächlichen Bedarfe
            final var bedarfeForAbfragevarianten = calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(
                model
            );
            // Übermitteln der Abfrage samt der vorher berechneten Bedarfe an die Reportingschnittstelle
            reportingdataTransferService.transferAbfrageAndBedarfe(model, bedarfeForAbfragevarianten);
            return model;
        } else {
            throw new UniqueViolationException(
                "Der angegebene Name der Abfrage ist schon vorhanden, bitte wählen Sie daher einen anderen Namen und speichern Sie die Abfrage erneut."
            );
        }
    }

    /**
     * Diese Methode aktualisiert ein {@link AbfrageAngelegtModel}.
     * Des Weiteren werden je Abfragevariante die planungs- und sobonursächlichen {@link LangfristigerBedarfModel} ermittelt
     * und samt mit der Abfrage an die Reportingschnittstelle übermittelt.
     *
     * @param abfrage zum Speichern
     * @param id      der Abfrage
     * @return das gespeicherte {@link AbfrageModel}
     * @throws UniqueViolationException          falls der Name der Abfrage oder der Abfragevariante bereits vorhanden ist.
     * @throws OptimisticLockingException        falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist.
     * @throws EntityNotFoundException           falls die Abfrage oder das referenzierte Bauvorhaben nicht existiert.
     * @throws AbfrageStatusNotAllowedException  falls die zu aktualisierende Abfrage sich nicht im Status {@link StatusAbfrage#ANGELEGT} befindet.
     * @throws FileHandlingFailedException       falls es beim Dateihandling zu einem Fehler gekommen ist.
     * @throws FileHandlingWithS3FailedException falls es beim Dateihandling im S3-Storage zu einem Fehler gekommen ist.
     * @throws CalculationException              falls bei den Berechnungen ein Fehler auftritt.
     * @throws ReportingException                falls bei der Übermittlung an die Reportingschnittstelle ein Fehler auftritt.
     * @throws UserRoleNotAllowedException       falls der User keine Berechtigung für die Abfrage hat.
     */
    public AbfrageModel patchAngelegt(final AbfrageAngelegtModel abfrage, final UUID id)
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, FileHandlingFailedException, FileHandlingWithS3FailedException, UserRoleNotAllowedException, CalculationException, ReportingException {
        final var originalAbfrageDb = this.getById(id);
        this.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(originalAbfrageDb, StatusAbfrage.ANGELEGT);
        this.changeRelevantAbfragevarianteOnBauvorhabenChangeAbfrageAngelegtModel(abfrage, originalAbfrageDb);
        final AbfrageModel abfrageToSave;
        if (ArtAbfrage.BAULEITPLANVERFAHREN.equals(abfrage.getArtAbfrage())) {
            abfrageToSave =
                this.patchBauleitplanverfahrenAngelegt(
                        (BauleitplanverfahrenAngelegtModel) abfrage,
                        (BauleitplanverfahrenModel) originalAbfrageDb
                    );
        } else if (ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN.equals(abfrage.getArtAbfrage())) {
            abfrageToSave =
                this.patchBaugenehmigungsverfahrenAngelegt(
                        (BaugenehmigungsverfahrenAngelegtModel) abfrage,
                        (BaugenehmigungsverfahrenModel) originalAbfrageDb
                    );
        } else if (ArtAbfrage.WEITERES_VERFAHREN.equals(abfrage.getArtAbfrage())) {
            abfrageToSave =
                this.patchWeiteresVerfahrenAngelegt(
                        (WeiteresVerfahrenAngelegtModel) abfrage,
                        (WeiteresVerfahrenModel) originalAbfrageDb
                    );
        } else {
            final var message = "Die Art der Abfrage wird nicht unterstützt.";
            log.error(message);
            throw new EntityNotFoundException(message);
        }
        return this.save(abfrageToSave);
    }

    /**
     * Diese Methode aktualisiert ein {@link BauleitplanverfahrenAngelegtModel}.
     *
     * @param abfrage           mit den Attributen zum Speichern
     * @param originalAbfrageDb welche mit den im Parameter gegebenen abfrage gegebenen Werten aktualisiert und gespeichert wird.
     * @return das gespeicherte {@link AbfrageModel}
     * @throws FileHandlingFailedException       falls es beim Dateihandling zu einem Fehler gekommen ist.
     * @throws FileHandlingWithS3FailedException falls es beim Dateihandling im S3-Storage zu einem Fehler gekommen ist.
     */
    protected AbfrageModel patchBauleitplanverfahrenAngelegt(
        BauleitplanverfahrenAngelegtModel abfrage,
        BauleitplanverfahrenModel originalAbfrageDb
    ) throws FileHandlingFailedException, FileHandlingWithS3FailedException {
        dokumentService.deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
            abfrage.getDokumente(),
            originalAbfrageDb.getDokumente()
        );
        return this.abfrageDomainMapper.request2Model(abfrage, originalAbfrageDb);
    }

    /**
     * Diese Methode aktualisiert ein {@link BaugenehmigungsverfahrenAngelegtModel}.
     *
     * @param abfrage           mit den Attributen zum Speichern
     * @param originalAbfrageDb welche mit den im Parameter gegebenen abfrage gegebenen Werten aktualisiert und gespeichert wird.
     * @return das gespeicherte {@link AbfrageModel}
     * @throws FileHandlingFailedException       falls es beim Dateihandling zu einem Fehler gekommen ist.
     * @throws FileHandlingWithS3FailedException falls es beim Dateihandling im S3-Storage zu einem Fehler gekommen ist.
     */
    protected AbfrageModel patchBaugenehmigungsverfahrenAngelegt(
        BaugenehmigungsverfahrenAngelegtModel abfrage,
        BaugenehmigungsverfahrenModel originalAbfrageDb
    ) throws FileHandlingFailedException, FileHandlingWithS3FailedException {
        dokumentService.deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
            abfrage.getDokumente(),
            originalAbfrageDb.getDokumente()
        );
        return this.abfrageDomainMapper.request2Model(abfrage, originalAbfrageDb);
    }

    /**
     * Diese Methode aktualisiert ein {@link WeiteresVerfahrenAngelegtModel}.
     *
     * @param abfrage           mit den Attributen zum Speichern
     * @param originalAbfrageDb welche mit den im Parameter gegebenen abfrage gegebenen Werten aktualisiert und gespeichert wird.
     * @return das gespeicherte {@link AbfrageModel}
     * @throws FileHandlingFailedException       falls es beim Dateihandling zu einem Fehler gekommen ist.
     * @throws FileHandlingWithS3FailedException falls es beim Dateihandling im S3-Storage zu einem Fehler gekommen ist.
     */
    protected AbfrageModel patchWeiteresVerfahrenAngelegt(
        WeiteresVerfahrenAngelegtModel abfrage,
        WeiteresVerfahrenModel originalAbfrageDb
    ) throws FileHandlingFailedException, FileHandlingWithS3FailedException {
        dokumentService.deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
            abfrage.getDokumente(),
            originalAbfrageDb.getDokumente()
        );
        return this.abfrageDomainMapper.request2Model(abfrage, originalAbfrageDb);
    }

    /**
     * Diese Methode aktualisiert ein {@link AbfrageInBearbeitungSachbearbeitungModel}.
     * Des Weiteren werden je Abfragevariante die planungs- und sobonursächlichen {@link LangfristigerBedarfModel} ermittelt
     * und samt mit der Abfrage an die Reportingschnittstelle übermittelt.
     *
     * @param abfrage zum Speichern
     * @param id      der Abfrage
     * @return das gespeicherte {@link AbfrageModel}
     * @throws UniqueViolationException         falls der Name der Abfrage oder der Abfragevariante bereits vorhanden ist.
     * @throws OptimisticLockingException       falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist.
     * @throws EntityNotFoundException          falls das referenzierte Bauvorhaben nicht existiert.
     * @throws AbfrageStatusNotAllowedException falls die zu aktualisierende Abfrage sich nicht im Status {@link StatusAbfrage#IN_BEARBEITUNG_SACHBEARBEITUNG} befindet.
     * @throws CalculationException             falls bei den Berechnungen ein Fehler auftritt.
     * @throws ReportingException               falls bei der Übermittlung an die Reportingschnittstelle ein Fehler auftritt.
     * @throws UserRoleNotAllowedException      falls der User keine Berechtigung für die Abfrage hat.
     */
    public AbfrageModel patchInBearbeitungSachbearbeitung(
        final AbfrageInBearbeitungSachbearbeitungModel abfrage,
        final UUID id
    )
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, UserRoleNotAllowedException, CalculationException, ReportingException {
        final var originalAbfrageDb = this.getById(id);
        this.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                originalAbfrageDb,
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG
            );

        this.changeRelevantAbfragevarianteOnBauvorhabenChangeAbfrageInBearbeitungSachbearbeitung(
                abfrage,
                originalAbfrageDb
            );
        final AbfrageModel abfrageToSave;
        if (ArtAbfrage.BAULEITPLANVERFAHREN.equals(abfrage.getArtAbfrage())) {
            abfrageToSave =
                this.abfrageDomainMapper.request2Model(
                        (BauleitplanverfahrenInBearbeitungSachbearbeitungModel) abfrage,
                        (BauleitplanverfahrenModel) originalAbfrageDb
                    );
        } else if (ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN.equals(abfrage.getArtAbfrage())) {
            abfrageToSave =
                this.abfrageDomainMapper.request2Model(
                        (BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel) abfrage,
                        (BaugenehmigungsverfahrenModel) originalAbfrageDb
                    );
        } else if (ArtAbfrage.WEITERES_VERFAHREN.equals(abfrage.getArtAbfrage())) {
            abfrageToSave =
                this.abfrageDomainMapper.request2Model(
                        (WeiteresVerfahrenInBearbeitungSachbearbeitungModel) abfrage,
                        (WeiteresVerfahrenModel) originalAbfrageDb
                    );
        } else {
            final var message = "Die Art der Abfrage wird nicht unterstützt.";
            log.error(message);
            throw new EntityNotFoundException(message);
        }
        return this.save(abfrageToSave);
    }

    /**
     * Diese Methode aktualisiert ein {@link AbfrageInBearbeitungFachreferatModel}.
     * Des Weiteren werden je Abfragevariante die planungs- und sobonursächlichen {@link LangfristigerBedarfModel} ermittelt
     * und samt mit der Abfrage an die Reportingschnittstelle übermittelt.
     *
     * @param abfrage zum Speichern
     * @param id      der Abfrage
     * @return das gespeicherte {@link AbfrageModel}
     * @throws UniqueViolationException         falls der Name der Abfrage oder der Abfragevariante bereits vorhanden ist.
     * @throws OptimisticLockingException       falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist.
     * @throws EntityNotFoundException          falls das referenzierte Bauvorhaben nicht existiert.
     * @throws AbfrageStatusNotAllowedException falls die zu aktualisierende Abfrage sich nicht im Status {@link StatusAbfrage#IN_BEARBEITUNG_FACHREFERATE} befindet.
     * @throws CalculationException             falls bei den Berechnungen ein Fehler auftritt.
     * @throws ReportingException               falls bei der Übermittlung an die Reportingschnittstelle ein Fehler auftritt.
     * @throws UserRoleNotAllowedException      falls der User keine Berechtigung für die Abfrage hat.
     */
    public AbfrageModel patchInBearbeitungFachreferat(
        final AbfrageInBearbeitungFachreferatModel abfrage,
        final UUID id
    )
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, UserRoleNotAllowedException, CalculationException, ReportingException {
        final var originalAbfrageDb = this.getById(id);
        this.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                originalAbfrageDb,
                StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE
            );

        final AbfrageModel abfrageToSave;
        if (ArtAbfrage.BAULEITPLANVERFAHREN.equals(abfrage.getArtAbfrage())) {
            abfrageToSave =
                this.abfrageDomainMapper.request2Model(
                        (BauleitplanverfahrenInBearbeitungFachreferatModel) abfrage,
                        (BauleitplanverfahrenModel) originalAbfrageDb
                    );
        } else if (ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN.equals(abfrage.getArtAbfrage())) {
            abfrageToSave =
                this.abfrageDomainMapper.request2Model(
                        (BaugenehmigungsverfahrenInBearbeitungFachreferatModel) abfrage,
                        (BaugenehmigungsverfahrenModel) originalAbfrageDb
                    );
        } else if (ArtAbfrage.WEITERES_VERFAHREN.equals(abfrage.getArtAbfrage())) {
            abfrageToSave =
                this.abfrageDomainMapper.request2Model(
                        (WeiteresVerfahrenInBearbeitungFachreferatModel) abfrage,
                        (WeiteresVerfahrenModel) originalAbfrageDb
                    );
        } else {
            final var message = "Die Art der Abfrage wird nicht unterstützt.";
            log.error(message);
            throw new EntityNotFoundException(message);
        }
        return this.save(abfrageToSave);
    }

    /**
     * Diese Methode aktualisiert ein {@link AbfrageBedarfsmeldungErfolgtModel}.
     * Des Weiteren werden je Abfragevariante die planungs- und sobonursächlichen {@link LangfristigerBedarfModel} ermittelt
     * und samt mit der Abfrage an die Reportingschnittstelle übermittelt.
     *
     * @param abfrage zum Speichern
     * @param id      der Abfrage
     * @return das gespeicherte {@link AbfrageModel}
     * @throws UniqueViolationException         falls der Name der Abfrage oder der Abfragevariante bereits vorhanden ist.
     * @throws OptimisticLockingException       falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist.
     * @throws EntityNotFoundException          falls das referenzierte Bauvorhaben nicht existiert.
     * @throws AbfrageStatusNotAllowedException falls die zu aktualisierende Abfrage sich nicht im Status {@link StatusAbfrage#IN_BEARBEITUNG_FACHREFERATE} befindet.
     * @throws CalculationException             falls bei den Berechnungen ein Fehler auftritt.
     * @throws ReportingException               falls bei der Übermittlung an die Reportingschnittstelle ein Fehler auftritt.
     */
    public AbfrageModel patchBedarfsmeldungErfolgt(final AbfrageBedarfsmeldungErfolgtModel abfrage, final UUID id)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, UserRoleNotAllowedException, CalculationException, ReportingException {
        final var originalAbfrageDb = this.getById(id);
        this.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                originalAbfrageDb,
                StatusAbfrage.BEDARFSMELDUNG_ERFOLGT
            );

        final AbfrageModel abfrageToSave;
        if (ArtAbfrage.BAULEITPLANVERFAHREN.equals(abfrage.getArtAbfrage())) {
            abfrageToSave =
                this.abfrageDomainMapper.request2Model(
                        (BauleitplanverfahrenBedarfsmeldungErfolgtModel) abfrage,
                        (BauleitplanverfahrenModel) originalAbfrageDb
                    );
        } else if (ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN.equals(abfrage.getArtAbfrage())) {
            abfrageToSave =
                this.abfrageDomainMapper.request2Model(
                        (BaugenehmigungsverfahrenBedarfsmeldungErfolgtModel) abfrage,
                        (BaugenehmigungsverfahrenModel) originalAbfrageDb
                    );
        } else if (ArtAbfrage.WEITERES_VERFAHREN.equals(abfrage.getArtAbfrage())) {
            abfrageToSave =
                this.abfrageDomainMapper.request2Model(
                        (WeiteresVerfahrenBedarfsmeldungErfolgtModel) abfrage,
                        (WeiteresVerfahrenModel) originalAbfrageDb
                    );
        } else {
            final var message = "Die Art der Abfrage wird nicht unterstützt.";
            log.error(message);
            throw new EntityNotFoundException(message);
        }
        return this.save(abfrageToSave);
    }

    /**
     * Diese Methode löscht ein {@link AbfrageModel} aus der Datenbank und über die Reportingschnittstelle.
     *
     * @param id zum Identifizieren des {@link AbfrageModel}.
     * @throws EntityNotFoundException          falls die Abfrage identifiziert durch die {@link AbfrageModel#getId()} nicht gefunden wird.
     * @throws EntityIsReferencedException      falls ein {@link BauvorhabenModel} in der Abfrage referenziert wird.
     * @throws UserRoleNotAllowedException      falls der Nutzer nicht die richtige Rolle hat.
     * @throws AbfrageStatusNotAllowedException falls die Abfrage den falschen Status hat.
     * @throws ReportingException               falls beim Löschen über die Reportingschnittstelle ein Fehler auftritt.
     * @throws UserRoleNotAllowedException      falls der User keine Berechtigung für die Abfrage hat.
     * @throws AbfrageStatusNotAllowedException falls die Abfrage den falschen Status hat.
     */
    public void deleteById(final UUID id)
        throws EntityNotFoundException, EntityIsReferencedException, UserRoleNotAllowedException, AbfrageStatusNotAllowedException, ReportingException {
        final var abfrage = this.getById(id);
        this.throwUserRoleNotAllowedOrAbfrageStatusNotAllowedExceptionWhenNotTheCorrectUserWithTheCorrectRole(abfrage);
        this.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(abfrage);
        this.abfrageRepository.deleteById(id);
        this.reportingdataTransferService.deleteTransferedAbfrage(abfrage);
    }

    /**
     * Diese Methode überprüft ob der Nutzer die richtige Rolle besitzt und die Abfrage sich im richtigen Status befindet.
     * Dabei wird auch geprüft, ob der die zur Abfrage zugeordneten sub-ID (Subject-ID der Security-Session) mit der sub-ID des Nutzers übereinstimmt.
     *
     * @param abfrage zum Identifizieren des Status.
     * @throws UserRoleNotAllowedException      falls der User keine Berechtigung für die Abfrage hat.
     * @throws AbfrageStatusNotAllowedException falls die Abfrage den falschen Status hat oder der Sub des Nutzers nicht mit dem Sub der Abfrage übereinstimmt.
     */
    protected void throwUserRoleNotAllowedOrAbfrageStatusNotAllowedExceptionWhenNotTheCorrectUserWithTheCorrectRole(
        final AbfrageModel abfrage
    ) throws UserRoleNotAllowedException, AbfrageStatusNotAllowedException {
        var roles = authenticationUtils.getUserRoles();
        if (!roles.contains(AuthenticationUtils.ROLE_ADMIN)) {
            if (!roles.contains(AuthenticationUtils.ROLE_ABFRAGEERSTELLUNG)) {
                throw new UserRoleNotAllowedException("Keine Berechtigung zum Löschen der Abfrage.");
            } else if (!abfrage.getSub().equals(authenticationUtils.getUserSub())) {
                log.error(
                    "User {} hat versucht, die Abfrage {} von User {} zu löschen.",
                    authenticationUtils.getUserSub(),
                    abfrage.getId(),
                    abfrage.getSub()
                );
                throw new UserRoleNotAllowedException(
                    "Keine Berechtigung zum Löschen der Abfrage, da diese durch einen anderen Nutzer angelegt wurde."
                );
            } else if (abfrage.getStatusAbfrage() != StatusAbfrage.ANGELEGT) {
                throw new AbfrageStatusNotAllowedException(
                    "Die Abfrage kann nur im Status 'angelegt' gelöscht werden."
                );
            }
        }
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
        Optional<Bauvorhaben> bauvorhaben = Optional.empty();
        if (ObjectUtils.isNotEmpty(abfrage.getBauvorhaben())) {
            bauvorhaben = bauvorhabenRepository.findById(abfrage.getBauvorhaben());
        }
        if (bauvorhaben.isPresent()) {
            final var message =
                "Die Abfrage " +
                abfrage.getName() +
                " referenziert das Bauvorhaben " +
                bauvorhaben.get().getNameVorhaben() +
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
    public void throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
        final AbfrageModel abfrage,
        final StatusAbfrage statusAbfrage
    ) throws AbfrageStatusNotAllowedException {
        if (abfrage.getStatusAbfrage() != statusAbfrage) {
            final var message =
                "Die Abfrage " +
                abfrage.getName() +
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
     * Ermittelt die Abfrage auf Basis der im Parameter gegebenen ID einer Abfragevariante.
     *
     * @param abfragevarianteId zum ermitteln der Abfrage.
     * @return die gefundene Abfrage.
     * @throws EntityNotFoundException     falls keine Abfrage auf Basis der Abfragevariante ID eindeutig ermittelt werden konnte.
     * @throws UserRoleNotAllowedException falls der User keine Berechtigung für die Abfrage hat.
     */
    public AbfrageModel getByAbfragevarianteId(final UUID abfragevarianteId)
        throws EntityNotFoundException, UserRoleNotAllowedException {
        final var abfrageIds = Stream
            .of(
                abfragevarianteBauleitplanverfahrenRepository.findAbfrageIdForAbfragevarianteById(abfragevarianteId),
                abfragevarianteBauleitplanverfahrenRepository.findAbfrageIdForAbfragevarianteSachbearbeitungById(
                    abfragevarianteId
                ),
                abfragevarianteBaugenehmigungsverfahrenRepository.findAbfrageIdForAbfragevarianteById(
                    abfragevarianteId
                ),
                abfragevarianteBaugenehmigungsverfahrenRepository.findAbfrageIdForAbfragevarianteSachbearbeitungById(
                    abfragevarianteId
                ),
                abfragevarianteWeiteresVerfahrenRepository.findAbfrageIdForAbfragevarianteById(abfragevarianteId),
                abfragevarianteWeiteresVerfahrenRepository.findAbfrageIdForAbfragevarianteSachbearbeitungById(
                    abfragevarianteId
                )
            )
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

        if (abfrageIds.size() != 1) {
            final var message = "Abfrage auf Basis einer Abfragevariante ID nicht eindeutig auffindbar.";
            log.error(message);
            throw new EntityNotFoundException(message);
        }

        return this.getById(abfrageIds.get(0));
    }

    /**
     * Wirft eine {@link UserRoleNotAllowedException} wenn der User die Rolle Anwender hat
     * und der Status der Abfrage nicht {@link  StatusAbfrage#ERLEDIGT_OHNE_FACHREFERAT} oder
     * {@link StatusAbfrage#ERLEDIGT_MIT_FACHREFERAT} ist.
     *
     * @param abfrage die bearbeitet werden soll.
     * @throws UserRoleNotAllowedException falls der User keine Berechtigung für die Abfrage hat.
     */
    public void throwUserRoleNotAllowedExceptionWhenRoleIsAnwenderAndAbfragestatusIsNotErledigt(Abfrage abfrage)
        throws UserRoleNotAllowedException {
        if (authenticationUtils.isOnlyRoleAnwender()) {
            if (
                abfrage.getStatusAbfrage() != StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT &&
                abfrage.getStatusAbfrage() != StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT
            ) {
                throw new UserRoleNotAllowedException("Fehlende Berechtigung für die Abfrage");
            }
        }
    }

    public void changeRelevantAbfragevarianteOnBauvorhabenChangeAbfrageAngelegtModel(
        AbfrageAngelegtModel abfrage,
        AbfrageModel originalAbfrageDb
    ) {
        if (originalAbfrageDb.getBauvorhaben() != null) {
            if (
                abfrage.getBauvorhaben() == null || !abfrage.getBauvorhaben().equals(originalAbfrageDb.getBauvorhaben())
            ) {
                var bauvorhaben = this.bauvorhabenRepository.getReferenceById(originalAbfrageDb.getBauvorhaben());
                bauvorhaben.setRelevanteAbfragevariante(null);
                this.bauvorhabenRepository.save(bauvorhaben);
            }
        }
    }

    public void changeRelevantAbfragevarianteOnBauvorhabenChangeAbfrageInBearbeitungSachbearbeitung(
        AbfrageInBearbeitungSachbearbeitungModel abfrage,
        AbfrageModel originalAbfrageDb
    ) {
        if (originalAbfrageDb.getBauvorhaben() != null) {
            if (
                abfrage.getBauvorhaben() == null || !abfrage.getBauvorhaben().equals(originalAbfrageDb.getBauvorhaben())
            ) {
                var bauvorhaben = this.bauvorhabenRepository.getReferenceById(originalAbfrageDb.getBauvorhaben());
                bauvorhaben.setRelevanteAbfragevariante(null);
                this.bauvorhabenRepository.save(bauvorhaben);
            }
        }
    }
}
