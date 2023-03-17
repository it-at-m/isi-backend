package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.stammdaten.FileInformationDto;
import de.muenchen.isi.api.dto.stammdaten.FoerdermixStammDto;
import de.muenchen.isi.api.dto.stammdaten.SobonOrientierungswertSozialeInfrastrukturDto;
import de.muenchen.isi.api.dto.stammdaten.StaedtebaulicheOrientierungswertDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.stammdaten.FileInformationModel;
import de.muenchen.isi.domain.model.stammdaten.FoerdermixStammModel;
import de.muenchen.isi.domain.model.stammdaten.SobonOrientierungswertSozialeInfrastrukturModel;
import de.muenchen.isi.domain.model.stammdaten.StaedtebaulicheOrientierungswertModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface StammdatenApiMapper {
    SobonOrientierungswertSozialeInfrastrukturModel dto2Model(final SobonOrientierungswertSozialeInfrastrukturDto dto);

    SobonOrientierungswertSozialeInfrastrukturDto model2Dto(
        final SobonOrientierungswertSozialeInfrastrukturModel model
    );

    StaedtebaulicheOrientierungswertModel dto2Model(final StaedtebaulicheOrientierungswertDto dto);

    StaedtebaulicheOrientierungswertDto model2Dto(final StaedtebaulicheOrientierungswertModel model);

    FoerdermixStammModel dto2Model(final FoerdermixStammDto dto);

    FoerdermixStammDto model2Dto(final FoerdermixStammModel model);

    FileInformationDto model2Dto(final FileInformationModel model);
}
