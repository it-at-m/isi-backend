package de.muenchen.isi.api.dto;

import de.muenchen.isi.domain.model.BedarfsmeldungFachabteilungenModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BedarfsmeldungFachabteilungenDto extends BaseEntityDto {

    private Long anzahlEinrichtungen;

    private Long anzahlKinderkrippengruppen;

    private Long anzahlKindergartengruppen;

    private Long anzahlHortgruppen;

    private Long anzahlGrundschulzuege;
}
