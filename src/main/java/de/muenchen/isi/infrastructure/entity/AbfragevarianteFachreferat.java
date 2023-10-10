package de.muenchen.isi.infrastructure.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import lombok.Data;

@Embeddable
@Data
public class AbfragevarianteFachreferat {

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "abfragevariante_bauleitplanverfahren_id", referencedColumnName = "id")
    @OrderBy("createdDateTime asc")
    private List<BedarfsmeldungFachreferate> bedarfsmeldungFachreferate;
}
