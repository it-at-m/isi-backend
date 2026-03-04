package de.muenchen.isi.domain.service.stammdaten;

import de.muenchen.isi.domain.mapper.StammdatenDomainMapper;
import de.muenchen.isi.domain.model.stammdaten.FoerdermixStammModel;
import de.muenchen.isi.domain.model.stammdaten.VersorgungsquoteSobonHortModel;
import de.muenchen.isi.infrastructure.repository.stammdaten.FoerdermixStammRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.VersorgungsquoteSobonHortRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VersorungsquoteService {

    private final StammdatenDomainMapper stammdatenDomainMapper;

    private final VersorgungsquoteSobonHortRepository versorgungsquoteSobonHortRepository;

    /**
     * Die Methode gibt alle {@link FoerdermixStammModel} als Liste zurück.
     *
     * @return Liste an {@link FoerdermixStammModel}
     */
    public List<VersorgungsquoteSobonHortModel> getVersorgungsquoteHortSobon() {
        return this.versorgungsquoteSobonHortRepository.findAll()
            .stream()
            .map(this.stammdatenDomainMapper::entity2Model)
            .collect(Collectors.toList());
    }
}
