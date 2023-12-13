/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Column(length = 36)
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Version
    private Long version;

    @GenericField(sortable = Sortable.YES)
    @EqualsAndHashCode.Exclude
    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP", updatable = false)
    private LocalDateTime createdDateTime;

    @GenericField(sortable = Sortable.YES)
    @EqualsAndHashCode.Exclude
    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime lastModifiedDateTime;
}
