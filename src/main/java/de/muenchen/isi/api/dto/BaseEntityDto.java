/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public abstract class BaseEntityDto {

    private UUID id;

    @EqualsAndHashCode.Exclude
    private LocalDateTime createdDateTime;

    @EqualsAndHashCode.Exclude
    private LocalDateTime lastModifiedDateTime;

}