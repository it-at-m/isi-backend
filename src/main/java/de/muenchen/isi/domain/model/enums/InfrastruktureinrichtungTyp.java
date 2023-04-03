package de.muenchen.isi.domain.model.enums;

import de.muenchen.isi.domain.model.infrastruktureinrichtung.GrundschuleModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.GsNachmittagBetreuungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.HausFuerKinderModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KindergartenModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KinderkrippeModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.MittelschuleModel;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum InfrastruktureinrichtungTyp {
    KINDERKRIPPE(KinderkrippeModel.class),

    KINDERGARTEN(KindergartenModel.class),

    HAUS_FUER_KINDER(HausFuerKinderModel.class),

    GS_NACHMITTAG_BETREUUNG(GsNachmittagBetreuungModel.class),

    GRUNDSCHULE(GrundschuleModel.class),

    MITTELSCHULE(MittelschuleModel.class);

    @Getter
    private final Class clazz;

    public static List<InfrastruktureinrichtungTyp> getAsList() {
        return Arrays.asList(values());
    }

    public static Optional<InfrastruktureinrichtungTyp> findByClazz(final Class clazz) {
        return getAsList()
            .stream()
            .filter(infrastruktureinrichtungTyp -> Objects.equals(infrastruktureinrichtungTyp.getClazz(), clazz))
            .findFirst();
    }
}
