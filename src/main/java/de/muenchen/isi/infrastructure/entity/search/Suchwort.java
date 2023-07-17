package de.muenchen.isi.infrastructure.entity.search;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = { @Index(name = "reference_id_index", columnList = "referenceId") })
@Indexed
public class Suchwort extends BaseEntity {

    @FullTextField(analyzer = "searchword_analyzer_string_field")
    @Column(nullable = false)
    private String suchwort;

    @Column(nullable = false)
    private UUID referenceId;
}
