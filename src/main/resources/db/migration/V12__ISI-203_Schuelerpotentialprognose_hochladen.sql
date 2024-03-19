--
-- Hinzufügen der Dokumente zu jeweiligen Abfragevariante
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD COLUMN abfragevariante_baugenehmigungsverfahren_id character varying(36),
    ADD COLUMN abfragevariante_bauleitplanverfahren_id character varying(36),
    ADD COLUMN abfragevariante_weiteres_verfahren_id character varying(36);

-- Syntax fk_childtablecolumn_parentable_column example in table baugebiet column bauabschnitt_id references to parentable bauabschnitt with the column id. So result fk_bauabschnitt_id_bauabschnitt_id

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD CONSTRAINT fk_abfrgvar_baugnhmgsverfhrn_id_abfrgvar_baugnhmgsverfhrn_id FOREIGN KEY (abfragevariante_baugenehmigungsverfahren_id)
    REFERENCES isidbuser.abfragevariante_baugenehmigungsverfahren (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD CONSTRAINT fk_abfrgvar_bauleitplnvrfhrn_id_abfrgvar_bauleitplnvrfhrn_id FOREIGN KEY (abfragevariante_bauleitplanverfahren_id)
    REFERENCES isidbuser.abfragevariante_bauleitplanverfahren (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

ALTER TABLE IF EXISTS isidbuser.dokument
    ADD CONSTRAINT fk_abfrgvar_weitrs_vrfhrn_id_abfrgvar_weitrs_vrfhrn_id FOREIGN KEY (abfragevariante_weiteres_verfahren_id)
    REFERENCES isidbuser.abfragevariante_weiteres_verfahren (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


CREATE INDEX IF NOT EXISTS dokument_abfragevariante_baugenehmigungsverfahren_id_index
    ON isidbuser.dokument USING btree (abfragevariante_baugenehmigungsverfahren_id);

CREATE INDEX IF NOT EXISTS dokument_abfragevariante_bauleitplanverfahren_id_index
    ON isidbuser.dokument USING btree (abfragevariante_bauleitplanverfahren_id);

CREATE INDEX IF NOT EXISTS dokument_abfragevariante_weiteres_verfahren_id_index
    ON isidbuser.dokument USING btree (abfragevariante_weiteres_verfahren_id);

END;
