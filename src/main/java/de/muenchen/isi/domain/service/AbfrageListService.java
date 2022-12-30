package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.mapper.AbfrageListElementDomainMapper;
import de.muenchen.isi.domain.model.list.AbfrageListElementModel;
import de.muenchen.isi.domain.model.list.AbfrageListElementsModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbfrageListService {

    private final AbfrageService abfrageService;

    private final AbfrageListElementDomainMapper abfrageListElementDomainMapper;

    /**
     * Die Methode gibt {@link AbfrageListElementModel}e sortiert in absteigender Reihenfolge zurück.
     *
     * @return die {@link AbfrageListElementModel}s sortiert in absteigender Reihenfolge.
     */
    public AbfrageListElementsModel getAbfrageListElements() {
        final var abfrageListElementsModel = new AbfrageListElementsModel();
        final List<AbfrageListElementModel> listElements = new ArrayList<>();

        // Holen und Mappen
        this.abfrageService.getInfrastrukturabfragen().stream()
                .map(this.abfrageListElementDomainMapper::infrastrukturabfrageModel2AbfrageListElementModel)
                .forEach(listElements::add);

        // Sortieren
        listElements.sort(Comparator.comparing(AbfrageListElementModel::getFristStellungnahme));
        Collections.reverse(listElements);

        abfrageListElementsModel.setListElements(listElements);
        return abfrageListElementsModel;
    }

}
