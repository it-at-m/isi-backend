package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "artAbfragevariante")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class Abfragevariante extends BaseEntity {

    /**
     * Diese Methode gibt den Wert der {@link DiscriminatorColumn} zurück.
     * Ist kein {@link DiscriminatorValue} gesetzt, so wird null zurückgegeben.
     *
     * @return Wert der {@link DiscriminatorColumn}.
     */
    @Transient
    public ArtAbfrage getArtAbfragevariante() {
        final var discriminatorValue = this.getClass().getAnnotation(DiscriminatorValue.class);
        return ObjectUtils.isEmpty(discriminatorValue)
            ? null
            : EnumUtils.getEnum(ArtAbfrage.class, discriminatorValue.value());
    }

    @Column(nullable = false)
    private Integer abfragevariantenNr;

    @Column(nullable = false, length = 30)
    private String name;
}
