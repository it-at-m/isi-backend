package de.muenchen.isi.domain.model.common;

import jakarta.validation.Valid;
import java.util.Set;
import lombok.Data;

@Data
public abstract class VerortungModel {

    private Set<StadtbezirkModel> stadtbezirke;

    private Set<BezirksteilModel> bezirksteile;

    private Set<ViertelModel> viertel;

    private Set<GemarkungModel> gemarkungen;

    private Set<KitaplanungsbereichModel> kitaplanungsbereiche;

    private Set<GrundschulsprengelModel> grundschulsprengel;

    private Set<MittelschulsprengelModel> mittelschulsprengel;

    private Set<SchulstandortModel> schulstandorte;
}
