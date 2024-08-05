--
-- Umbenennung des Feldes offizielleMitzeichnung zu mitzeichnungBeschlussentwurf
--
BEGIN;

ALTER TABLE isidbuser.bauleitplanverfahren
    RENAME COLUMN offizielle_mitzeichnung TO mitzeichnung_beschlussentwurf;

ALTER TABLE isidbuser.weiteres_verfahren
    RENAME COLUMN offizielle_mitzeichnung TO mitzeichnung_beschlussentwurf;

END;