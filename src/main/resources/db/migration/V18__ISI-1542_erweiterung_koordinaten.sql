--
-- Erweiterung der Adresskoordinaten um UTM-Koordinate
--

BEGIN;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ADD COLUMN zone character varying(3),
    ADD COLUMN east double precision,
    ADD COLUMN north double precision;

ALTER TABLE IF EXISTS isidbuser.infrastruktureinrichtung
    ADD COLUMN zone character varying(3),
    ADD COLUMN east double precision,
    ADD COLUMN north double precision;

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    ADD COLUMN zone character varying(3),
    ADD COLUMN east double precision,
    ADD COLUMN north double precision;

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    ADD COLUMN zone character varying(3),
    ADD COLUMN east double precision,
    ADD COLUMN north double precision;

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
    ADD COLUMN zone character varying(3),
    ADD COLUMN east double precision,
    ADD COLUMN north double precision;

END;