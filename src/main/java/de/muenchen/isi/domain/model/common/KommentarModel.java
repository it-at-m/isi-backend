package de.muenchen.isi.domain.model.common;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class KommentarModel extends BaseEntityModel {

    private String datum;

    private String text;

    private UUID bauvorhaben;

    private UUID infrastruktureinrichtung;
}
