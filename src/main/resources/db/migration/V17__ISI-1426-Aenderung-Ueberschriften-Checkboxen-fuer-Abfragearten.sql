--
-- Für die Bedarfsmeldung wurde ein zusätzliches Anmerkungsfeld hinzugefügt und das anderen umbenannt.
--

BEGIN;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_baugnhmgsverfhrn
    RENAME COLUMN hinweis_versorgung TO anmerkung_fachreferate;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_baugnhmgsverfhrn
    ADD COLUMN anmerkung_abfrageersteller character varying(1000);


ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn
    RENAME COLUMN hinweis_versorgung TO anmerkung_fachreferate;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn
    ADD COLUMN anmerkung_abfrageersteller character varying(1000);


ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn
    RENAME COLUMN hinweis_versorgung TO anmerkung_fachreferate;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn
    ADD COLUMN anmerkung_abfrageersteller character varying(1000);

END;