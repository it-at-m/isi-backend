package de.muenchen.isi.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
public class FoerderartDto extends BaseEntityDto {

    private String bezeichnung;

    @EqualsAndHashCode.Exclude
    private BigDecimal anteilProzent;

}
