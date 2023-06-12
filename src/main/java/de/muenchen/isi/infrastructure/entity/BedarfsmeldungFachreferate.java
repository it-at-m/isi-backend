package de.muenchen.isi.infrastructure.entity;

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
    private Long anzahlKinderkrippengruppen;

    @Column(nullable = false)
    private Long anzahlKindergartengruppen;

    @Column(nullable = false)
    private Long anzahlHortgruppen;

    @Column(nullable = false)
    private Long anzahlGrundschulzuege;
}
