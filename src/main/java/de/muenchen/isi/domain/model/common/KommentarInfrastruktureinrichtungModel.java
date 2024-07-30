package de.muenchen.isi.domain.model.common;

import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class KommentarInfrastruktureinrichtungModel extends KommentarModel {

    private UUID infrastruktureinrichtung;
}
