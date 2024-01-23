package de.muenchen.isi.infrastructure.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.reporting.client.model.BaugenehmigungsverfahrenDto;
import de.muenchen.isi.reporting.client.model.BauleitplanverfahrenDto;
import de.muenchen.isi.reporting.client.model.SaveRequest;
import de.muenchen.isi.reporting.client.model.WeiteresVerfahrenDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfiguration.class)
public interface ReportingApiMapper {
    SaveRequest reportingDto2RequestDto(final BauleitplanverfahrenDto dto);

    @Mapping(target = "abfragevariantenBauleitplanverfahren", ignore = true)
    @Mapping(target = "abfragevariantenSachbearbeitungBauleitplanverfahren", ignore = true)
    @Mapping(target = "abfragevariantenWeiteresVerfahren", ignore = true)
    @Mapping(target = "abfragevariantenSachbearbeitungWeiteresVerfahren", ignore = true)
    SaveRequest reportingDto2RequestDto(final BaugenehmigungsverfahrenDto dto);

    @Mapping(target = "abfragevariantenBauleitplanverfahren", ignore = true)
    @Mapping(target = "abfragevariantenSachbearbeitungBauleitplanverfahren", ignore = true)
    SaveRequest reportingDto2RequestDto(final WeiteresVerfahrenDto dto);
}
