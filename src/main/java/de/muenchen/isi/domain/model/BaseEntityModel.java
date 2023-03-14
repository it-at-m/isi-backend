/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public abstract class BaseEntityModel {

    private UUID id;

    private Long version;

    @EqualsAndHashCode.Exclude
    private LocalDateTime createdDateTime;

    @EqualsAndHashCode.Exclude
    private LocalDateTime lastModifiedDateTime;
}
