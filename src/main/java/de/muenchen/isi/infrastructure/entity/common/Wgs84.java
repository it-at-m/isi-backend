package de.muenchen.isi.infrastructure.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

@Data
@NoArgsConstructor
@Embeddable
public class Wgs84 {

    @Column
    @GenericField(projectable = Projectable.YES)
    private Double latitude;

    @Column
    @GenericField(projectable = Projectable.YES)
    private Double longitude;

    @Override
    public String toString() {
        return latitude + "," + longitude;
    }

    public static Wgs84 fromString(String value) {
        String[] split = value.split(",");
        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid Wgs84 Format: " + value);
        }
        Wgs84 result = new Wgs84();
        result.latitude = Double.valueOf(split[0]);
        result.longitude = Double.valueOf(split[1]);
        return result;
    }
}
