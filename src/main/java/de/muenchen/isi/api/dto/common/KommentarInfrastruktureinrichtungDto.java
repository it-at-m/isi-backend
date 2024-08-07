package de.muenchen.isi.api.dto.common;

import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class KommentarInfrastruktureinrichtungDto extends KommentarDto {

    private UUID infrastruktureinrichtung;
}
