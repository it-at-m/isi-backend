package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.stammdaten.FoerdermixStammModel;
import de.muenchen.isi.domain.model.stammdaten.SobonOrientierungswertSozialeInfrastrukturModel;
import de.muenchen.isi.domain.model.stammdaten.StaedtebaulicheOrientierungswertModel;
import de.muenchen.isi.domain.model.stammdaten.UmlegungFoerderartenModel;
import de.muenchen.isi.domain.model.stammdaten.VersorgungsquoteGruppenstaerkeModel;
import de.muenchen.isi.infrastructure.csv.PrognosedatenKitaPlbCsv;
import de.muenchen.isi.infrastructure.csv.SobonOrientierungswertSozialeInfrastrukturCsv;
import de.muenchen.isi.infrastructure.csv.StaedtebaulicheOrientierungswertCsv;
import de.muenchen.isi.infrastructure.entity.enums.Altersgruppe;
import de.muenchen.isi.infrastructure.entity.stammdaten.FoerdermixStamm;
import de.muenchen.isi.infrastructure.entity.stammdaten.PrognoseKitaPlb;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonOrientierungswertSozialeInfrastruktur;
import de.muenchen.isi.infrastructure.entity.stammdaten.StaedtebaulicheOrientierungswert;
import de.muenchen.isi.infrastructure.entity.stammdaten.UmlegungFoerderarten;
import de.muenchen.isi.infrastructure.entity.stammdaten.VersorgungsquoteGruppenstaerke;
import java.util.stream.Stream;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapstructConfiguration.class)
public interface StammdatenDomainMapper {
    SobonOrientierungswertSozialeInfrastrukturModel entity2Model(
        final SobonOrientierungswertSozialeInfrastruktur entity
    );

    SobonOrientierungswertSozialeInfrastruktur model2Entity(
        final SobonOrientierungswertSozialeInfrastrukturModel model
    );

    @Mappings(
        {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
        }
    )
    SobonOrientierungswertSozialeInfrastruktur csv2Entity(final SobonOrientierungswertSozialeInfrastrukturCsv csv);

    StaedtebaulicheOrientierungswertModel entity2Model(final StaedtebaulicheOrientierungswert entity);

    StaedtebaulicheOrientierungswert model2Entity(final StaedtebaulicheOrientierungswertModel model);

    @Mappings(
        {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
        }
    )
    StaedtebaulicheOrientierungswert csv2Entity(final StaedtebaulicheOrientierungswertCsv csv);

    FoerdermixStammModel entity2Model(final FoerdermixStamm entity);

    FoerdermixStamm model2Entity(final FoerdermixStammModel model);

    UmlegungFoerderartenModel entity2Model(final UmlegungFoerderarten entity);

    UmlegungFoerderarten model2Entity(final UmlegungFoerderartenModel model);

    VersorgungsquoteGruppenstaerkeModel entity2Model(final VersorgungsquoteGruppenstaerke entity);

    VersorgungsquoteGruppenstaerke model2entity(final VersorgungsquoteGruppenstaerkeModel model);

    /**
     * Die Methode erstellt aus dem im Parameter gegebenen CSV eintrag jeweils eine
     * Entität für die Altersgruppe der {@link Altersgruppe#NULL_ZWEI_JAEHRIGE}
     * und {@link Altersgruppe#DREI_FUENF_UND_FUENFZIG_PROZENT_SECHS_JAEHRIGE}.
     *
     * @param csv zur Erstellung der Entitäten.
     * @return die erstellten Entitäten.
     */
    default Stream<PrognoseKitaPlb> csv2EntityForEachAltersgruppe(final PrognosedatenKitaPlbCsv csv) {
        return Stream.of(
            this.csv2EntityAnzahlNullBisZweiJaehrige(csv),
            this.csv2EntityAnzahlDreiBisFuenfJaehrigeUndFuenfzigProzentSechsJaehrige(csv)
        );
    }

    @Mappings(
        {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(target = "altersgruppe", constant = Altersgruppe.Values.NULL_ZWEI_JAEHRIGE),
            @Mapping(source = "anzahlNullBisZweiJaehrige", target = "anzahlKinder"),
        }
    )
    PrognoseKitaPlb csv2EntityAnzahlNullBisZweiJaehrige(final PrognosedatenKitaPlbCsv csv);

    @Mappings(
        {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
            @Mapping(
                target = "altersgruppe",
                constant = Altersgruppe.Values.DREI_FUENF_UND_FUENFZIG_PROZENT_SECHS_JAEHRIGE
            ),
            @Mapping(source = "anzahlDreiBisFuenfJaehrigeUndFuenfzigProzentSechsJaehrige", target = "anzahlKinder"),
        }
    )
    PrognoseKitaPlb csv2EntityAnzahlDreiBisFuenfJaehrigeUndFuenfzigProzentSechsJaehrige(
        final PrognosedatenKitaPlbCsv csv
    );
}
