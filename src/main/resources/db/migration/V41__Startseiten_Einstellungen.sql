BEGIN;

--
-- Persönliche Startseiteneinstellungen ("Meine Vorgänge")
--

CREATE TABLE isidbuser.startseiten_einstellung
(
    id                      character varying(36) PRIMARY KEY,
    version                 BIGINT,
    created_date_time       TIMESTAMP              NOT NULL,
    last_modified_date_time TIMESTAMP              NOT NULL,

    personalid              character varying(255) NOT NULL UNIQUE,
    schnellfilter           character varying(255) NOT NULL,
    sort_by                 character varying(255) NOT NULL,
    sort_order              character varying(255) NOT NULL
);

END;
