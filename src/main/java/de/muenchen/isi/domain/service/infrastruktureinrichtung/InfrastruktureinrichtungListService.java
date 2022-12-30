package de.muenchen.isi.domain.service.infrastruktureinrichtung;

import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungListElementDomainMapper;
import de.muenchen.isi.domain.model.list.InfrastruktureinrichtungListElementModel;
import de.muenchen.isi.domain.model.list.InfrastruktureinrichtungListElementsModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfrastruktureinrichtungListService {

    private final KinderkrippeService kinderkrippeService;

    private final KindergartenService kindergartenService;

    private final HausFuerKinderService hausFuerKinderService;

    private final GsNachmittagBetreuungService gsNachmittagBetreuungService;

    private final GrundschuleService grundschuleService;

    private final MittelschuleService mittelschuleService;

    private final InfrastruktureinrichtungListElementDomainMapper infrastruktureinrichtungListElementDomainMapper;

    /**
     * Die Methode gibt {@link InfrastruktureinrichtungListElementModel}e sortiert aufsteigend nach Name der Einrichtung zurück.
     *
     * @return die {@link InfrastruktureinrichtungListElementModel}s sortiert in aufsteigender Reihenfolge.
     */
    public InfrastruktureinrichtungListElementsModel getInfrastruktureinrichtungListElements() {
        final var infrastruktureinrichtungListElementsModel = new InfrastruktureinrichtungListElementsModel();
        final List<InfrastruktureinrichtungListElementModel> listElements = new ArrayList<>();

        // Lesen und Mappen
        this.kinderkrippeService.getKinderkrippen().stream()
                .map(this.infrastruktureinrichtungListElementDomainMapper::kinderkrippeModel2InfrastruktureinrichtungListElementModel)
                .forEach(listElements::add);
        this.kindergartenService.getKindergaerten().stream()
                .map(this.infrastruktureinrichtungListElementDomainMapper::kindergartenModel2InfrastruktureinrichtungListElementModel)
                .forEach(listElements::add);
        this.hausFuerKinderService.getHaeuserFuerKinder().stream()
                .map(this.infrastruktureinrichtungListElementDomainMapper::hausFuerKinderModel2InfrastruktureinrichtungListElementModel)
                .forEach(listElements::add);
        this.gsNachmittagBetreuungService.getGsNachmittagBetreuungen().stream()
                .map(this.infrastruktureinrichtungListElementDomainMapper::gsNachmittagBetreuungModel2InfrastruktureinrichtungListElementModel)
                .forEach(listElements::add);
        this.grundschuleService.getGrundschulen().stream()
                .map(this.infrastruktureinrichtungListElementDomainMapper::grundschuleModel2InfrastruktureinrichtungListElementModel)
                .forEach(listElements::add);
        this.mittelschuleService.getMittelschulen().stream()
                .map(this.infrastruktureinrichtungListElementDomainMapper::mittelschuleModel2InfrastruktureinrichtungListElementModel)
                .forEach(listElements::add);

        // Sortieren
        listElements.sort(Comparator.comparing(InfrastruktureinrichtungListElementModel::getNameEinrichtung));

        infrastruktureinrichtungListElementsModel.setListElements(listElements);
        return infrastruktureinrichtungListElementsModel;

    }

}
