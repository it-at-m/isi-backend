--
-- Hinzufügen der Tabellen für die Informationen zur Erstellung der Bauratendatei
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.abfragevariante_baugenehmigungsverfahren
ADD COLUMN gf_anmerkung character varying(200), ADD COLUMN we_anmerkung character varying(200);

ALTER TABLE IF EXISTS isidbuser.abfragevariante_bauleitplanverfahren
ADD COLUMN gf_anmerkung character varying(200), ADD COLUMN we_anmerkung character varying(200);

ALTER TABLE IF EXISTS isidbuser.abfragevariante_weiteres_verfahren
ADD COLUMN gf_anmerkung character varying(200), ADD COLUMN we_anmerkung character varying(200);

END;