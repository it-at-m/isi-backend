package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.model.list.AbfrageListElementModel;
import de.muenchen.isi.domain.model.list.AbfrageListElementsModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbfrageListService {

    private final AbfrageService abfrageService;

    private final AbfrageDomainMapper abfrageDomainMapper;

    /**
     * Die Methode gibt {@link AbfrageListElementModel}e sortiert in absteigender Reihenfolge zurück.
     *
     * @return die {@link AbfrageListElementModel}s sortiert in absteigender Reihenfolge.
     */
    public AbfrageListElementsModel getAbfrageListElements() {
        final var abfrageListElementsModel = new AbfrageListElementsModel();
        final List<AbfrageListElementModel> listElements = new ArrayList<>();

        // Holen und Mappen
        this.abfrageService.getInfrastrukturabfragen()
            .stream()
            .map(this.abfrageDomainMapper::model2ListElementModel)
            .forEach(listElements::add);

        // Sortieren
        listElements.sort(Comparator.comparing(AbfrageListElementModel::getFristStellungnahme));
        Collections.reverse(listElements);

        abfrageListElementsModel.setListElements(listElements);
        return abfrageListElementsModel;
    }

    /**
     * Die Methode gibt {@link AbfrageListElementModel}e sortiert nach createdDateTime aufsteigend zurück.
     *
     * @param bauvorhabenId um Bauvorhaben zu ermitteln
     * @return die {@link AbfrageListElementsModel}s sortiert in aufsteigender Reihenfolge.
     */
    public AbfrageListElementsModel getAbfrageListElementsThatReferenceBauvorhaben(final UUID bauvorhabenId) {
        final var abfrageListElementsModel = new AbfrageListElementsModel();
        final List<AbfrageListElementModel> listElements = new ArrayList<>();

        // Holen und Mappen
        this.abfrageService.getAllReferencedAbfragenForBauvorhaben(bauvorhabenId)
            .stream()
            .map(this.abfrageDomainMapper::model2ListElementModel)
            .forEach(listElements::add);

        // Sortieren
        listElements.sort(Comparator.comparing(AbfrageListElementModel::getFristStellungnahme));
        Collections.reverse(listElements);

        abfrageListElementsModel.setListElements(listElements);
        return abfrageListElementsModel;
    }
}
