package de.muenchen.isi.domain.model.common;

import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class KommentarBauvorhabenModel extends KommentarModel {

    private UUID bauvorhaben;
}
