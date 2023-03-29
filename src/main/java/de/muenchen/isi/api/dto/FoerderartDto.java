package de.muenchen.isi.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
public class FoerderartDto {

    private String bezeichnung;

    @EqualsAndHashCode.Exclude
    private BigDecimal anteilProzent;

}
