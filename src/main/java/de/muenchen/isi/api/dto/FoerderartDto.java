package de.muenchen.isi.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FoerderartDto extends BaseEntityDto {

    private String bezeichnung;

    private BigDecimal anteilProzent;

}
