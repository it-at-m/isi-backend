--
-- Hinzufügen der Dokumente zu den Bedarfsmeldungen
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD COLUMN bedarfsmeldung_id character varying(36);

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD CONSTRAINT fk_bedarfsmeldung_id_bedarfsmeldung_id FOREIGN KEY (bedarfsmeldung_id)
    REFERENCES isidbuser.bedarfsmeldung (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION;

CREATE INDEX IF NOT EXISTS dokument_bedarfsmeldung_id_index
    ON isidbuser.dokument USING btree (bedarfsmeldung_id);

--
-- Größe der Anmerkung von 200 auf 500 erhöhen
--

ALTER TABLE IF EXISTS isidbuser.abfrgvar_baugnhmgsverfhrn
    ALTER COLUMN gf_anmerkung TYPE character varying(500),
	ALTER COLUMN we_anmerkung TYPE character varying(500);

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn
    ALTER COLUMN gf_anmerkung TYPE character varying(500),
	ALTER COLUMN we_anmerkung TYPE character varying(500);

ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn
    ALTER COLUMN gf_anmerkung TYPE character varying(500),
	ALTER COLUMN we_anmerkung TYPE character varying(500);

END;