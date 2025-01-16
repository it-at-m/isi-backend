--
-- Änderung des Feldes Jahr für SoBoN-Orientierungswerte (Planungs-ursaechlich) und hinzufügen des Feldes Jahr für SoBoN-Orientierungswerte (SoBoN-ursaechlich)
--
BEGIN;

ALTER TABLE isidbuser.abfrgvar_bauleitplnvrfhrn
    RENAME COLUMN sobon_orientierungswert_jahr TO sobon_orientierungswert_jahr_planungsursaechlich;

ALTER TABLE isidbuser.abfrgvar_bauleitplnvrfhrn
    ADD COLUMN sobon_orientierungswert_jahr_sobon_ursaechlich character varying(255);

ALTER TABLE isidbuser.abfrgvar_weitrs_vrfhrn
    RENAME COLUMN sobon_orientierungswert_jahr TO sobon_orientierungswert_jahr_planungsursaechlich;

ALTER TABLE isidbuser.abfrgvar_weitrs_vrfhrn
    ADD COLUMN sobon_orientierungswert_jahr_sobon_ursaechlich character varying(255);

ALTER TABLE isidbuser.abfrgvar_baugnhmgsverfhrn
    RENAME COLUMN sobon_orientierungswert_jahr TO sobon_orientierungswert_jahr_planungsursaechlich;

END;