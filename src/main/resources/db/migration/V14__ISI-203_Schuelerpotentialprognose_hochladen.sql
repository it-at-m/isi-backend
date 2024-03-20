--
-- Hinzufügen der Dokumente zu jeweiligen Abfragevariante
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD COLUMN abfrgvar_baugnhmgsverfhrn_id character varying(36),
    ADD COLUMN abfrgvar_bauleitplnvrfhrn_id character varying(36),
    ADD COLUMN abfrgvar_weitrs_vrfhrn_id character varying(36);

-- Syntax fk_childtablecolumn_parentable_column example in table baugebiet column bauabschnitt_id references to parentable bauabschnitt with the column id. So result fk_bauabschnitt_id_bauabschnitt_id

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD CONSTRAINT fk_abfrgvar_baugnhmgsverfhrn_id_abfrgvar_baugnhmgsverfhrn_id FOREIGN KEY (abfrgvar_baugnhmgsverfhrn_id)
    REFERENCES isidbuser.abfrgvar_baugnhmgsverfhrn (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD CONSTRAINT fk_abfrgvar_bauleitplnvrfhrn_id_abfrgvar_bauleitplnvrfhrn_id FOREIGN KEY (abfrgvar_bauleitplnvrfhrn_id)
    REFERENCES isidbuser.abfrgvar_bauleitplnvrfhrn (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD CONSTRAINT fk_abfrgvar_weitrs_vrfhrn_id_abfrgvar_weitrs_vrfhrn_id FOREIGN KEY (abfrgvar_weitrs_vrfhrn_id)
    REFERENCES isidbuser.abfrgvar_weitrs_vrfhrn (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


CREATE INDEX IF NOT EXISTS dokument_abfrgvar_baugnhmgsverfhrn_id_index
    ON isidbuser.dokument USING btree (abfrgvar_baugnhmgsverfhrn_id);

CREATE INDEX IF NOT EXISTS dokument_abfrgvar_bauleitplnvrfhrn_id_index
    ON isidbuser.dokument USING btree (abfrgvar_bauleitplnvrfhrn_id);

CREATE INDEX IF NOT EXISTS dokument_abfrgvar_weitrs_vrfhrn_id_index
    ON isidbuser.dokument USING btree (abfrgvar_weitrs_vrfhrn_id);

END;
