package de.muenchen.isi.domain.service.common;

import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.common.BearbeitungshistorieModel;
import de.muenchen.isi.infrastructure.entity.common.Bearbeitungshistorie;
import de.muenchen.isi.infrastructure.entity.enums.BearbeitungsArt;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.security.AuthenticationUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BearbeitungshistorieService {

    private final AbfrageDomainMapper abfrageDomainMapper;

    private final AuthenticationUtils authenticationUtils;

    /**
     * Fügt ein {@link BearbeitungshistorieModel} an die Abfrage an.
     *
     * @param abfrage zum anfügen des {@link BearbeitungshistorieModel}.
     * @param art     der Bearbeitung. Eine Aktualisierung ändert den Status nicht und erhält keinen Zielstatus.
     * @return die Abfrage mit dem angefügten {@link BearbeitungshistorieModel}.
     */
    public AbfrageModel appendBearbeitungshistorieToAbfrage(final AbfrageModel abfrage, final BearbeitungsArt art) {
        final var statusAbfrage = BearbeitungsArt.AKTUALISIERT.equals(art) ? null : abfrage.getStatusAbfrage();
        final var bearbeitungshistorie = createBearbeitungshistorieForStatus(statusAbfrage, art);
        abfrage.getBearbeitungshistorie().add(bearbeitungshistorie);
        return abfrage;
    }

    /**
     * Schreibt die Bearbeitungshistorie eines Bauvorhabens oder einer Infrastruktureinrichtung beim Speichern fort.
     * <p>
     * Die Historie wird nicht über die API übermittelt und deshalb aus der Datenbank übernommen.
     *
     * @param id                   der zu speichernden Entität oder null bei einer Neuanlage.
     * @param gespeicherteHistorie liefert die bisherige Bearbeitungshistorie zur id.
     * @return die bisherige Bearbeitungshistorie mit dem Eintrag für die aktuelle Bearbeitung.
     */
    public List<Bearbeitungshistorie> fortschreiben(
        final UUID id,
        final Function<UUID, List<Bearbeitungshistorie>> gespeicherteHistorie
    ) {
        final var eintrag = new Bearbeitungshistorie();
        eintrag.setArt(id == null ? BearbeitungsArt.ERSTELLT : BearbeitungsArt.AKTUALISIERT);
        eintrag.setZeitpunkt(LocalDateTime.now());
        eintrag.setBearbeitendePerson(authenticationUtils.getBearbeitendePerson());
        final var historie = new ArrayList<Bearbeitungshistorie>();
        if (id != null) {
            historie.addAll(gespeicherteHistorie.apply(id));
        }
        historie.add(eintrag);
        return historie;
    }

    protected BearbeitungshistorieModel createBearbeitungshistorieForStatus(
        final StatusAbfrage status,
        final BearbeitungsArt art
    ) {
        final var bearbeitendePerson = abfrageDomainMapper.entity2Model(authenticationUtils.getBearbeitendePerson());
        final var bearbeitungshistorie = new BearbeitungshistorieModel();
        bearbeitungshistorie.setArt(art);
        bearbeitungshistorie.setZielStatus(status);
        bearbeitungshistorie.setZeitpunkt(LocalDateTime.now());
        bearbeitungshistorie.setBearbeitendePerson(bearbeitendePerson);
        return bearbeitungshistorie;
    }
}
