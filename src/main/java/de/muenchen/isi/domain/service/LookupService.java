package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.model.stammdaten.LookupEntryModel;
import de.muenchen.isi.domain.model.stammdaten.LookupListModel;
import de.muenchen.isi.domain.model.stammdaten.LookupListsModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtBaulicheNutzung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtDokument;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtGsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LookupService {

    /**
     * Erstellt die Lookup-Listen
     *
     * @return Lookup-Listen.
     */
    public LookupListsModel getLookupLists() {
        final var model = new LookupListsModel();
        model.setArtDokument(this.getArtDokumentList());
        model.setArtAbfrage(this.getArtAbfrageList());
        model.setUncertainBoolean(this.getUncertainBooleanList());
        model.setSobonVerfahrensgrundsaetzeJahr(this.getSobonVerfahrensgrundsaetzeJahrList());
        model.setStandVerfahrenBauleitplanverfahren(this.getStandVerfahrenBauleitplanverfahrenList());
        model.setStandVerfahrenBaugenehmigungsverfahren(this.getStandVerfahrenBaugenehmigungsverfahrenList());
        model.setStandVerfahren(this.getStandVerfahrenList());
        model.setStatusAbfrage(this.getStatusAbfrageList());
        model.setWesentlicheRechtsgrundlageBauleitplanverfahren(
            this.getWesentlicheRechtsgrundlageBauleitplanverfahrenList()
        );
        model.setWesentlicheRechtsgrundlageBaugenehmigungsverfahren(
            this.getWesentlicheRechtsgrundlageBaugenehmigungsverfahrenList()
        );
        model.setWesentlicheRechtsgrundlage(this.getWesentlicheRechtsgrundlageList());
        model.setArtBaulicheNutzung(this.getArtBaulicheNutzungList());
        model.setStatusInfrastruktureinrichtung((this.getStatusInfrastruktureinrichtungList()));
        model.setEinrichtungstraeger((this.getEinrichtungstraegerList()));
        model.setEinrichtungstraegerSchulen(this.getEinrichtungstraegerSchuleList());
        model.setInfrastruktureinrichtungTyp((this.getInfrastruktureinrichtungTypList()));
        model.setArtGsNachmittagBetreuung((this.getArtGsNachmittagBetreuungList()));
        model.setSobonOrientierungswertJahr(this.getSobonOrientierungswertJahr());
        return model;
    }

    private LookupListModel getArtDokumentList() {
        final List<LookupEntryModel> list = EnumUtils
            .getEnumList(ArtDokument.class)
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getArtAbfrageList() {
        final List<LookupEntryModel> list = EnumUtils
            .getEnumList(ArtAbfrage.class)
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getUncertainBooleanList() {
        final List<LookupEntryModel> list = EnumUtils
            .getEnumList(UncertainBoolean.class)
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getSobonVerfahrensgrundsaetzeJahrList() {
        final List<LookupEntryModel> list = EnumUtils
            .getEnumList(SobonVerfahrensgrundsaetzeJahr.class)
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getStandVerfahrenBauleitplanverfahrenList() {
        final List<LookupEntryModel> list = StandVerfahren
            .getStandVerfahrenForBauleitplanverfahren()
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getStandVerfahrenBaugenehmigungsverfahrenList() {
        final List<LookupEntryModel> list = StandVerfahren
            .getStandVerfahrenForBaugenehmigungsverfahren()
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getStandVerfahrenList() {
        final List<LookupEntryModel> list = EnumUtils
            .getEnumList(StandVerfahren.class)
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getStatusAbfrageList() {
        final List<LookupEntryModel> list = EnumUtils
            .getEnumList(StatusAbfrage.class)
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getWesentlicheRechtsgrundlageBauleitplanverfahrenList() {
        final List<LookupEntryModel> list = WesentlicheRechtsgrundlage
            .getWesentlicheRechtsgrundlageForBauleitplanverfahren()
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getWesentlicheRechtsgrundlageBaugenehmigungsverfahrenList() {
        final List<LookupEntryModel> list = WesentlicheRechtsgrundlage
            .getWesentlicheRechtsgrundlageForBaugenehmigungsverfahren()
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getWesentlicheRechtsgrundlageList() {
        final List<LookupEntryModel> list = EnumUtils
            .getEnumList(WesentlicheRechtsgrundlage.class)
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getArtBaulicheNutzungList() {
        final List<LookupEntryModel> list = EnumUtils
            .getEnumList(ArtBaulicheNutzung.class)
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getStatusInfrastruktureinrichtungList() {
        final List<LookupEntryModel> list = EnumUtils
            .getEnumList(StatusInfrastruktureinrichtung.class)
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getEinrichtungstraegerList() {
        final List<LookupEntryModel> list = Einrichtungstraeger
            .getEinrichtungstraeger()
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getEinrichtungstraegerSchuleList() {
        final List<LookupEntryModel> list = Einrichtungstraeger
            .getEinrichtungstraegerSchulen()
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getInfrastruktureinrichtungTypList() {
        final List<LookupEntryModel> list = EnumUtils
            .getEnumList(InfrastruktureinrichtungTyp.class)
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getArtGsNachmittagBetreuungList() {
        final List<LookupEntryModel> list = EnumUtils
            .getEnumList(ArtGsNachmittagBetreuung.class)
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }

    private LookupListModel getSobonOrientierungswertJahr() {
        final List<LookupEntryModel> list = EnumUtils
            .getEnumList(SobonOrientierungswertJahr.class)
            .stream()
            .map(item -> new LookupEntryModel(item.toString(), item.getBezeichnung()))
            .collect(Collectors.toList());

        return new LookupListModel(list);
    }
}
