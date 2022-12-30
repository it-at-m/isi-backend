package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaugebietModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.calculation.RealisierungsZeitraumModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenInformationModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class WohneinheitenInformationService {

    /**
     * @param abfragevariante zum Summieren.
     * @return das summierte {@link WohneinheitenInformationModel} aus den {@link BauabschnittModel}s
     * des im Parameter übergebenen {@link AbfragevarianteModel}.
     */
    public WohneinheitenInformationModel calculateWohneinheitenInformation(final AbfragevarianteModel abfragevariante) {
        return ObjectUtils.defaultIfNull(abfragevariante.getBauabschnitte(), new ArrayList<BauabschnittModel>())
                .stream()
                .map(this::calculateWohneinheitenInformation)
                .reduce(this.initEmptyWohneinheitenInformationModel(), this::sum);
    }

    /**
     * @param bauabschnitt zum Summieren.
     * @return das summierte {@link WohneinheitenInformationModel} aus den {@link BaugebietModel}s
     * des im Parameter übergebenen {@link BauabschnittModel}.
     */
    public WohneinheitenInformationModel calculateWohneinheitenInformation(final BauabschnittModel bauabschnitt) {
        return ObjectUtils.defaultIfNull(bauabschnitt.getBaugebiete(), new ArrayList<BaugebietModel>())
                .stream()
                .map(this::calculateWohneinheitenInformation)
                .reduce(this.initEmptyWohneinheitenInformationModel(), this::sum);
    }

    /**
     * @param baugebiet zum Summieren.
     * @return das summierte {@link WohneinheitenInformationModel} aus den {@link BaurateModel}s
     * des im Parameter übergebenen {@link BaugebietModel}.
     */
    public WohneinheitenInformationModel calculateWohneinheitenInformation(final BaugebietModel baugebiet) {
        return ObjectUtils.defaultIfNull(baugebiet.getBauraten(), new ArrayList<BaurateModel>())
                .stream()
                .map(this::calculateWohneinheitenInformation)
                .reduce(this.initEmptyWohneinheitenInformationModel(), this::sum);
    }

    /**
     * Ermittelt aus dem {@link BaurateModel} das {@link WohneinheitenInformationModel}.
     *
     * @param baurate zum Ermitteln.
     * @return das {@link WohneinheitenInformationModel} für ein {@link BaurateModel}.
     */
    public WohneinheitenInformationModel calculateWohneinheitenInformation(final BaurateModel baurate) {
        final var wohneinheitenInformation = this.initEmptyWohneinheitenInformationModel();

        wohneinheitenInformation.getRealisierungsZeitraum().setRealisierungVon(baurate.getJahr());
        wohneinheitenInformation.getRealisierungsZeitraum().setRealisierungBis(baurate.getJahr());
        wohneinheitenInformation.setAnzahlWohneinheitenGeplant(baurate.getAnzahlWeGeplant());
        wohneinheitenInformation.setGeschossflaecheWohnenGeplant(baurate.getGeschossflaecheWohnenGeplant());

        return wohneinheitenInformation;
    }

    /**
     * Die Methode summiert die Attribute zweier Objekte vom Typ {@link WohneinheitenInformationModel}
     * und gibt diese als neues Objekt vom selben Typ zurück.
     *
     * @param o1 zum Summieren.
     * @param o2 zum Summieren.
     * @return neues Objekt vom Typ {@link WohneinheitenInformationModel} mit summierten Werten der beiden Parameterobjekte.
     */
    public WohneinheitenInformationModel sum(final WohneinheitenInformationModel o1,
                                             final WohneinheitenInformationModel o2) {
        final var sumWohneinheitenInformation = this.initEmptyWohneinheitenInformationModel();

        final var realisierungsZeitraum = this.aggregate(
                o1.getRealisierungsZeitraum(),
                o2.getRealisierungsZeitraum()
        );
        sumWohneinheitenInformation.setRealisierungsZeitraum(realisierungsZeitraum);

        sumWohneinheitenInformation.setAnzahlWohneinheitenGeplant(
                ObjectUtils.defaultIfNull(o1.getAnzahlWohneinheitenGeplant(), 0)
                        + ObjectUtils.defaultIfNull(o2.getAnzahlWohneinheitenGeplant(), 0)
        );

        sumWohneinheitenInformation.setGeschossflaecheWohnenGeplant(
                ObjectUtils.defaultIfNull(o1.getGeschossflaecheWohnenGeplant(), BigDecimal.ZERO).add(
                        ObjectUtils.defaultIfNull(o2.getGeschossflaecheWohnenGeplant(), BigDecimal.ZERO)
                )
        );

        return sumWohneinheitenInformation;
    }

    /**
     * Die Methode aggregiert zwei Objekte vom Typ {@link RealisierungsZeitraumModel} zu einem neuen Objekt des selben Typs.
     *
     * @param o1 zum Aggregieren.
     * @param o2 zum Aggregieren.
     * @return das neue aggregierte Objekt.
     */
    public RealisierungsZeitraumModel aggregate(final RealisierungsZeitraumModel o1,
                                                final RealisierungsZeitraumModel o2) {
        final var realisierungsZeitraum = new RealisierungsZeitraumModel();

        final var von = ObjectUtils.min(
                o1.getRealisierungVon(),
                o2.getRealisierungVon()
        );
        final var bis = ObjectUtils.max(
                o1.getRealisierungBis(),
                o2.getRealisierungBis()
        );

        realisierungsZeitraum.setRealisierungVon(von);
        realisierungsZeitraum.setRealisierungBis(bis);

        return realisierungsZeitraum;
    }

    public WohneinheitenInformationModel initEmptyWohneinheitenInformationModel() {
        final var sumWohneinheitenInformation = new WohneinheitenInformationModel();
        sumWohneinheitenInformation.setRealisierungsZeitraum(new RealisierungsZeitraumModel());
        return sumWohneinheitenInformation;
    }

}
