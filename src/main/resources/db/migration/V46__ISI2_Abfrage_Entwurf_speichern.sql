BEGIN;

--
-- Abfragen im Status ANGELEGT können als Entwurf ohne vollständige Pflichtangaben gespeichert werden.
-- Die Pflichtangaben werden erst bei der Freigabe geprüft (Validierungsgruppe Vollstaendig).
--
-- Dafür darf 'Realisierung von' einer Abfragevariante leer sein. Die Regel, dass bei einem Bauleitplanverfahren
-- entweder 'Start 4.2-Verfahren' angegeben oder 'Datum unbekannt / nicht zutreffend' ausgewählt ist, wird ebenfalls
-- erst bei der Freigabe geprüft (Start42VerfahrenValid).
--

ALTER TABLE isidbuser.abfragevariante_bauleitplanverfahren
    ALTER COLUMN realisierung_von DROP NOT NULL;

ALTER TABLE isidbuser.abfragevariante_baugenehmigungsverfahren
    ALTER COLUMN realisierung_von DROP NOT NULL;

ALTER TABLE isidbuser.abfragevariante_weiteres_verfahren
    ALTER COLUMN realisierung_von DROP NOT NULL;

ALTER TABLE isidbuser.bauleitplanverfahren
    DROP CONSTRAINT bauleitplanverfahren_start_42_verfahren_xor_check;

COMMIT;
