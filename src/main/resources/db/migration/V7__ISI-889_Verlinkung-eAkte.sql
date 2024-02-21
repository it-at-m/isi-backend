--
-- Neues Feld "eAkte" bei der Abfrage (ISI-889).
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    ADD COLUMN link_eakte;

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
    ADD COLUMN link_eakte;

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    ADD COLUMN link_eakte;

END;
