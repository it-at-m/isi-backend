BEGIN;

ALTER TABLE IF EXISTS isidbuser.infrastruktureinrichtung
    ADD COLUMN anlass_planung character varying(255) NOT NULL DEFAULT 'UNSPECIFIED';

ALTER TABLE IF EXISTS isidbuser.infrastruktureinrichtung
    DROP CONSTRAINT IF EXISTS infrastruktureinrichtung_status_check;

UPDATE isidbuser.infrastruktureinrichtung
SET status =
        CASE
            WHEN status = 'BESTAND' THEN 'BESTAND'
            WHEN status = 'UNGESICHERTE_PLANUNG' THEN 'UNGESICHERTE_PLANUNG'
            WHEN status = 'UNGESICHERTE_PLANUNG_TF_KITA_STANDORT' THEN 'UNGESICHERTE_PLANUNG'
            WHEN status = 'GESICHERTE_PLANUNG_NEUE_EINR' THEN 'GESICHERTE_PLANUNG'
            WHEN status = 'GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR' THEN 'GESICHERTE_PLANUNG'
            WHEN status = 'GESICHERTE_PLANUNG_TF_KITA_STANDORT' THEN 'GESICHERTE_PLANUNG'
            WHEN status = 'GESICHERTE_PLANUNG_REDUZIERUNG_PLAETZE' THEN 'GESICHERTE_PLANUNG'
            WHEN status = 'GESICHERTE_PLANUNG_INTERIMSSTANDORT' THEN 'GESICHERTE_PLANUNG'
            WHEN status = 'PLANUNG_ZURUECKGEZOGEN' THEN 'PLANUNG_ZURUECKGEZOGEN'
            ELSE status
            END;

ALTER TABLE IF EXISTS isidbuser.infrastruktureinrichtung
    ADD CONSTRAINT infrastruktureinrichtung_status_check CHECK (
        status::text = ANY (
            ARRAY [
                'BESTAND'::character varying,
                'UNGESICHERTE_PLANUNG'::character varying,
                'GESICHERTE_PLANUNG'::character varying,
                'PLANUNG_ZURUECKGEZOGEN'::character varying
                ]::text[]
            )
        );

END;
