--
-- Neue Felder "isASobonBerechnung" und "sobonFoerdermix" für die abfragevariante entsprechend ISI-837.
-- Betrifft Bauleitplanverfahren und Weiteres Verfahren.
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.abfragevariante_bauleitplanverfahren
    ADD COLUMN is_a_sobon_berechnung             BOOLEAN,
    ADD COLUMN sobon_foerdermix_bezeichnung_jahr VARCHAR(255),
    ADD COLUMN sobon_foerdermix_bezeichnung      VARCHAR(80);

ALTER TABLE IF EXISTS isidbuser.abfragevariante_weiteres_verfahren
    ADD COLUMN is_a_sobon_berechnung             BOOLEAN,
    ADD COLUMN sobon_foerdermix_bezeichnung_jahr VARCHAR(255),
    ADD COLUMN sobon_foerdermix_bezeichnung      VARCHAR(80);

END;
