package de.muenchen.isi.domain.model.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@Embeddable
public class UtmModel {

    private String zone;

    private Double east;

    private Double north;

}
