/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.abfrageEinplanungBedarfe;

import de.muenchen.isi.domain.model.BedarfsmeldungModel;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class AbfragevarianteBaugenehmigungsverfahrenEinplanungBedarfeModel {

    private UUID id;

    private Long version;

    private ArtAbfrage artAbfragevariante;

    private List<BedarfsmeldungModel> bedarfsmeldungAbfrageersteller;

    private List<DokumentModel> bedarfsmeldungDokumenteAbfrageersteller;

    private String anmerkungAbfrageersteller;
}
