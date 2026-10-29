BEGIN;

--
-- Fehlende Indexe auf Fremdschlüsselspalten anlegen und doppelte Indexe entfernen.
--

-- Referenz auf das Bauvorhaben
CREATE INDEX IF NOT EXISTS bauleitplanverfahren_bauvorhaben_id_index
    ON isidbuser.bauleitplanverfahren (bauvorhaben_id);
CREATE INDEX IF NOT EXISTS baugenehmigungsverfahren_bauvorhaben_id_index
    ON isidbuser.baugenehmigungsverfahren (bauvorhaben_id);
CREATE INDEX IF NOT EXISTS weiteres_verfahren_bauvorhaben_id_index
    ON isidbuser.weiteres_verfahren (bauvorhaben_id);
CREATE INDEX IF NOT EXISTS infrastruktureinrichtung_bauvorhaben_id_index
    ON isidbuser.infrastruktureinrichtung (bauvorhaben_id);

-- Referenz auf die Basis der Bauratendatei
CREATE INDEX IF NOT EXISTS abfrgvar_bauleitplnvrfhrn_bauratendatei_basis_id_index
    ON isidbuser.abfrgvar_bauleitplnvrfhrn (bauratendatei_basis_id);
CREATE INDEX IF NOT EXISTS abfrgvar_baugnhmgsverfhrn_bauratendatei_basis_id_index
    ON isidbuser.abfrgvar_baugnhmgsverfhrn (bauratendatei_basis_id);
CREATE INDEX IF NOT EXISTS abfrgvar_weitrs_vrfhrn_bauratendatei_basis_id_index
    ON isidbuser.abfrgvar_weitrs_vrfhrn (bauratendatei_basis_id);

-- Element-Collection-Tabellen (werden beim Laden der Eltern-Entität über den Fremdschlüssel gelesen)
CREATE INDEX IF NOT EXISTS baurate_foerderarten_baurate_id_index
    ON isidbuser.baurate_foerderarten (baurate_id);
CREATE INDEX IF NOT EXISTS foerdermix_stamm_foerderarten_foerdermix_stamm_id_index
    ON isidbuser.foerdermix_stamm_foerderarten (foerdermix_stamm_id);
CREATE INDEX IF NOT EXISTS abfrgvar_bauleitplnvrfhrn_foerderarten_abfrgvar_id_index
    ON isidbuser.abfrgvar_bauleitplnvrfhrn_foerderarten (abfrgvar_bauleitplnvrfhrn_id);
CREATE INDEX IF NOT EXISTS abfrgvar_weitrs_vrfhrn_foerderarten_abfrgvar_id_index
    ON isidbuser.abfrgvar_weitrs_vrfhrn_foerderarten (abfrgvar_weitrs_vrfhrn_id);
CREATE INDEX IF NOT EXISTS abfrgvar_bauleitplnvrfhrn_planart_abfrgvar_id_index
    ON isidbuser.abfrgvar_bauleitplnvrfhrn_planart (abfrgvar_bauleitplnvrfhrn_id);
CREATE INDEX IF NOT EXISTS abfrgvar_baugnhmgsverfhrn_wesentl_rechtsgrdl_abfrgvar_id_index
    ON isidbuser.abfrgvar_baugnhmgsverfhrn_wesentliche_rechtsgrundlage (abfrgvar_baugnhmgsverfhrn_id);
CREATE INDEX IF NOT EXISTS abfrgvar_weitrs_vrfhrn_wesentl_rechtsgrdl_abfrgvar_id_index
    ON isidbuser.abfrgvar_weitrs_vrfhrn_wesentliche_rechtsgrundlage (abfrgvar_weitrs_vrfhrn_id);
CREATE INDEX IF NOT EXISTS bauvorhaben_art_fnp_bauvorhaben_id_index
    ON isidbuser.bauvorhaben_art_fnp (bauvorhaben_id);
CREATE INDEX IF NOT EXISTS bauvorhaben_wesentliche_rechtsgrundlage_bauvorhaben_id_index
    ON isidbuser.bauvorhaben_wesentliche_rechtsgrundlage (bauvorhaben_id);

-- Doppelte Indexe: In V4 angelegt und mit V13 unter neuem Namen ein zweites Mal erstellt.
DROP INDEX IF EXISTS isidbuser.bedarfsmeldung_fachreferate_abfragevariante_baugenehmigungsverf;
DROP INDEX IF EXISTS isidbuser.bedarfsmeldung_fachreferate_abfragevariante_bauleitplanverfahre;
DROP INDEX IF EXISTS isidbuser.bedarfsmeldung_fachreferate_abfragevariante_weiteres_verfahren_;

-- Doppelte Indexe: identisch zu einem Unique-Constraint, der bereits einen Index mitbringt.
DROP INDEX IF EXISTS isidbuser.counter_type_index;
DROP INDEX IF EXISTS isidbuser.staedtebau_orientwrt_gltgab_frdrtbzchng_index;
DROP INDEX IF EXISTS isidbuser.versorgungsqoute_gruppenstaerke;

COMMIT;
