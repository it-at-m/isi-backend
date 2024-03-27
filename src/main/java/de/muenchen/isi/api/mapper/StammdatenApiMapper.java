package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.stammdaten.FileInformationDto;
import de.muenchen.isi.api.dto.stammdaten.FoerdermixStammDto;
import de.muenchen.isi.api.dto.stammdaten.MetabaseReportingDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.stammdaten.FileInformationModel;
import de.muenchen.isi.domain.model.stammdaten.FoerdermixStammModel;
import de.muenchen.isi.domain.model.stammdaten.MetabaseReportingModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface StammdatenApiMapper {
    FoerdermixStammModel dto2Model(final FoerdermixStammDto dto);

    FoerdermixStammDto model2Dto(final FoerdermixStammModel model);

    FileInformationDto model2Dto(final FileInformationModel model);

    MetabaseReportingDto model2Dto(final MetabaseReportingModel model);
}
