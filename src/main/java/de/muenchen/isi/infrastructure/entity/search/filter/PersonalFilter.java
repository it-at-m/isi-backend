package de.muenchen.isi.infrastructure.entity.search.filter;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "personalFilterSettings")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PersonalFilter extends BaseEntity {

    private String personalID;

    private String filterName;

    @NotNull
    @Embedded
    private FilterSettings filterSettings;
}
