--
-- Abändern der Statusbennenungen in den Tabellen und des Constraints
--

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    DROP CONSTRAINT bauleitplanverfahren_status_abfrage_check;

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    DROP CONSTRAINT weiteres_verfahren_status_abfrage_check;

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
    DROP CONSTRAINT baugenehmigungsverfahren_status_abfrage_check;

ALTER TABLE IF EXISTS isidbusers.abfrage_bearbeitungshistorie
    DROP CONSTRAINT abfrage_bearbeitungshistorie_ziel_status_check;

UPDATE isidbuser.bauleitplanverfahren
SET status_abfrage = CASE
    WHEN status_abfrage = 'OFFEN' THEN 'UEBERMITTELT_ZUR_BEARBEITUNG'
    WHEN status_abfrage = 'IN_BEARBEITUNG_SACHBEARBEITUNG' THEN 'START_BEARBEITUNG'
    WHEN status_abfrage = 'IN_BEARBEITUNG_FACHREFERATE' THEN 'EINPFLEGEN_BEDARFSMELDUNG'
    WHEN status_abfrage = 'BEDARFSMELDUNG_ERFOLGT' THEN 'EINPLANUNG_BEDARFE'
    ELSE status_abfrage -- Falls ein Wert nicht gemappt wird, bleibt er unverändert
END;


UPDATE isidbuser.weiteres_verfahren
SET status_abfrage = CASE
    WHEN status_abfrage = 'OFFEN' THEN 'UEBERMITTELT_ZUR_BEARBEITUNG'
    WHEN status_abfrage = 'IN_BEARBEITUNG_SACHBEARBEITUNG' THEN 'START_BEARBEITUNG'
    WHEN status_abfrage = 'IN_BEARBEITUNG_FACHREFERATE' THEN 'EINPFLEGEN_BEDARFSMELDUNG'
    WHEN status_abfrage = 'BEDARFSMELDUNG_ERFOLGT' THEN 'EINPLANUNG_BEDARFE'
    ELSE status_abfrage
END;

UPDATE isidbuser.baugenehmigungsverfahren
SET status_abfrage = CASE
    WHEN status_abfrage = 'OFFEN' THEN 'UEBERMITTELT_ZUR_BEARBEITUNG'
    WHEN status_abfrage = 'IN_BEARBEITUNG_SACHBEARBEITUNG' THEN 'START_BEARBEITUNG'
    WHEN status_abfrage = 'IN_BEARBEITUNG_FACHREFERATE' THEN 'EINPFLEGEN_BEDARFSMELDUNG'
    WHEN status_abfrage = 'BEDARFSMELDUNG_ERFOLGT' THEN 'EINPLANUNG_BEDARFE'
    ELSE status_abfrage
END;

UPDATE isidbuser.abfrage_bearbeitungshistorie
SET ziel_status = CASE
    WHEN ziel_status = 'OFFEN' THEN 'UEBERMITTELT_ZUR_BEARBEITUNG'
    WHEN ziel_status = 'IN_BEARBEITUNG_SACHBEARBEITUNG' THEN 'START_BEARBEITUNG'
    WHEN ziel_status = 'IN_BEARBEITUNG_FACHREFERATE' THEN 'EINPFLEGEN_BEDARFSMELDUNG'
    WHEN ziel_status = 'BEDARFSMELDUNG_ERFOLGT' THEN 'EINPLANUNG_BEDARFE'
    ELSE ziel_status
END;

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    ADD CONSTRAINT bauleitplanverfahren_status_abfrage_check CHECK (status_abfrage::text = ANY (ARRAY['ANGELEGT'::character varying, 'UEBERMITTELT_ZUR_BEARBEITUNG'::character varying, 'START_BEARBEITUNG'::character varying, 'EINPFLEGEN_BEDARFSMELDUNG'::character varying, 'EINPLANUNG_BEDARFE'::character varying, 'ERLEDIGT_MIT_FACHREFERAT'::character varying, 'ERLEDIGT_OHNE_FACHREFERAT'::character varying, 'ABBRUCH'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    ADD CONSTRAINT weiteres_verfahren_status_abfrage_check CHECK (status_abfrage::text = ANY (ARRAY['ANGELEGT'::character varying, 'UEBERMITTELT_ZUR_BEARBEITUNG'::character varying, 'START_BEARBEITUNG'::character varying, 'EINPFLEGEN_BEDARFSMELDUNG'::character varying, 'EINPLANUNG_BEDARFE'::character varying, 'ERLEDIGT_MIT_FACHREFERAT'::character varying, 'ERLEDIGT_OHNE_FACHREFERAT'::character varying, 'ABBRUCH'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
    ADD CONSTRAINT baugenehmigungsverfahren_status_abfrage_check CHECK (status_abfrage::text = ANY (ARRAY['ANGELEGT'::character varying, 'UEBERMITTELT_ZUR_BEARBEITUNG'::character varying, 'START_BEARBEITUNG'::character varying, 'EINPFLEGEN_BEDARFSMELDUNG'::character varying, 'EINPLANUNG_BEDARFE'::character varying, 'ERLEDIGT_MIT_FACHREFERAT'::character varying, 'ERLEDIGT_OHNE_FACHREFERAT'::character varying, 'ABBRUCH'::character varying]::text[]));

ALTER TABLE IF EXISTS isidbuser.abfrage_bearbeitungshistorie
    ADD CONSTRAINT abfrage_bearbeitungshistorie_ziel_status_check CHECK ziel_status::text = ANY (ARRAY['ANGELEGT'::character varying, 'UEBERMITTELT_ZUR_BEARBEITUNG'::character varying, 'START_BEARBEITUNG'::character varying, 'EINPFLEGEN_BEDARFSMELDUNG'::character varying, 'EINPLANUNG_BEDARFE'::character varying, 'ERLEDIGT_MIT_FACHREFERAT'::character varying, 'ERLEDIGT_OHNE_FACHREFERAT'::character varying, 'ABBRUCH'::character varying]::text[]);

