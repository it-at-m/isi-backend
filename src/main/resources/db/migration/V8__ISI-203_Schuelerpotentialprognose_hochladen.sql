--
-- Hinzufügen der Dokumente zu jeweiligen Abfragevariante
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD COLUMN abfragevariante_baugenehmigungsverfahren_id character varying(36),
    ADD COLUMN abfragevariante_bauleitplanverfahren_id character varying(36),
    ADD COLUMN abfragevariante_weiteres_verfahren_id character varying(36);

END;
