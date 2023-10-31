package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.stammdaten.FileInformationDto;
import de.muenchen.isi.api.dto.stammdaten.FoerdermixStammDto;
import de.muenchen.isi.api.dto.stammdaten.SobonOrientierungswertDto;
import de.muenchen.isi.api.dto.stammdaten.StaedtbaulicherOrientierungswertDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.stammdaten.FileInformationModel;
import de.muenchen.isi.domain.model.stammdaten.FoerdermixStammModel;
import de.muenchen.isi.domain.model.stammdaten.SobonOrientierungswertModel;
import de.muenchen.isi.domain.model.stammdaten.StaedtbaulicherOrientierungswertModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface StammdatenApiMapper {
    SobonOrientierungswertModel dto2Model(final SobonOrientierungswertDto dto);

    SobonOrientierungswertDto model2Dto(final SobonOrientierungswertModel model);

    StaedtbaulicherOrientierungswertModel dto2Model(final StaedtbaulicherOrientierungswertDto dto);

    StaedtbaulicherOrientierungswertDto model2Dto(final StaedtbaulicherOrientierungswertModel model);

    FoerdermixStammModel dto2Model(final FoerdermixStammDto dto);

    FoerdermixStammDto model2Dto(final FoerdermixStammModel model);

    FileInformationDto model2Dto(final FileInformationModel model);
}
