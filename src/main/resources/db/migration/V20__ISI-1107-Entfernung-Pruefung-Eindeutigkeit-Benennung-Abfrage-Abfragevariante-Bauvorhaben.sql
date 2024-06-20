--
-- Entfernen der Prüfung auf Eindeutigkeit bei Benennung von Bauvorhaben, Abfragen und Abfragevarianten
--

BEGIN;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    DROP CONSTRAINT IF EXISTS bauvorhaben_name_vorhaben_key;

ALTER TABLE ONLY isidbuser.abfrgvar_weitrs_vrfhrn
    DROP CONSTRAINT IF EXISTS uniquenameabfragevarianteperweiteresverfahren;

ALTER TABLE ONLY isidbuser.abfrgvar_bauleitplnvrfhrn
    DROP CONSTRAINT IF EXISTS uniquenameabfragevarianteperbauleitplanverfahren;

ALTER TABLE ONLY isidbuser.abfrgvar_baugnhmgsverfhrn
    DROP CONSTRAINT IF EXISTS uniquenameabfragevarianteperbaugenehmigungsverfahren;

ALTER TABLE ONLY isidbuser.baugenehmigungsverfahren
    DROP CONSTRAINT IF EXISTS baugenehmigungsverfahren_name_key;

ALTER TABLE ONLY isidbuser.bauleitplanverfahren
    DROP CONSTRAINT IF EXISTS bauleitplanverfahren_name_key;

ALTER TABLE ONLY isidbuser.weiteres_verfahren
    DROP CONSTRAINT IF EXISTS weiteres_verfahren_name_key;

END;