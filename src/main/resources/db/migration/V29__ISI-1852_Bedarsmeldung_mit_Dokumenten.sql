BEGIN;

---
-- Hinzufügen der Dokumente zu den Bedarfsmeldungen
---

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD COLUMN bedarfsmeldung_id character varying(36);

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD CONSTRAINT fk_bedarfsmeldung_id_bedarfsmeldung_id FOREIGN KEY (bedarfsmeldung_id)
    REFERENCES isidbuser.bedarfsmeldung (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION;

CREATE INDEX IF NOT EXISTS dokument_bedarfsmeldung_id_index
    ON isidbuser.dokument USING btree (bedarfsmeldung_id);

---
--- Größe der Anmerkung auf 2000 erhöhen
---

--- Abfragen

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
ALTER COLUMN anmerkung TYPE character varying(2000);

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
ALTER COLUMN anmerkung TYPE character varying(2000);

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
ALTER COLUMN anmerkung TYPE character varying(2000);

--- Abfragevarianten

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn
ALTER COLUMN anmerkung TYPE character varying(2000),
    ALTER COLUMN anmerkung_fachreferate TYPE character varying(2000),
    ALTER COLUMN anmerkung_bauratendatei_input TYPE character varying(2000),
    ALTER COLUMN anmerkung_abfrageersteller TYPE character varying(2000);

ALTER TABLE IF EXISTS isidbuser.abfrgvar_baugnhmgsverfhrn
ALTER COLUMN anmerkung TYPE character varying(2000),
    ALTER COLUMN anmerkung_fachreferate TYPE character varying(2000),
    ALTER COLUMN anmerkung_bauratendatei_input TYPE character varying(2000),
    ALTER COLUMN anmerkung_abfrageersteller TYPE character varying(2000);

ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn
ALTER COLUMN anmerkung TYPE character varying(2000),
    ALTER COLUMN anmerkung_fachreferate TYPE character varying(2000),
    ALTER COLUMN anmerkung_bauratendatei_input TYPE character varying(2000),
    ALTER COLUMN anmerkung_abfrageersteller TYPE character varying(2000);

END;