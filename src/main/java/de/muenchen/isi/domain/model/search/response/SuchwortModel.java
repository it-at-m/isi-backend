package de.muenchen.isi.domain.model.search.response;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import java.util.UUID;
import lombok.Data;

@Data
public class SuchwortModel extends BaseEntity {

    private String suchwort;

    private UUID referenceId;
}
