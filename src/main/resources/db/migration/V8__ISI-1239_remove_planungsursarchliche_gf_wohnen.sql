--
-- Entfernung des Attributes für die planungsursächliche Geschossfläche Wohnen
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.abfragevariante_baugenehmigungsverfahren
    DROP COLUMN gf_wohnen_planungsursaechlich;

ALTER TABLE IF EXISTS isidbuser.abfragevariante_bauleitplanverfahren
    DROP COLUMN gf_wohnen_planungsursaechlich;

ALTER TABLE IF EXISTS isidbuser.abfragevariante_weiteres_verfahren
    DROP COLUMN gf_wohnen_planungsursaechlich;

END;