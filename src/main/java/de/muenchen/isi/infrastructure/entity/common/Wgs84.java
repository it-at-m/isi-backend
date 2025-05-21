package de.muenchen.isi.infrastructure.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class Wgs84 {

    @Column
    private Double latitude;

    @Column
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
