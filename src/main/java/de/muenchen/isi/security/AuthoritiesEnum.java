/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.security;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Each possible authority in this project is represented by an enum.
 * The enums are used within the {@link PagingAndSortingRepository}
 * in the annotation e.g. {@link PreAuthorize}.
 */
public enum AuthoritiesEnum {
    ISI_BACKEND_READ_ABFRAGE,
    ISI_BACKEND_POST_ABFRAGE,
    ISI_BACKEND_PUT_ABFRAGEVARIANTE_RELEVANT,
    ISI_BACKEND_PATCH_ABFRAGE_ANGELEGT,
    ISI_BACKEND_PATCH_ABFRAGE_IN_BEARBEITUNG_SACHBEARBEITUNG,
    ISI_BACKEND_PATCH_ABFRAGE_IN_BEARBEITUNG_FACHREFERATE,
    ISI_BACKEND_PATCH_ABFRAGE_BEDARFSMELDUNG_ERFOLGT,
    ISI_BACKEND_DELETE_ABFRAGE,

    ISI_BACKEND_FREIGABE_ABFRAGE,
    ISI_BACKEND_ABBRECHEN_ABFRAGE,
    ISI_BACKEND_ZURUECK_AN_ABFRAGEERSTELLUNG_ABFRAGE,
    ISI_BACKEND_IN_BEARBEITUNG_SETZTEN_ABFRAGE,
    ISI_BACKEND_ZURUECK_AN_SACHBEARBEITUNG_ABFRAGE,
    ISI_BACKEND_KEINE_BEARBEITUNG_NOETIG_ABFRAGE,
    ISI_BACKEND_VERSCHICKEN_DER_STELLUNGNAHME_ABFRAGE,
    ISI_BACKEND_BEDARFSMELDUNG_ERFOLGTE_ABFRAGE,
    ISI_BACKEND_SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG_ABFRAGE,
    ISI_BACKEND_ERNEUTE_BEARBEITUNG_ABFRAGE,
    ISI_BACKEND_READ_TRANSITIONS,

    ISI_BACKEND_DETERMINE_BAURATEN,

    ISI_BACKEND_READ_BAUVORHABEN,
    ISI_BACKEND_WRITE_BAUVORHABEN,
    ISI_BACKEND_DELETE_BAUVORHABEN,

    ISI_BACKEND_KOORDINATEN_TRANSFORM,

    ISI_BACKEND_LOOKUP_LIST,

    ISI_BACKEND_PRESIGNED_URL_GET_FILE,
    ISI_BACKEND_PRESIGNED_URL_SAVE_FILE,

    ISI_BACKEND_CALCULATE_WOHNEINHEITEN,

    ISI_BACKEND_READ_STAMMDATEN_FILEINFORMATION,

    ISI_BACKEND_READ_STAMMDATEN_FOERDERMIX,
    ISI_BACKEND_WRITE_STAMMDATEN_FOERDERMIX,
    ISI_BACKEND_DELETE_STAMMDATEN_FOERDERMIX,

    ISI_BACKEND_WRITE_STAMMDATEN_ORIENTIERUNGSWERTE,

    ISI_BACKEND_READ_INFRASTRUKTUREINRICHTUNG,
    ISI_BACKEND_WRITE_INFRASTRUKTUREINRICHTUNG,
    ISI_BACKEND_DELETE_INFRASTRUKTUREINRICHTUNG,

    ISI_BACKEND_READ_KOMMENTAR_BAUVORHABEN,
    ISI_BACKEND_READ_KOMMENTAR_INFRASTRUKTUREINRICHTUNG,
    ISI_BACKEND_WRITE_KOMMENTAR,
    ISI_BACKEND_DELETE_KOMMENTAR,
}
