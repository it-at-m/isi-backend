--
-- Hinzufügen des Feldes VersorgungsquoteHortSobon in Tabelle
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn
    ADD COLUMN versorgungsquote_hort_sobon character varying(100);

ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn
    ADD COLUMN versorgungsquote_hort_sobon character varying(100);

END;