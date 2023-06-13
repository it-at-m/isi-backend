package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BedarfsmeldungFachreferate extends BaseEntity {

    @Column(nullable = false)
    private Long anzahlEinrichtungen;

    @Column(nullable = false)
    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    @Column(nullable = true)
    private Long anzahlKinderkrippengruppen;

    @Column(nullable = true)
    private Long anzahlKindergartengruppen;

    @Column(nullable = true)
    private Long anzahlHortgruppen;

    @Column(nullable = true)
    private Long anzahlGrundschulzuege;
}
