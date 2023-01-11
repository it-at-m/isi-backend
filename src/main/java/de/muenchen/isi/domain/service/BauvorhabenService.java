package de.muenchen.isi.domain.service;

import de.muenchen.isi.api.dto.AbfrageDto;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.BauvorhabenDomainMapper;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.GsNachmittagBetreuungRepository;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.KinderkrippeRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.KindergartenRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.HausFuerKinderRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.GrundschuleRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.MittelschuleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BauvorhabenService {

    private final BauvorhabenDomainMapper bauvorhabenDomainMapper;
    private final BauvorhabenRepository bauvorhabenRepository;
    private final InfrastrukturabfrageRepository infrastrukturabfrageRepository;
    private final KinderkrippeRepository kinderkrippeRepository;
    private final KindergartenRepository kindergartenRepositoryRepository;
    private final HausFuerKinderRepository hausFuerKinderRepository;
    private final GsNachmittagBetreuungRepository gsNachmittagBetreuungRepository;
    private final GrundschuleRepository grundschuleRepository;
    private final MittelschuleRepository mittelschuleRepository;

    /**
     * Die Methode gibt alle {@link BauvorhabenModel} als Liste zurück.
     *
     * @return Liste an {@link BauvorhabenModel}.
     */
    public List<BauvorhabenModel> getBauvorhaben() {
        return this.bauvorhabenRepository.findAllByOrderByGrundstuecksgroesseDesc()
                .map(this.bauvorhabenDomainMapper::entity2Model)
                .collect(Collectors.toList());
    }

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
     * @param bauvorhaben zum Speichern.
     * @return das gespeicherte {@link BauvorhabenModel}.
     */
    public BauvorhabenModel saveBauvorhaben(final BauvorhabenModel bauvorhaben) throws UniqueViolationException {
        var entity = this.bauvorhabenDomainMapper.model2Entity(bauvorhaben);
        var saved = this.bauvorhabenRepository.findByNameVorhabenIgnoreCase(entity.getNameVorhaben());
        if(saved.isPresent()) {
            throw new UniqueViolationException("Der angegebene Name des Bauvorhabens ist schon vorhanden, bitte wählen Sie daher einen anderen Namen und speichern Sie die Abfrage erneut.");
        } else {
            entity = this.bauvorhabenRepository.save(entity);
            return this.bauvorhabenDomainMapper.entity2Model(entity);
        }
    }

    /**
     * Diese Methode updated ein {@link BauvorhabenModel}.
     *
     * @param bauvorhaben zum Updaten.
     * @return das geupdatete {@link BauvorhabenModel}.
     * @throws EntityNotFoundException falls das Bauvorhaben identifiziert durch die {@link BauvorhabenModel#getId()} nicht gefunden wird.
     */
    public BauvorhabenModel updateBauvorhaben(final BauvorhabenModel bauvorhaben) throws EntityNotFoundException, UniqueViolationException {
        this.getBauvorhabenById(bauvorhaben.getId());
        return this.saveBauvorhaben(bauvorhaben);
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
        this.bauvorhabenRepository.deleteById(id);
    }

    /**
     * Setzt in einem {@link AbfrageModel} den Wert des Feldes 'bauvorhaben' auf das {@link BauvorhabenModel}, welches über die gegebene ID gefunden wurde.
     * Da im {@link AbfrageDto} das Bauvorhaben über eine ID und im {@link AbfrageModel} als ein {@link BauvorhabenModel} gespeichert wird, wird bei einem Mapping von Dto nach Model das Feld 'bauvorhaben' auf null gesetzt.
     * Diese Methode soll dann verwendet werden, um die beim Mapping verloren gegangene Information zum Bauvorhaben wieder in die Abfrage einzusetzen.
     * Der Parameter 'bauvorhabenId' darf null sein. In diesem Fall passiert nichts.
     *
     * @param bauvorhabenId id des {@link BauvorhabenModel}s. Darf null sein.
     * @param abfrage       zum Speichern.
     * @return Die (möglicherweise) geänderte Abfrage.
     * @throws EntityNotFoundException falls das Bauvorhaben mit der gegebenen ID nicht gefunden wurde.
     */
    public AbfrageModel assignBauvorhabenToAbfrage(@Nullable final UUID bauvorhabenId, final AbfrageModel abfrage) throws EntityNotFoundException {
        if (bauvorhabenId != null) {
            final var model = this.getBauvorhabenById(bauvorhabenId);
            abfrage.setBauvorhaben(model);
        }
        return abfrage;
    }

    /**
     * Setzt in einem {@link InfrastruktureinrichtungModel} den Wert des Feldes 'bauvorhaben' auf das {@link BauvorhabenModel}, welches über die gegebene ID gefunden wurde.
     * Da im {@link InfrastruktureinrichtungModel} das Bauvorhaben über eine ID und im {@link InfrastruktureinrichtungModel} als ein {@link BauvorhabenModel} gespeichert wird, wird bei einem Mapping von Dto nach Model das Feld 'bauvorhaben' auf null gesetzt.
     * Diese Methode soll dann verwendet werden, um die beim Mapping verloren gegangene Information zum Bauvorhaben wieder in der Infrastruktureinrichung einzusetzen.
     * Der Parameter 'bauvorhabenId' darf null sein. In diesem Fall passiert nichts.
     *
     * @param bauvorhabenId id des {@link BauvorhabenModel}s. Darf null sein.
     * @param infrastruktureinrichtung zum Speichern.
     * @return Die (möglicherweise) geänderte Infrastruktureinrichtung.
     * @throws EntityNotFoundException falls das Bauvorhaben mit der gegebenen ID nicht gefunden wurde.
     */
    public InfrastruktureinrichtungModel assignBauvorhabenToInfrastruktureinrichtung(@Nullable final UUID bauvorhabenId, final InfrastruktureinrichtungModel infrastruktureinrichtung) throws EntityNotFoundException {
        if (bauvorhabenId != null) {
            final var model = this.getBauvorhabenById(bauvorhabenId);
            infrastruktureinrichtung.setBauvorhaben(model);
        }
        return infrastruktureinrichtung;
    }

    /**
     * Wird das im Parameter gegebene {@link BauvorhabenModel} durch ein {@link AbfrageModel} referenziert,
     * wird eine {@link EntityIsReferencedException} geworfen.
     *
     * @param bauvorhaben zum Prüfen.
     * @throws EntityIsReferencedException falls das {@link BauvorhabenModel} durch ein {@link AbfrageModel} referenziert wird.
     */
    protected void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(final BauvorhabenModel bauvorhaben) throws EntityIsReferencedException {
        final List<String> nameAbfragen = this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenId(bauvorhaben.getId())
                .map(abfrage -> abfrage.getAbfrage().getNameAbfrage()).collect(Collectors.toList());
        if (!nameAbfragen.isEmpty()) {
            final var commaSeparatedNames = String.join(", ", nameAbfragen);
            final var message = "Das Bauvorhaben " + bauvorhaben.getNameVorhaben() + " wird durch die Abfragen " + commaSeparatedNames + " referenziert.";
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
    protected void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(final BauvorhabenModel bauvorhaben) throws EntityIsReferencedException {
        final List<String> namenInfrastruktureinrichtung = new ArrayList<String>();
        namenInfrastruktureinrichtung.addAll(this.kinderkrippeRepository.findAllByInfrastruktureinrichtungBauvorhabenId(bauvorhaben.getId())
                .map(kinderkrippe -> kinderkrippe.getInfrastruktureinrichtung().getNameEinrichtung()).collect(Collectors.toList()));
        namenInfrastruktureinrichtung.addAll(this.kindergartenRepositoryRepository.findAllByInfrastruktureinrichtungBauvorhabenId(bauvorhaben.getId())
                .map(kindergarten -> kindergarten.getInfrastruktureinrichtung().getNameEinrichtung()).collect(Collectors.toList()));
        namenInfrastruktureinrichtung.addAll(this.hausFuerKinderRepository.findAllByInfrastruktureinrichtungBauvorhabenId(bauvorhaben.getId())
                .map(hausFuerKinder -> hausFuerKinder.getInfrastruktureinrichtung().getNameEinrichtung()).collect(Collectors.toList()));
        namenInfrastruktureinrichtung.addAll(this.gsNachmittagBetreuungRepository.findAllByInfrastruktureinrichtungBauvorhabenId(bauvorhaben.getId())
                .map(gsNachmittagBetreuung -> gsNachmittagBetreuung.getInfrastruktureinrichtung().getNameEinrichtung()).collect(Collectors.toList()));
        namenInfrastruktureinrichtung.addAll(this.grundschuleRepository.findAllByInfrastruktureinrichtungBauvorhabenId(bauvorhaben.getId())
                .map(grundschule -> grundschule.getInfrastruktureinrichtung().getNameEinrichtung()).collect(Collectors.toList()));
        namenInfrastruktureinrichtung.addAll(this.mittelschuleRepository.findAllByInfrastruktureinrichtungBauvorhabenId(bauvorhaben.getId())
                .map(mittelschule -> mittelschule.getInfrastruktureinrichtung().getNameEinrichtung()).collect(Collectors.toList()));
        if (!namenInfrastruktureinrichtung.isEmpty()) {
            final var commaSeparatedNames = String.join(", ", namenInfrastruktureinrichtung);
            final var message = "Das Bauvorhaben " + bauvorhaben.getNameVorhaben() + " wird durch die Infrastruktureinrichtungen " + commaSeparatedNames + " referenziert.";
            log.error(message);
            throw new EntityIsReferencedException(message);
        }
    }


}
