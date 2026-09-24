--
-- Shedlock Tabelle: Steuerung konkurrierender Zugriffe von Cron-Jobs
--
BEGIN;

CREATE TABLE isidbuser.shedlock (
    name character varying(255) NOT NULL,
    lock_until timestamp(6) without time zone NOT NULL,
    locked_at timestamp(6) without time zone NOT NULL,
    locked_by character varying(255) NOT NULL,
    CONSTRAINT shedlock_pkey PRIMARY KEY (name)
);

END;

