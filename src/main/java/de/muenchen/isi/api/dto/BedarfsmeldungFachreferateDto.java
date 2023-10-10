package de.muenchen.isi.api.dto;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BedarfsmeldungFachreferateDto extends BaseEntityDto {

    private Integer anzahlEinrichtungen;

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    private Integer anzahlKinderkrippengruppen;

    private Integer anzahlKindergartengruppen;

    private Integer anzahlHortgruppen;

    private Integer anzahlGrundschulzuege;
}
