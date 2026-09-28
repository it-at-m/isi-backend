BEGIN;

ALTER TABLE IF EXISTS isidbuser.infrastruktureinrichtung
    ADD COLUMN anlass_planung character varying(255) NOT NULL DEFAULT 'UNSPECIFIED';

END;
