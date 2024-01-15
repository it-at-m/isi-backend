package de.muenchen.isi.domain.model.common;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class UtmModel {

    private String zone;

    private Double east;

    private Double north;
}
