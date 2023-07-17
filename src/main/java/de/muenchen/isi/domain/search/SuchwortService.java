package de.muenchen.isi.domain.search;

import de.muenchen.isi.domain.mapper.SearchDomainMapper;
import de.muenchen.isi.domain.model.search.SuchwortModel;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.search.Suchwort;
import de.muenchen.isi.infrastructure.repository.search.SuchwortRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuchwortService {

    private final SuchwortRepository suchwortRepository;

    private final EntityManager entityManager;

    private final SearchDomainMapper searchDomainMapper;

    public Set<SuchwortModel> extractSearchwordSuggestion(final String singleWordQuery) {
        final var adaptedQuery = StringUtils.lowerCase(StringUtils.trimToEmpty(singleWordQuery));

        SearchSession searchSession = Search.session(entityManager.getEntityManagerFactory().createEntityManager());

        return searchSession
            .search(Suchwort.class)
            .where(function -> function.wildcard().field("suchwort").matching(adaptedQuery))
            .fetch(20)
            .hits()
            .stream()
            .map(searchDomainMapper::entity2Model)
            .collect(Collectors.toSet());
    }

    public void deleteOldSearchwordsAndAddNewSearchwords(final Infrastrukturabfrage infrastrukturabfrage) {
        final Set<String> suchwoerter = new HashSet<>();
        suchwoerter.add(infrastrukturabfrage.getAbfrage().getNameAbfrage());
        deleteOldSearchwordsAndAddNewSearchwords(infrastrukturabfrage.getId(), suchwoerter);
    }

    public void deleteOldSearchwordsAndAddNewSearchwords(final Bauvorhaben bauvorhaben) {
        final Set<String> suchwoerter = new HashSet<>();
        suchwoerter.add(bauvorhaben.getNameVorhaben());
        deleteOldSearchwordsAndAddNewSearchwords(bauvorhaben.getId(), suchwoerter);
    }

    public void deleteOldSearchwordsAndAddNewSearchwords(final Infrastruktureinrichtung infrastruktureinrichtung) {
        final Set<String> suchwoerter = new HashSet<>();
        suchwoerter.add(infrastruktureinrichtung.getNameEinrichtung());
        deleteOldSearchwordsAndAddNewSearchwords(infrastruktureinrichtung.getId(), suchwoerter);
    }

    public void deleteOldSearchwordsAndAddNewSearchwords(final UUID id, Set<String> suchwoerter) {
        suchwortRepository.deleteAllByReferenceId(id);
        final Set<Suchwort> bauvorhabenSuchwoerter = suchwoerter
            .stream()
            .map(suchwort -> new Suchwort(suchwort, id))
            .collect(Collectors.toSet());
        suchwortRepository.saveAllAndFlush(bauvorhabenSuchwoerter);
    }
}
