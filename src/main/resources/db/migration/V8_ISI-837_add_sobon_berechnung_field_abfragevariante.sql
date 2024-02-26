--
-- Neue Felder "isASobonBerechnung" und "sobonFoerdermix" für die abfragevariante entsprechend ISI-837.
-- Betrifft Bauleitplanverfahren und Weiteres Verfahren.
--
BEGIN;

ALTER TABLE isidbuser.abfragevariante_bauleitplanverfahren
    ADD COLUMN is_a_sobon_berechnung BOOLEAN,
ADD COLUMN sobon_foerdermix_bezeichnung_jahr VARCHAR(255) NOT NULL,
ADD COLUMN sobon_foerdermix_bezeichnung VARCHAR(80) NOT NULL;

ALTER TABLE isidbuser.abfragevariante_weiteres_verfahren
    ADD COLUMN is_a_sobon_berechnung BOOLEAN,
ADD COLUMN sobon_foerdermix_bezeichnung_jahr VARCHAR(255) NOT NULL,
ADD COLUMN sobon_foerdermix_bezeichnung VARCHAR(80) NOT NULL;

END;