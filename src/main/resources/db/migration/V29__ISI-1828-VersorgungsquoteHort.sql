--
-- Hinzufügen des Feldes VersorgungsquoteHortSobon in Tabelle
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn
    ADD COLUMN versorgungsquote_hort_sobon numeric(4,3),
    ADD CONSTRAINT abfrgvar_bauleitplnvrfhrn_vq_hort_sobon_check
    CHECK (versorgungsquote_hort_sobon >= 0 AND versorgungsquote_hort_sobon <= 1);

ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn
    ADD COLUMN versorgungsquote_hort_sobon numeric(4,3),
    ADD CONSTRAINT abfrgvar_weitrs_vrfhrn_vq_hort_sobon_check
    CHECK (versorgungsquote_hort_sobon >= 0 AND versorgungsquote_hort_sobon <= 1);

CREATE TABLE isidbuser.versorgungsquote_sobon_hort (
    id character varying(36) NOT NULL,
    created_date_time timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version bigint,
    beschreibung character varying(255) NOT NULL,
    versorgungsquote_sobon numeric(4,3) NOT NULL,
    CONSTRAINT pk_versorgungsquote_sobon_hort PRIMARY KEY (id),
    CONSTRAINT uq_versorgungsquote_sobon_hort_beschreibung UNIQUE (beschreibung),
    CONSTRAINT versorgungsquote_sobon_hort_versorgungsquote_sobon_check CHECK (((versorgungsquote_sobon >= (0)::numeric) AND (versorgungsquote_sobon <= (1)::numeric)))
);

END;