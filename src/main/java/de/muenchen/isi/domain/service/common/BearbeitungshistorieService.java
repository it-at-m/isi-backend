package de.muenchen.isi.domain.service.common;

import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.common.BearbeitungshistorieModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.security.AuthenticationUtils;
import java.time.LocalDateTime;
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
     * @return die Abfrage mit dem angefügten {@link BearbeitungshistorieModel}.
     */
    public AbfrageModel appendBearbeitungshistorieToAbfrage(final AbfrageModel abfrage) {
        final var statusAbfrage = abfrage.getStatusAbfrage();
        final var bearbeitungshistorie = createBearbeitungshistorieForStatus(statusAbfrage);
        abfrage.getBearbeitungshistorie().add(bearbeitungshistorie);
        return abfrage;
    }

    protected BearbeitungshistorieModel createBearbeitungshistorieForStatus(final StatusAbfrage status) {
        final var bearbeitendePerson = abfrageDomainMapper.entity2Model(authenticationUtils.getBearbeitendePerson());
        final var bearbeitungshistorie = new BearbeitungshistorieModel();
        bearbeitungshistorie.setZielStatus(status);
        bearbeitungshistorie.setZeitpunkt(LocalDateTime.now());
        bearbeitungshistorie.setBearbeitendePerson(bearbeitendePerson);
        return bearbeitungshistorie;
    }
}
