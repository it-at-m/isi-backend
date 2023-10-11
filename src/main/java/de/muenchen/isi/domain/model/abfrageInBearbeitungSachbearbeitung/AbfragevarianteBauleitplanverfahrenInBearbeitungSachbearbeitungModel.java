/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.domain.model.AbfragevarianteSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtModel;
import lombok.Data;

@Data
public class AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel
    extends AbfragevarianteBauleitplanverfahrenAngelegtModel {

    private AbfragevarianteSachbearbeitungModel abfragevarianteSachbearbeitung;
}
