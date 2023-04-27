package de.muenchen.isi.api.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class FoerderartDto {

    private String bezeichnung;

    @EqualsAndHashCode.Exclude
    private BigDecimal anteilProzent;
}
