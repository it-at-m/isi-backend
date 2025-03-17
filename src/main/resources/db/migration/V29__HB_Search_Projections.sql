--
-- Hinzufügen der Spalte Jahr Bezeichnung
--

BEGIN;

ALTER TABLE isidbuser.bauvorhaben
    ADD COLUMN entity_type CHARACTER VARYING(31),
    ADD COLUMN umgriff jsonb,
    ADD CONSTRAINT entity_type_check CHECK (entity_type::text = ANY (ARRAY['BAUVORHABEN'::character varying]::text[]));


ALTER TABLE isidbuser.infrastruktureinrichtung
    ADD COLUMN entity_type CHARACTER VARYING(31),
    ADD CONSTRAINT entity_type_check CHECK (entity_type::text = ANY (ARRAY['INFRASTRUKTUREINRICHTUNG'::character varying]::text[]));


ALTER TABLE isidbuser.bauleitplanverfahren
    ADD COLUMN entity_type CHARACTER VARYING(31),
    ADD CONSTRAINT entity_type_check CHECK (entity_type::text = ANY (ARRAY['ABFRAGE'::character varying]::text[]));


ALTER TABLE isidbuser.baugenehmigungsverfahren
    ADD COLUMN entity_type CHARACTER VARYING(31),
    ADD CONSTRAINT entity_type_check CHECK (entity_type::text = ANY (ARRAY['ABFRAGE'::character varying]::text[]));

ALTER TABLE isidbuser.weiteres_verfahren
    ADD COLUMN entity_type CHARACTER VARYING(31),
    ADD CONSTRAINT entity_type_check CHECK (entity_type::text = ANY (ARRAY['ABFRAGE'::character varying]::text[]));

UPDATE isidbuser.bauvorhaben
    SET entity_type = 'BAUVORHABEN';

UPDATE isidbuser.infrastruktureinrichtung
    SET entity_type = 'INFRASTRUKTUREINRICHTUNG';

UPDATE isidbuser.bauleitplanverfahren
    SET entity_type = 'ABFRAGE';

UPDATE isidbuser.baugenehmigungsverfahren
    SET entity_type = 'ABFRAGE';

UPDATE isidbuser.weiteres_verfahren
    SET entity_type = 'ABFRAGE';
END;