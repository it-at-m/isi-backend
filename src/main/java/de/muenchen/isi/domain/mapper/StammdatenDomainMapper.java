package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.stammdaten.FoerdermixStammModel;
import de.muenchen.isi.domain.model.stammdaten.SobonOrientierungswertSozialeInfrastrukturModel;
import de.muenchen.isi.domain.model.stammdaten.StaedtebaulicheOrientierungswertModel;
import de.muenchen.isi.infrastructure.csv.SobonOrientierungswertSozialeInfrastrukturCsv;
import de.muenchen.isi.infrastructure.csv.StaedtebaulicheOrientierungswertCsv;
import de.muenchen.isi.infrastructure.entity.stammdaten.FoerdermixStamm;
import de.muenchen.isi.infrastructure.entity.stammdaten.SobonOrientierungswertSozialeInfrastruktur;
import de.muenchen.isi.infrastructure.entity.stammdaten.StaedtebaulicheOrientierungswert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapstructConfiguration.class)
public interface StammdatenDomainMapper {

    SobonOrientierungswertSozialeInfrastrukturModel entity2Model(final SobonOrientierungswertSozialeInfrastruktur entity);

    SobonOrientierungswertSozialeInfrastruktur model2Entity(final SobonOrientierungswertSozialeInfrastrukturModel model);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
    })
    SobonOrientierungswertSozialeInfrastruktur csv2Entity(final SobonOrientierungswertSozialeInfrastrukturCsv csv);

    StaedtebaulicheOrientierungswertModel entity2Model(final StaedtebaulicheOrientierungswert entity);

    StaedtebaulicheOrientierungswert model2Entity(final StaedtebaulicheOrientierungswertModel model);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
    })
    StaedtebaulicheOrientierungswert csv2Entity(final StaedtebaulicheOrientierungswertCsv csv);

    FoerdermixStammModel entity2Model(final FoerdermixStamm entity);

    FoerdermixStamm model2Entity(final FoerdermixStammModel model);

}
