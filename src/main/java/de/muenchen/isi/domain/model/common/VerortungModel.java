package de.muenchen.isi.domain.model.common;

import java.util.Set;
import lombok.Data;

@Data
public abstract class VerortungModel {

    private Set<StadtbezirkModel> stadtbezirke;

    private Set<GemarkungModel> gemarkungen;
}
