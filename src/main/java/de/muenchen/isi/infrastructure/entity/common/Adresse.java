/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.infrastructure.adapter.search.AdresseBinder;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.TypeBinderRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.TypeBinding;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@TypeBinding(binder = @TypeBinderRef(type = AdresseBinder.class))
public class Adresse implements Cloneable, Serializable {

    @Column(nullable = true)
    private String strasse;

    @Column(nullable = true)
    private String hausnummer;

    @Column(nullable = true)
    private String plz;

    @Column(nullable = true)
    private String ort;

    @Embedded
    private Wgs84 coordinate;
}
