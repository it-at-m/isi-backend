package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.filter.PersonalFilterRequestModel;
import de.muenchen.isi.domain.model.filter.PersonalFilterResponseModel;
import de.muenchen.isi.infrastructure.entity.filter.PersoenlicherFilter;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfiguration.class)
public interface PersonalFilterDomainMapper {
    /**
     * Die persönlichen Filter werden als JSON gehalten und tragen deshalb keine eigene Version;
     * der Schutz vor verlorenen Updates liegt auf dem umgebenden Benutzer-Datensatz.
     */
    @Mapping(target = "version", ignore = true)
    PersonalFilterResponseModel entity2Model(final PersoenlicherFilter persoenlicherFilter);

    List<PersonalFilterResponseModel> entities2Models(final List<PersoenlicherFilter> persoenlicheFilter);

    // Id und Zeitstempel werden im Service gesetzt und nie aus dem Request übernommen.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    PersoenlicherFilter model2Entity(final PersonalFilterRequestModel personalFilterRequestModel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    void updateEntityFromModel(PersonalFilterRequestModel model, @MappingTarget PersoenlicherFilter entity);
}
