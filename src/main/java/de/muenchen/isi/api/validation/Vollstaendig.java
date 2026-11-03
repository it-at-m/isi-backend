package de.muenchen.isi.api.validation;

/**
 * Validierungsgruppe für Pflichtangaben einer Abfrage im Status ANGELEGT.
 * <p>
 * Ein Entwurf wird beim Speichern nur gegen die Gruppe {@link jakarta.validation.groups.Default} geprüft
 * (Name der Abfrage, Feldlängen, Wertebereiche). Die Constraints dieser Gruppe werden zusätzlich erst
 * bei der Freigabe der Abfrage geprüft.
 */
public interface Vollstaendig {}
