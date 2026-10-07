--
-- Infrastruktureinrichtung: Nachbarschaftstreff
--
BEGIN;

CREATE TABLE isidbuser.nachbarschaftstreff (
    kooperation character varying(255),
    kooperation_freie_eingabe character varying(1000),
    sobon_relevant character varying(255) NOT NULL,
    CONSTRAINT weiteres_verfahren_sobon_relevant_check1 CHECK (((sobon_relevant)::text <> 'UNSPECIFIED'::text)),
    id character varying(36) NOT NULL
);

ALTER TABLE isidbuser.nachbarschaftstreff OWNER TO isidbuser;

END;
