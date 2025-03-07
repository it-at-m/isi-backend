package de.muenchen.isi.domain.model.search.response;

import de.muenchen.isi.domain.model.common.Wgs84Model;
import de.muenchen.isi.infrastructure.entity.enums.EntityType;
import lombok.Data;

@Data
public abstract class SearchResultModel {

    private EntityType type;

    private Wgs84Model coordinate;
}
