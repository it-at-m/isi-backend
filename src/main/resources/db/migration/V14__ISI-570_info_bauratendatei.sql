--
-- Hinzufügen der Tabellen für die Informationen zur Erstellung der Bauratendatei
--
BEGIN;

CREATE TABLE IF NOT EXISTS isidbuser.bauratendatei_input (
   id character varying(36) COLLATE pg_catalog."default" NOT NULL,
   created_date_time timestamp without time zone,
   last_modified_date_time timestamp without time zone,
   version bigint,
   grundschulsprengel jsonb,
   mittelschulsprengel jsonb,
   viertel jsonb,
   abfrgvar_baugnhmgsverfhrn_id character varying(36) COLLATE pg_catalog."default",
   abfrgvar_bauleitplnvrfhrn_id character varying(36) COLLATE pg_catalog."default",
   abfrgvar_weitrs_vrfhrn_id character varying(36) COLLATE pg_catalog."default",
   CONSTRAINT bauratendatei_input_pkey PRIMARY KEY (id),
   CONSTRAINT abfrgvar_baugnhmgsverfhrn_id_fk FOREIGN KEY (abfrgvar_baugnhmgsverfhrn_id) REFERENCES isidbuser.abfrgvar_baugnhmgsverfhrn(id),
   CONSTRAINT abfrgvar_bauleitplnvrfhrn_id_fk FOREIGN KEY (abfrgvar_bauleitplnvrfhrn_id) REFERENCES isidbuser.abfrgvar_bauleitplnvrfhrn(id),
   CONSTRAINT abfrgvar_weitrs_vrfhrn_id_fk FOREIGN KEY (abfrgvar_weitrs_vrfhrn_id) REFERENCES isidbuser.abfrgvar_bauleitplnvrfhrn(id)
);

CREATE INDEX IF NOT EXISTS bauratendatei_input_abfrgvar_baugnhmgsverfhrn_id
    ON isidbuser.bauratendatei_input USING btree
        (abfrgvar_baugnhmgsverfhrn_id COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS bauratendatei_input_abfrgvar_bauleitplnvrfhrn_id
    ON isidbuser.bauratendatei_input USING btree
        (abfrgvar_bauleitplnvrfhrn_id COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS bauratendatei_input_abfrgvar_weitrs_vrfhrn_id
    ON isidbuser.bauratendatei_input USING btree
        (abfrgvar_weitrs_vrfhrn_id COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS isidbuser.bauratendatei_input_wohneinheiten (
    bauratendatei_input_id character varying(36) COLLATE pg_catalog."default" NOT NULL,
    wohneinheiten numeric(30,15),
    foerderart character varying(255),
    jahr character varying(255),
    CONSTRAINT bauratendatei_input_id_fk FOREIGN KEY (bauratendatei_input_id) REFERENCES isidbuser.bauratendatei_input(id)
);

CREATE INDEX bauratendatei_input_wohneinheiten_id ON isidbuser.bauratendatei_input_wohneinheiten USING btree (bauratendatei_input_id);

ALTER TABLE IF EXISTS isidbuser.abfrgvar_baugnhmgsverfhrn
    ADD COLUMN has_bauratendatei_input             boolean,
    ADD COLUMN anmerkung_bauratendatei_input character varying(1000) COLLATE pg_catalog."default",
    ADD COLUMN bauratendatei_basis_id      character varying(36) COLLATE pg_catalog."default",
    ADD CONSTRAINT bauratendatei_basis_id_fk FOREIGN KEY (bauratendatei_basis_id) REFERENCES isidbuser.bauratendatei_input(id);

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn
    ADD COLUMN has_bauratendatei_input             boolean,
    ADD COLUMN anmerkung_bauratendatei_input character varying(1000) COLLATE pg_catalog."default",
    ADD COLUMN bauratendatei_basis_id      character varying(36) COLLATE pg_catalog."default",
    ADD CONSTRAINT bauratendatei_basis_id_fk FOREIGN KEY (bauratendatei_basis_id) REFERENCES isidbuser.bauratendatei_input(id);

ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn
    ADD COLUMN has_bauratendatei_input             boolean,
    ADD COLUMN anmerkung_bauratendatei_input character varying(1000) COLLATE pg_catalog."default",
    ADD COLUMN bauratendatei_basis_id      character varying(36) COLLATE pg_catalog."default",
    ADD CONSTRAINT bauratendatei_basis_id_fk FOREIGN KEY (bauratendatei_basis_id) REFERENCES isidbuser.bauratendatei_input(id);

END;