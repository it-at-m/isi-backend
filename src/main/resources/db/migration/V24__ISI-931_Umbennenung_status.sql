--
-- Abändern der Statusbennenungen in den Tabellen
--
UPDATE isidbuser.bauleitplanverfahren
SET status_abfrage = CASE
WHEN status_abfrage = 'OFFEN' THEN 'UEBERMITTELT_ZUR_BEARBEITUNG'
WHEN status_abfrage = 'IN_BEARBEITUNG_SACHBEARBEITUNG' THEN 'START_BEARBEITUNG'
WHEN status_abfrage = 'IN_BEARBEITUNG_FACHREFERATE' THEN 'EINPFLEGEN_BEDARFSMELDUNG'
WHEN status_abfrage = 'BEDARFSMELDUNG_ERFOLGT' THEN 'EINPLANUNG_BEDARFE'
ELSE status_abfrage -- Falls ein Wert nicht gemappt wird, bleibt er unverändert
END;


UPDATE isidbuser.baugenehmigungsverfahren
SET status_abfrage = CASE
WHEN status_abfrage = 'OFFEN' THEN 'UEBERMITTELT_ZUR_BEARBEITUNG'
WHEN status_abfrage = 'IN_BEARBEITUNG_SACHBEARBEITUNG' THEN 'START_BEARBEITUNG'
WHEN status_abfrage = 'IN_BEARBEITUNG_FACHREFERATE' THEN 'EINPFLEGEN_BEDARFSMELDUNG'
WHEN status_abfrage = 'BEDARFSMELDUNG_ERFOLGT' THEN 'EINPLANUNG_BEDARFE'
ELSE status_abfrage
END;

UPDATE isidbuser.weiteres_verfahren
SET status_abfrage = CASE
WHEN status_abfrage = 'OFFEN' THEN 'UEBERMITTELT_ZUR_BEARBEITUNG'
WHEN status_abfrage = 'IN_BEARBEITUNG_SACHBEARBEITUNG' THEN 'START_BEARBEITUNG'
WHEN status_abfrage = 'IN_BEARBEITUNG_FACHREFERATE' THEN 'EINPFLEGEN_BEDARFSMELDUNG'
WHEN status_abfrage = 'BEDARFSMELDUNG_ERFOLGT' THEN 'EINPLANUNG_BEDARFE'
ELSE status_abfrage
END;

