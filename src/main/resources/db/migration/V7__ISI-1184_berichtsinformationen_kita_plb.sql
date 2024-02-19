--
-- Erweitern um Entität für Prognosedaten der Kita-Plb.
--

BEGIN;

CREATE TABLE isidbuser.berichtsdaten_kita_plb
(
    id                      character varying(36) COLLATE pg_catalog."default" NOT NULL,
    created_date_time       timestamp without time zone,
    last_modified_date_time timestamp without time zone,
    version                 bigint,
    kita_plb                bigint                                             NOT NULL,
    berichtsstand           timestamp(6) without time zone                     NOT NULL,
    altersgruppe            character varying(255)                             NOT NULL,
    anzahl_kinder           numeric(19, 2)                                     NOT NULL,
    CONSTRAINT berichtsdaten_kita_plb_pkey PRIMARY KEY (id),
    CONSTRAINT berichtsdaten_kita_plb_altersgruppe_check CHECK (altersgruppe::text = ANY (ARRAY ['NULL_ZWEI_JAEHRIGE'::character varying, 'DREI_FUENF_UND_FUENFZIG_PROZENT_SECHS_JAEHRIGE'::character varying]::text[])),
    CONSTRAINT berichtsdaten_kita_plb_unique UNIQUE (kita_plb, berichtsstand, altersgruppe)
);

CREATE INDEX IF NOT EXISTS berichtsdaten_kita_plb_index
    ON isidbuser.berichtsdaten_kita_plb USING btree (kita_plb, berichtsstand, altersgruppe);

END;
