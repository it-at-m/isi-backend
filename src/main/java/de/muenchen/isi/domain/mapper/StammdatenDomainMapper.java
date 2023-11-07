package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.stammdaten.FoerdermixStammModel;
import de.muenchen.isi.domain.model.stammdaten.SobonOrientierungswertModel;
import de.muenchen.isi.domain.model.stammdaten.StaedtbaulicherOrientierungswertModel;
import de.muenchen.isi.domain.model.stammdaten.UmlegungModel;
import de.muenchen.isi.infrastructure.csv.SobonOrientierungswertCsv;
import de.muenchen.isi.infrastructure.csv.StaedtebaulicheOrientierungswertCsv;
import de.muenchen.isi.infrastructure.entity.stammdaten.FoerdermixStamm;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonOrientierungswert;
import de.muenchen.isi.infrastructure.entity.stammdaten.StaedtbaulicherOrientierungwert;
import de.muenchen.isi.infrastructure.entity.stammdaten.Umlegung;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface StammdatenDomainMapper {
    SobonOrientierungswertModel entity2Model(final SobonOrientierungswert entity);

    SobonOrientierungswert model2Entity(final SobonOrientierungswertModel model);

    SobonOrientierungswert csv2Entity(final SobonOrientierungswertCsv csv);

    StaedtbaulicherOrientierungswertModel entity2Model(final StaedtbaulicherOrientierungwert entity);

    StaedtbaulicherOrientierungwert model2Entity(final StaedtbaulicherOrientierungswertModel model);

    StaedtbaulicherOrientierungwert csv2Entity(final StaedtebaulicheOrientierungswertCsv csv);

    FoerdermixStammModel entity2Model(final FoerdermixStamm entity);

    FoerdermixStamm model2Entity(final FoerdermixStammModel model);

    UmlegungModel entity2Model(final Umlegung entity);

    Umlegung model2Entity(final UmlegungModel model);
}
