--
-- Hinzufügen der Spalte Jahr Bezeichnung
--

BEGIN;

ALTER TABLE IF EXISTS isidbuser.sobon_orientierungswert_soziale_infrastruktur
    ADD COLUMN jahr_bezeichnung character varying(200);

END;