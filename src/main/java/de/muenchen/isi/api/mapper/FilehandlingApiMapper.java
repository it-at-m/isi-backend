package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.filehandling.FilepathDto;
import de.muenchen.isi.api.dto.filehandling.MediaTypeInformationDto;
import de.muenchen.isi.api.dto.filehandling.PresignedUrlDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.domain.model.filehandling.MediaTypeInformationModel;
import de.muenchen.isi.domain.model.filehandling.PresignedUrlModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface FilehandlingApiMapper {

    PresignedUrlDto model2Dto(final PresignedUrlModel model);

    MediaTypeInformationDto model2Dto(final MediaTypeInformationModel model);

    FilepathModel dto2Model(final FilepathDto dto);

}
