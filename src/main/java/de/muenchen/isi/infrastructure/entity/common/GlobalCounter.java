package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.CounterType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(
    indexes = { @Index(name = "counter_type_index", columnList = "counterType") },
    uniqueConstraints = { @UniqueConstraint(columnNames = { "counterType" }) }
)
public class GlobalCounter extends BaseEntity {

    public GlobalCounter() {}

    public GlobalCounter(CounterType counterType, long counter) {
        this.counterType = counterType;
        this.counter = counter;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CounterType counterType;

    @Column(nullable = false)
    private long counter;
}
