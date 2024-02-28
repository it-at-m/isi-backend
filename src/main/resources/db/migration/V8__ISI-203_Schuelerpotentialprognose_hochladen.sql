--
-- Hinzufügen der Dokumente zu jeweiligen Abfragevariante
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD COLUMN abfragevariante_baugenehmigungsverfahren_id;

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD COLUMN abfragevariante_bauleitplanverfahren_id;

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD COLUMN abfragevariante_weiteres_verfahren_id;


END;
