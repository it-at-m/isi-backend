package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VerortungPoint extends Verortung {

    private PointGeometry point;
}
