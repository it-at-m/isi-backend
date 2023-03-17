package de.muenchen.isi.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FoerderartDto {

    private String bezeichnung;

    private BigDecimal anteilProzent;

}
