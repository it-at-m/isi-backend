package de.muenchen.isi.api.dto.stammdaten;

import de.muenchen.isi.api.dto.BaseEntityDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SobonJahrDto extends BaseEntityDto {

    private Integer jahr;

    private LocalDate gueltigAb;

    private List<SobonOrientierungswertDto> sobonOrientierungswerte;

    private List<StaedtbaulicherOrientierungswertDto> staedtebaulicheOrientierungswerte;
}
