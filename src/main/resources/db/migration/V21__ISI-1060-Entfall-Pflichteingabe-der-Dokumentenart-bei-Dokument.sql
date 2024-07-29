--
-- Entfall der Pflichteingabe der Dokumentenart beim Dokument.
--

BEGIN;

ALTER TABLE isidbuser.dokument
    ALTER COLUMN art_dokument DROP NOT NULL;

END;