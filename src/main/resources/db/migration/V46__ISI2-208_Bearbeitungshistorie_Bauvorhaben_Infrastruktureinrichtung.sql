BEGIN;

--
-- Die Bearbeitungshistorie hält fest, wer eine Abfrage, ein Bauvorhaben oder eine Infrastruktureinrichtung
-- erstellt oder bearbeitet hat. Bisherige Einträge der Abfragen sind Statusänderungen, der erste Eintrag im
-- Status ANGELEGT ist die Erstellung.
--

ALTER TABLE isidbuser.abfrage_bearbeitungshistorie ADD COLUMN art character varying(255);

UPDATE isidbuser.abfrage_bearbeitungshistorie SET art = 'STATUSAENDERUNG';

UPDATE isidbuser.abfrage_bearbeitungshistorie historie
SET art = 'ERSTELLT'
WHERE historie.ziel_status = 'ANGELEGT'
  AND historie.zeitpunkt = (SELECT MIN(erster.zeitpunkt)
                            FROM isidbuser.abfrage_bearbeitungshistorie erster
                            WHERE erster.abfrage_id = historie.abfrage_id);

CREATE TABLE isidbuser.bauvorhaben_bearbeitungshistorie
(
    bauvorhaben_id                           character varying(36) NOT NULL REFERENCES isidbuser.bauvorhaben (id),
    art                                      character varying(255),
    ziel_status                              character varying(255),
    zeitpunkt                                timestamp without time zone,
    bearbeitende_person_email                character varying(255),
    bearbeitende_person_name                 character varying(255),
    bearbeitende_person_organisationseinheit character varying(255)
);

CREATE INDEX bauvorhaben_bearbeitungshistorie_bauvorhaben_id_index
    ON isidbuser.bauvorhaben_bearbeitungshistorie USING btree (bauvorhaben_id);

CREATE TABLE isidbuser.infrastruktureinrichtung_bearbeitungshistorie
(
    infrastruktureinrichtung_id              character varying(36) NOT NULL REFERENCES isidbuser.infrastruktureinrichtung (id),
    art                                      character varying(255),
    ziel_status                              character varying(255),
    zeitpunkt                                timestamp without time zone,
    bearbeitende_person_email                character varying(255),
    bearbeitende_person_name                 character varying(255),
    bearbeitende_person_organisationseinheit character varying(255)
);

CREATE INDEX infrastruktureinrichtung_bearbeitungshistorie_id_index
    ON isidbuser.infrastruktureinrichtung_bearbeitungshistorie USING btree (infrastruktureinrichtung_id);

--
-- Von Bestandsdaten ist nur die zuletzt bearbeitende Person bekannt.
--

INSERT INTO isidbuser.bauvorhaben_bearbeitungshistorie
(bauvorhaben_id, art, zeitpunkt, bearbeitende_person_email, bearbeitende_person_name, bearbeitende_person_organisationseinheit)
SELECT id, 'AKTUALISIERT', last_modified_date_time, bearbeitende_person_email, bearbeitende_person_name, bearbeitende_person_organisationseinheit
FROM isidbuser.bauvorhaben
WHERE bearbeitende_person_email IS NOT NULL
   OR bearbeitende_person_name IS NOT NULL;

INSERT INTO isidbuser.infrastruktureinrichtung_bearbeitungshistorie
(infrastruktureinrichtung_id, art, zeitpunkt, bearbeitende_person_email, bearbeitende_person_name, bearbeitende_person_organisationseinheit)
SELECT id, 'AKTUALISIERT', last_modified_date_time, bearbeitende_person_email, bearbeitende_person_name, bearbeitende_person_organisationseinheit
FROM isidbuser.infrastruktureinrichtung
WHERE bearbeitende_person_email IS NOT NULL
   OR bearbeitende_person_name IS NOT NULL;

COMMIT;
