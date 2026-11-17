BEGIN;

--
-- Infrastruktureinrichtung
--
ALTER TABLE IF EXISTS isidbuser.infrastruktureinrichtung
    DROP COLUMN IF EXISTS flaeche_gesamtgrundstueck,
    DROP COLUMN IF EXISTS flaeche_teilgrundstueck;
--
-- Kinderkrippe
--
ALTER TABLE IF EXISTS isidbuser.kinderkrippe
    DROP COLUMN IF EXISTS anzahl_kinderkrippe_gruppen,
    DROP COLUMN IF EXISTS anzahl_kinderkrippe_plaetze,
    DROP COLUMN IF EXISTS wohnungsnahe_kinderkrippe_plaetze,
    ADD COLUMN fertigstellungsmonat integer,
    ADD COLUMN sobon_relevant varchar(255) not null,
    ADD COLUMN muenchner_kitafoerderung boolean,
    ADD COLUMN mini_kita boolean,
    ADD COLUMN integrationseinrichtung boolean,
    ADD COLUMN name_traeger varchar(255),
    ADD COLUMN gesamtplaetze_nach_be integer,
    ADD COLUMN kinderkrippenplaetze_nach_be integer,
    ADD COLUMN ueberregionale_kinderkrippenplaetze integer,
    ADD COLUMN belegung_kinderkrippenplaetze integer,
    ADD COLUMN soll_belegung_kinderkrippenplaetze_ke_plus integer,
    ADD COLUMN soll_belegung_kinderkrippenplaetze integer,
    ADD COLUMN integrationsplaetze_kinderkrippe_ke_plus integer,
    ADD COLUMN integrationsplaetze_kinderkrippe integer,
    ADD COLUMN belegung_integrationsplaetze_kinderkrippe integer,
    ADD COLUMN link_ke_plus varchar(8000);

ALTER TABLE IF EXISTS isidbuser.kinderkrippe
    ALTER COLUMN sobon_relevant SET NOT NULL,
    ADD CONSTRAINT kinderkrippe_sobon_relevant_check CHECK ((sobon_relevant::text <> 'UNSPECIFIED'::text));

-- Kindergarten
ALTER TABLE IF EXISTS isidbuser.kindergarten
    DROP COLUMN IF EXISTS anzahl_kindergarten_gruppen,
    DROP COLUMN IF EXISTS anzahl_kindergarten_plaetze,
    DROP COLUMN IF EXISTS wohnungsnahe_kindergarten_plaetze,
    ADD COLUMN sobon_relevant varchar(255) not null,
    ADD COLUMN muenchner_kitafoerderung boolean,
    ADD COLUMN mini_kita boolean,
    ADD COLUMN integrationseinrichtung boolean,
    ADD COLUMN name_traeger varchar(255),
    ADD COLUMN gesamtplaetze_nach_be integer,
    ADD COLUMN kindergartenplaetze_nach_be integer,
    ADD COLUMN ueberregionale_kindergartenplaetze integer,
    ADD COLUMN belegung_kindergartenplaetze integer,
    ADD COLUMN soll_belegung_kindergartenplaetze_ke_plus integer,
    ADD COLUMN soll_belegung_kindergartenplaetze integer,
    ADD COLUMN integrationsplaetze_kindergarten_ke_plus integer,
    ADD COLUMN integrationsplaetze_kindergarten integer,
    ADD COLUMN belegung_integrationsplaetze_kindergarten integer,
    ADD COLUMN link_ke_plus varchar(8000);

ALTER TABLE IF EXISTS isidbuser.kindergarten
    ALTER COLUMN sobon_relevant SET NOT NULL,
    ADD CONSTRAINT kindergarten_sobon_relevant_check CHECK ((sobon_relevant::text <> 'UNSPECIFIED'::text));

--
-- Bereinigung Einrichtungsträger
--

-- Kinderkrippe
ALTER TABLE IF EXISTS isidbuser.kinderkrippe
DROP CONSTRAINT kinderkrippe_einrichtungstraeger_check;

UPDATE isidbuser.kinderkrippe
SET einrichtungstraeger = 'UNSPECIFIED'
WHERE einrichtungstraeger IN
      (
        'EINRICHTUNG_GESAMTSTAEDTISCH',
        'PRIVATE_TRAEGERSCHAFT',
        'STAATLICHE_EINRICHTUNG',
        'KIRCHLICHE_TRAEGERSCHAFT'
      );

ALTER TABLE IF EXISTS isidbuser.kinderkrippe
    ADD CONSTRAINT kinderkrippe_einrichtungstraeger_check CHECK (einrichtungstraeger::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'STAEDTISCHE_EINRICHTUNG'::character varying, 'EINRICHTUNG_BETRIEBSTRAEGERSCHAFT'::character varying, 'FREIE_GEMEINNUETZIGE_SONSTIGE'::character varying, 'ELTERN_KIND_INITIATIVE'::character varying::text]));

-- Kindergarten
ALTER TABLE IF EXISTS isidbuser.kindergarten
DROP CONSTRAINT kindergarten_einrichtungstraeger_check;

UPDATE isidbuser.kindergarten
SET einrichtungstraeger = 'UNSPECIFIED'
WHERE einrichtungstraeger IN
      (
        'EINRICHTUNG_GESAMTSTAEDTISCH',
        'PRIVATE_TRAEGERSCHAFT',
        'STAATLICHE_EINRICHTUNG',
        'KIRCHLICHE_TRAEGERSCHAFT'
      );

ALTER TABLE IF EXISTS isidbuser.kindergarten
    ADD CONSTRAINT kindergarten_einrichtungstraeger_check CHECK (einrichtungstraeger::text = ANY (ARRAY['UNSPECIFIED'::character varying, 'STAEDTISCHE_EINRICHTUNG'::character varying, 'EINRICHTUNG_BETRIEBSTRAEGERSCHAFT'::character varying, 'FREIE_GEMEINNUETZIGE_SONSTIGE'::character varying, 'ELTERN_KIND_INITIATIVE'::character varying::text]));

COMMIT;
