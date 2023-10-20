package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.BauvorhabenNotReferencedException;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.BauvorhabenDomainMapper;
import de.muenchen.isi.domain.mapper.SearchDomainMapper;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.search.response.AbfrageSearchResultModel;
import de.muenchen.isi.domain.model.search.response.InfrastruktureinrichtungSearchResultModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.common.GlobalCounter;
import de.muenchen.isi.infrastructure.entity.common.Stadtbezirk;
import de.muenchen.isi.infrastructure.entity.common.Verortung;
import de.muenchen.isi.infrastructure.entity.enums.CounterType;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import de.muenchen.isi.infrastructure.repository.AbfragevarianteRepository;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.infrastructure.repository.InfrastruktureinrichtungRepository;
import de.muenchen.isi.infrastructure.repository.common.GlobalCounterRepository;
import de.muenchen.isi.infrastructure.repository.common.KommentarRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BauvorhabenService {

    private final BauvorhabenDomainMapper bauvorhabenDomainMapper;

    private final SearchDomainMapper searchDomainMapper;

    private final BauvorhabenRepository bauvorhabenRepository;

    private final AbfrageRepository abfrageRepository;

    private final AbfragevarianteRepository abfragevarianteRepository;

    private final InfrastruktureinrichtungRepository infrastruktureinrichtungRepository;

    private final GlobalCounterRepository globalCounterRepository;

    private final AbfrageService abfrageService;

    private final DokumentService dokumentService;

    private final KommentarRepository kommentarRepository;

    /**
     * Die Methode gibt ein {@link BauvorhabenModel} identifiziert durch die ID zurück.
     *
     * @param id zum Identifizieren des {@link BauvorhabenModel}.
     * @return {@link BauvorhabenModel}.
     */
    public BauvorhabenModel getBauvorhabenById(final UUID id) throws EntityNotFoundException {
        final var optEntity = this.bauvorhabenRepository.findById(id);
        final var entity = optEntity.orElseThrow(() -> {
            final var message = "Bauvorhaben nicht gefunden";
            log.error(message);
            return new EntityNotFoundException(message);
        });
        return this.bauvorhabenDomainMapper.entity2Model(entity);
    }

    /**
     * Diese Methode speichert ein {@link BauvorhabenModel}.
     *
     * @param bauvorhaben zum Speichern
     * @param abfrageId ID der Abfrage bei einer Datenübernahme
     * @return das gespeicherte {@link BauvorhabenModel}
     * @throws UniqueViolationException   falls der Name des Bauvorhabens {@link BauvorhabenModel#getNameVorhaben()} bereits vorhanden ist
     * @throws OptimisticLockingException falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     * @throws EntityNotFoundException falls bei der Datenübernahme die ausgewählte Abfrage nicht mehr vorhanden ist
     * @throws EntityIsReferencedException falls bei der Datenübernahme die ausgewählte Abfrage bereits ein Bauvorhaben referenziert
     */
    public BauvorhabenModel saveBauvorhaben(final BauvorhabenModel bauvorhaben, final UUID abfrageId)
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, EntityIsReferencedException {
        var bauvorhabenEntity = this.bauvorhabenDomainMapper.model2Entity(bauvorhaben);
        final var saved = this.bauvorhabenRepository.findByNameVorhabenIgnoreCase(bauvorhabenEntity.getNameVorhaben());
        if ((saved.isPresent() && saved.get().getId().equals(bauvorhabenEntity.getId())) || saved.isEmpty()) {
            try {
                if (StringUtils.isEmpty(bauvorhaben.getBauvorhabenNummer())) {
                    bauvorhabenEntity.setBauvorhabenNummer(
                        this.buildBauvorhabennummer(bauvorhabenEntity.getVerortung())
                    );
                }
                bauvorhabenEntity = this.bauvorhabenRepository.saveAndFlush(bauvorhabenEntity);
                // falls bei Neuanlage eines Bauvorhabens eine Datenübernahme mit einer Abfrage durchgeführt wurde, dann wird diese mit dem Bauvorhaben verknüpft
                if (bauvorhaben.getId() == null && abfrageId != null) {
                    final var abfrageModel = this.abfrageService.getById(abfrageId);
                    this.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(
                            abfrageModel,
                            bauvorhabenEntity.getNameVorhaben()
                        );
                    abfrageModel.setBauvorhaben(bauvorhabenEntity.getId());
                    abfrageService.save(abfrageModel);
                }
            } catch (final ObjectOptimisticLockingFailureException exception) {
                final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
                throw new OptimisticLockingException(message, exception);
            }
            return this.bauvorhabenDomainMapper.entity2Model(bauvorhabenEntity);
        } else {
            throw new UniqueViolationException(
                "Der angegebene Name des Bauvorhabens ist schon vorhanden, bitte wählen Sie daher einen anderen Namen und speichern Sie die Abfrage erneut."
            );
        }
    }

    /**
     * Diese Methode updated ein {@link BauvorhabenModel}.
     *
     * @param bauvorhaben zum Updaten
     * @return das geupdatete {@link BauvorhabenModel}
     * @throws EntityNotFoundException           falls das Bauvorhaben identifiziert durch die {@link BauvorhabenModel#getId()} nicht gefunden wird
     * @throws UniqueViolationException          falls der Name des Bauvorhabens {@link BauvorhabenModel#getNameVorhaben()} bereits vorhanden ist
     * @throws OptimisticLockingException        falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     * @throws FileHandlingFailedException
     * @throws FileHandlingWithS3FailedException
     * @throws EntityIsReferencedException       falls bei Neuanlage eines Bauvorhabens bei Datenübernahme einer Abfrage diese bereits ein Bauvorhaben referenziert
     */
    public BauvorhabenModel updateBauvorhaben(final BauvorhabenModel bauvorhaben)
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, FileHandlingFailedException, FileHandlingWithS3FailedException, EntityIsReferencedException {
        final var originalBauvorhabenDb = this.getBauvorhabenById(bauvorhaben.getId());
        dokumentService.deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
            bauvorhaben.getDokumente(),
            originalBauvorhabenDb.getDokumente()
        );
        return this.saveBauvorhaben(bauvorhaben, null);
    }

    /**
     * Diese Methode löscht ein {@link BauvorhabenModel}.
     *
     * @param id zum Identifizieren des {@link BauvorhabenModel}.
     * @throws EntityNotFoundException     falls das Bauvorhaben identifiziert durch die {@link BauvorhabenModel#getId()} nicht gefunden wird.
     * @throws EntityIsReferencedException falls das {@link BauvorhabenModel} in einer Abfrage referenziert wird.
     */
    public void deleteBauvorhaben(final UUID id) throws EntityNotFoundException, EntityIsReferencedException {
        final var bauvorhaben = this.getBauvorhabenById(id);
        this.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(bauvorhaben);
        this.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(bauvorhaben);
        this.kommentarRepository.deleteAllByBauvorhabenId(id);
        this.bauvorhabenRepository.deleteById(id);
    }

    /**
     * Diese Methode setzt in einem {@link BauvorhabenModel} eine neue relevante Abfragevariante.
     * Ist diese Abfragevariante bereits relevant, wird die relevante Abfragevariante des Bauvorhabens auf null gesetzt.
     * Ist eine andere Abfragevariante bereits relevant, wird eine Exception geworfen.
     * Die Abfrage muss sich im Status {@link StatusAbfrage#IN_BEARBEITUNG_SACHBEARBEITUNG} befinden.
     *
     * @param abfragevarianteId als ID der neuen relevanten Abfragevariante
     * @return das aktualisierte {@link BauvorhabenModel}
     * @throws EntityNotFoundException           falls die Abfrage oder Abfragevariante nicht gefunden wurde
     * @throws UniqueViolationException          falls schon eine andere Abfragevariante relevant ist
     * @throws OptimisticLockingException        falls in der Anwendung bereits eine neuere Version der Entität gespeichert ist
     * @throws AbfrageStatusNotAllowedException  falls die Abfrage den falschen Status hat
     * @throws BauvorhabenNotReferencedException falls die Abfrage zu keinem Bauvorhaben gehört
     */
    public BauvorhabenModel changeRelevanteAbfragevariante(final UUID abfragevarianteId)
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, BauvorhabenNotReferencedException, EntityIsReferencedException {
        final AbfrageModel abfrage = abfrageService.getByAbfragevarianteId(abfragevarianteId);
        abfrageService.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
            abfrage,
            StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG
        );

        final var bauvorhabenId = abfrage.getBauvorhaben();
        if (bauvorhabenId == null) {
            String message =
                "Die Abfrage ist keinem Bauvorhaben zugeordnet. Somit kann keine Abfragevariante als relevant markiert werden.";
            log.error(message);
            throw new BauvorhabenNotReferencedException(message);
        }

        final var bauvorhaben = this.getBauvorhabenById(bauvorhabenId);
        final var relevanteAbfragevarianteId = bauvorhaben.getRelevanteAbfragevariante();
        if (relevanteAbfragevarianteId != null) {
            if (!relevanteAbfragevarianteId.equals(abfragevarianteId)) {
                final var relevanteAbfragevariante = abfragevarianteRepository
                    .findById(relevanteAbfragevarianteId)
                    .orElseThrow(() -> {
                        final var message = "Abfragevariante nicht gefunden.";
                        log.error(message);
                        return new EntityNotFoundException(message);
                    });
                var errorMessage =
                    "Die Abfragevariante " +
                    relevanteAbfragevariante.getName() +
                    " in Abfrage " +
                    abfrage.getName() +
                    " ist bereits als relevant markiert.";
                log.error(errorMessage);
                throw new UniqueViolationException(errorMessage);
            } else {
                bauvorhaben.setRelevanteAbfragevariante(null);
            }
        } else {
            bauvorhaben.setRelevanteAbfragevariante(abfragevarianteId);
        }

        return this.saveBauvorhaben(bauvorhaben, null);
    }

    /**
     * Die Methode gibt alle {@link InfrastruktureinrichtungSearchResultModel} als Liste zurück sortiert nach InfrastrukturTyp und innerhalb
     * des InfrastrukturTyps alphabetisch aufsteigend welche einem Bauvorhaben zugeordnet sind.
     *
     * @param bauvorhabenId zum Identifizieren des {@link BauvorhabenModel}
     * @return Liste von {@link InfrastruktureinrichtungSearchResultModel} welche einem Bauvorhaben zugeordent sind
     */
    public List<InfrastruktureinrichtungSearchResultModel> getReferencedInfrastruktureinrichtungen(
        final UUID bauvorhabenId
    ) {
        return this.infrastruktureinrichtungRepository.findAllByBauvorhabenId(bauvorhabenId)
            .map(this.searchDomainMapper::entity2SearchResultModel)
            .sorted(
                Comparator
                    .comparing(InfrastruktureinrichtungSearchResultModel::getInfrastruktureinrichtungTyp)
                    .thenComparing(InfrastruktureinrichtungSearchResultModel::getNameEinrichtung)
            )
            .collect(Collectors.toList());
    }

    /**
     * Die Methode gibt alle {@link AbfrageSearchResultModel} als Liste zurück sortiert nach Erstellungsdatum aufsteigend
     * welche einem Bauvorhaben zugeordnet sind.
     *
     * @param bauvorhabenId zum Identifizieren des {@link BauvorhabenModel}
     * @return Liste von {@link AbfrageSearchResultModel} welche einem Bauvorhaben zugeordent sind
     */
    public List<AbfrageSearchResultModel> getReferencedAbfrage(final UUID bauvorhabenId) {
        return this.abfrageRepository.findAllByBauvorhabenIdOrderByCreatedDateTimeDesc(bauvorhabenId)
            .map(this.searchDomainMapper::entity2SearchResultModel)
            .map(AbfrageSearchResultModel.class::cast)
            .collect(Collectors.toList());
    }

    /**
     * Wird das im Parameter gegebene {@link BauvorhabenModel} durch ein {@link AbfrageModel} referenziert,
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param bauvorhaben zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link BauvorhabenModel} durch ein {@link AbfrageModel} referenziert wird.
     */
    protected void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(
        final BauvorhabenModel bauvorhaben
    ) throws EntityIsReferencedException {
        final List<String> nameAbfragen =
            this.abfrageRepository.findAllByBauvorhabenId(bauvorhaben.getId())
                .map(Abfrage::getName)
                .collect(Collectors.toList());
        if (!nameAbfragen.isEmpty()) {
            final var commaSeparatedNames = String.join(", ", nameAbfragen);
            final var message =
                "Das Bauvorhaben " +
                bauvorhaben.getNameVorhaben() +
                " wird durch die Abfragen " +
                commaSeparatedNames +
                " referenziert.";
            log.error(message);
            throw new EntityIsReferencedException(message);
        }
    }

    /**
     * Wird das im Parameter gegebene {@link BauvorhabenModel} durch ein {@link InfrastruktureinrichtungModel} referenziert,
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param bauvorhaben zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link BauvorhabenModel} durch ein {@link InfrastruktureinrichtungModel} referenziert wird.
     */
    protected void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(
        final BauvorhabenModel bauvorhaben
    ) throws EntityIsReferencedException {
        final List<String> namenInfrastruktureinrichtung =
            this.infrastruktureinrichtungRepository.findAllByBauvorhabenId(bauvorhaben.getId())
                .map(Infrastruktureinrichtung::getNameEinrichtung)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(namenInfrastruktureinrichtung)) {
            final var commaSeparatedNames = String.join(", ", namenInfrastruktureinrichtung);
            final var message =
                "Das Bauvorhaben " +
                bauvorhaben.getNameVorhaben() +
                " wird durch die Infrastruktureinrichtungen " +
                commaSeparatedNames +
                " referenziert.";
            log.error(message);
            throw new EntityIsReferencedException(message);
        }
    }

    protected void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(
        final AbfrageModel abfrage,
        final String nameBauvorhaben
    ) throws EntityIsReferencedException {
        final var bauvorhaben = abfrage.getBauvorhaben();
        if (ObjectUtils.isNotEmpty(bauvorhaben)) {
            final var message =
                "Die Abfrage " + abfrage.getName() + " referenziert das Bauvorhaben " + nameBauvorhaben + ".";
            log.error(message);
            throw new EntityIsReferencedException(message);
        }
    }

    /**
     * Leitet aus der Verortung die Bauvorhabennummer ab. Aufbau: {kleinste Stadtbezirksnummer}_{fortlaufende Bauvorhabennummer}
     *
     * @param verortung Verortung des Bauvorhabens
     * @return ermittelte Bauvorhabennummer
     * @throws OptimisticLockingException im Falle eines konkurrierenden Zugriffs auf die globale fortlaufende Bauvorhabennummer
     */
    private String buildBauvorhabennummer(Verortung verortung) throws OptimisticLockingException {
        if (verortung != null) {
            final Optional<String> minStadtbezirkNummer = CollectionUtils
                .emptyIfNull(verortung.getStadtbezirke())
                .stream()
                .map(Stadtbezirk::getNummer)
                .filter(Objects::nonNull)
                .min(String::compareTo);
            if (!minStadtbezirkNummer.isEmpty()) {
                final Optional<GlobalCounter> saved =
                    this.globalCounterRepository.findByCounterType(CounterType.NUMMER_BAUVORHABEN);
                var bauvorhabennummerEntity = saved.isPresent()
                    ? saved.get()
                    : new GlobalCounter(CounterType.NUMMER_BAUVORHABEN, 0);
                bauvorhabennummerEntity.setCounter(bauvorhabennummerEntity.getCounter() + 1);
                try {
                    bauvorhabennummerEntity = this.globalCounterRepository.saveAndFlush(bauvorhabennummerEntity);
                    return String.format(
                        "%s_%s",
                        minStadtbezirkNummer.get(),
                        StringUtils.leftPad(String.valueOf(bauvorhabennummerEntity.getCounter()), 4, "0")
                    );
                } catch (final ObjectOptimisticLockingFailureException exception) {
                    final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
                    throw new OptimisticLockingException(message, exception);
                }
            }
        }
        return null;
    }
}
