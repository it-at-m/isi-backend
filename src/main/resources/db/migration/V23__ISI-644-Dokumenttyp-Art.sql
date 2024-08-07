--
-- Entfernen der Attribute size_in_bytes und typ_dokument für die Tabelle Dokument
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.dokument
DROP COLUMN size_in_bytes;

ALTER TABLE IF EXISTS isidbuser.dokument
DROP COLUMN typ_dokument;

END;