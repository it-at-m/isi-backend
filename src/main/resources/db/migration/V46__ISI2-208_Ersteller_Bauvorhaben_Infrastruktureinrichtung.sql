BEGIN;

--
-- Ersteller (Keycloak-sub) von Bauvorhaben und Infrastruktureinrichtungen analog zur Abfrage.
-- Für Bestandsdaten ist der Ersteller nicht bekannt, die Spalte ist daher nullable.
--

ALTER TABLE isidbuser.bauvorhaben ADD COLUMN sub character varying(255);

ALTER TABLE isidbuser.infrastruktureinrichtung ADD COLUMN sub character varying(255);

COMMIT;
