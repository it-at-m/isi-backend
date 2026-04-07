BEGIN;

---
-- Hinzufügen der Dokumente zu den Bedarfsmeldungen für Fachreferate und Abfrageersteller
---

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD COLUMN bedmeld_fachref_abfrgvar_baugnhmgsverfhrn_id character varying(36),
    ADD COLUMN bedmeld_fachref_abfrgvar_bauleitplnvrfhrn_id character varying(36),
    ADD COLUMN bedmeld_fachref_abfrgvar_weitrs_vrfhrn_id character varying(36),

    ADD COLUMN bedmeld_abfrerst_abfrgvar_baugnhmgsverfhrn_id character varying(36),
    ADD COLUMN bedmeld_abfrerst_abfrgvar_bauleitplnvrfhrn_id character varying(36),
    ADD COLUMN bedmeld_abfrerst_abfrgvar_weitrs_vrfhrn_id character varying(36);

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD CONSTRAINT fk_bedmeld_fachref_abfrgvar_baugnhmgsverfhrn_id FOREIGN KEY (bedmeld_fachref_abfrgvar_baugnhmgsverfhrn_id)
    REFERENCES isidbuser.abfrgvar_baugnhmgsverfhrn (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION,

    ADD CONSTRAINT fk_bedmeld_fachref_abfrgvar_bauleitplnvrfhrn_id FOREIGN KEY (bedmeld_fachref_abfrgvar_bauleitplnvrfhrn_id)
    REFERENCES isidbuser.abfrgvar_bauleitplnvrfhrn (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION,

    ADD CONSTRAINT fk_bedmeld_fachref_abfrgvar_weitrs_vrfhrn_id FOREIGN KEY (bedmeld_fachref_abfrgvar_weitrs_vrfhrn_id)
    REFERENCES isidbuser.abfrgvar_weitrs_vrfhrn (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION,

    ADD CONSTRAINT fk_bedmeld_abfrerst_abfrgvar_baugnhmgsverfhrn_id FOREIGN KEY (bedmeld_abfrerst_abfrgvar_baugnhmgsverfhrn_id)
    REFERENCES isidbuser.abfrgvar_baugnhmgsverfhrn (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION,

    ADD CONSTRAINT fk_bedmeld_abfrerst_abfrgvar_bauleitplnvrfhrn_id FOREIGN KEY (bedmeld_abfrerst_abfrgvar_bauleitplnvrfhrn_id)
    REFERENCES isidbuser.abfrgvar_bauleitplnvrfhrn (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION,

    ADD CONSTRAINT fk_bedmeld_abfrerst_abfrgvar_weitrs_vrfhrn_id FOREIGN KEY (bedmeld_abfrerst_abfrgvar_weitrs_vrfhrn_id)
    REFERENCES isidbuser.abfrgvar_weitrs_vrfhrn (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION;


CREATE INDEX IF NOT EXISTS dokument_bedmeld_fachref_abfrgvar_baugnhmgsverfhrn_id_index
    ON isidbuser.dokument USING btree (bedmeld_fachref_abfrgvar_baugnhmgsverfhrn_id);

CREATE INDEX IF NOT EXISTS dokument_bedmeld_fachref_abfrgvar_bauleitplnvrfhrn_id_index
    ON isidbuser.dokument USING btree (bedmeld_fachref_abfrgvar_bauleitplnvrfhrn_id);

CREATE INDEX IF NOT EXISTS dokument_bedmeld_fachref_abfrgvar_weitrs_vrfhrn_id_index
    ON isidbuser.dokument USING btree (bedmeld_fachref_abfrgvar_weitrs_vrfhrn_id);

CREATE INDEX IF NOT EXISTS dokument_bedmeld_abfrerst_abfrgvar_baugnhmgsverfhrn_id_index
    ON isidbuser.dokument USING btree (bedmeld_abfrerst_abfrgvar_baugnhmgsverfhrn_id);

CREATE INDEX IF NOT EXISTS dokument_bedmeld_abfrerst_abfrgvar_bauleitplnvrfhrn_id_index
    ON isidbuser.dokument USING btree (bedmeld_abfrerst_abfrgvar_bauleitplnvrfhrn_id);

CREATE INDEX IF NOT EXISTS dokument_bedmeld_abfrerst_abfrgvar_weitrs_vrfhrn_id_index
    ON isidbuser.dokument USING btree (bedmeld_abfrerst_abfrgvar_weitrs_vrfhrn_id);

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