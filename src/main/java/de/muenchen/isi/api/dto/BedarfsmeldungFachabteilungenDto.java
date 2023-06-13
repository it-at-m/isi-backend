package de.muenchen.isi.api.dto;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BedarfsmeldungFachabteilungenDto extends BaseEntityDto {

    private Long anzahlEinrichtungen;

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    private Long anzahlKinderkrippengruppen;

    private Long anzahlKindergartengruppen;

    private Long anzahlHortgruppen;

    private Long anzahlGrundschulzuege;
}
