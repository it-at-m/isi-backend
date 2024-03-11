package de.muenchen.isi.infrastructure.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(
    indexes = {
        @Index(name = "bauabschnitt_abfrgvar_bauleitplnvrfhrn_id_index", columnList = "abfrgvar_bauleitplnvrfhrn_id"),
        @Index(name = "bauabschnitt_abfrgvar_baugnhmgsverfhrn_id_index", columnList = "abfrgvar_baugnhmgsverfhrn_id"),
        @Index(name = "bauabschnitt_abfrgvar_weitrs_vrfhrn_id_index", columnList = "abfrgvar_weitrs_vrfhrn_id"),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Bauabschnitt extends BaseEntity {

    @Column(nullable = false)
    private String bezeichnung;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "bauabschnitt_id")
    @OrderBy("createdDateTime asc")
    private List<Baugebiet> baugebiete;

    @Column(nullable = false)
    private Boolean technical;
}
