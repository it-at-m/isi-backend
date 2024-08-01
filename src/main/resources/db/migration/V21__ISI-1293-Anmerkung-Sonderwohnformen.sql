--
-- Hinzufügen der Tabellen für die Informationen zur Erstellung der Bauratendatei
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_baugnhmgsverfhrn
ADD COLUMN gf_anmerkung character varying(200), ADD COLUMN we_anmerkung character varying(200);

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn
ADD COLUMN gf_anmerkung character varying(200), ADD COLUMN we_anmerkung character varying(200);

ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn
ADD COLUMN gf_anmerkung character varying(200), ADD COLUMN we_anmerkung character varying(200);

END;