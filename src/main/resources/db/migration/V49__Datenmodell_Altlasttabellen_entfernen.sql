BEGIN;

--
-- Entfernen der Tabellen des Datenmodells vor Version 1.1 (aus der Baseline V3).
-- Sie werden von keiner Entity und keinem ETL-Job mehr genutzt. Pentaho liest gleichnamige Tabellen
-- ausschließlich aus der Reporting-Datenbank.
--
-- abfragevariante referenziert infrastrukturabfrage und wird daher zuerst entfernt.
--

DROP TABLE IF EXISTS isidbuser.abfragevariante;
DROP TABLE IF EXISTS isidbuser.infrastrukturabfrage;

COMMIT;
