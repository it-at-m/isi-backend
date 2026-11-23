BEGIN;

--
-- Haus für Kinder
--
ALTER TABLE IF EXISTS isidbuser.haus_fuer_kinder
    DROP COLUMN IF EXISTS anzahl_hort_gruppen,
    DROP COLUMN IF EXISTS anzahl_hort_plaetze,
    DROP COLUMN IF EXISTS anzahl_kindergarten_gruppen,
    DROP COLUMN IF EXISTS anzahl_kindergarten_plaetze,
    DROP COLUMN IF EXISTS anzahl_kinderkrippe_gruppen,
    DROP COLUMN IF EXISTS anzahl_kinderkrippe_plaetze,
    DROP COLUMN IF EXISTS wohnungsnahe_hort_plaetze,
    DROP COLUMN IF EXISTS wohnungsnahe_kinderkrippe_plaetze,
    DROP COLUMN IF EXISTS wohnungsnahe_kindergarten_plaetze,

    ADD COLUMN IF NOT EXISTS sobon_relevant varchar(255) NOT NULL DEFAULT 'UNSPECIFIED',
    ADD COLUMN IF NOT EXISTS muenchner_kitafoerderung boolean,
    ADD COLUMN IF NOT EXISTS mini_kita boolean,
    ADD COLUMN IF NOT EXISTS kitz boolean,
    ADD COLUMN IF NOT EXISTS integrationseinrichtung boolean,
    ADD COLUMN IF NOT EXISTS name_traeger varchar(255),
    ADD COLUMN IF NOT EXISTS link_ke_plus varchar(8000),

    ADD COLUMN IF NOT EXISTS gesamtplaetze_nach_be integer,
    ADD COLUMN IF NOT EXISTS kinderkrippenplaetze_nach_be integer,
    ADD COLUMN IF NOT EXISTS ueberregionale_kinderkrippenplaetze integer,
    ADD COLUMN IF NOT EXISTS belegung_kinderkrippenplaetze integer,
    ADD COLUMN IF NOT EXISTS soll_belegung_kinderkrippenplaetze_ke_plus integer,
    ADD COLUMN IF NOT EXISTS soll_belegung_kinderkrippenplaetze integer,
    ADD COLUMN IF NOT EXISTS integrationsplaetze_kinderkrippe_ke_plus integer,
    ADD COLUMN IF NOT EXISTS integrationsplaetze_kinderkrippe integer,
    ADD COLUMN IF NOT EXISTS belegung_integrationsplaetze_kinderkrippe integer,

    ADD COLUMN IF NOT EXISTS kindergartenplaetze_nach_be integer,
    ADD COLUMN IF NOT EXISTS ueberregionale_kindergartenplaetze integer,
    ADD COLUMN IF NOT EXISTS belegung_kindergartenplaetze integer,
    ADD COLUMN IF NOT EXISTS soll_belegung_kindergartenplaetze_ke_plus integer,
    ADD COLUMN IF NOT EXISTS soll_belegung_kindergartenplaetze integer,
    ADD COLUMN IF NOT EXISTS integrationsplaetze_kindergarten_ke_plus integer,
    ADD COLUMN IF NOT EXISTS integrationsplaetze_kindergarten integer,
    ADD COLUMN IF NOT EXISTS belegung_integrationsplaetze_kindergarten integer,

    ADD COLUMN IF NOT EXISTS hortplaetze_nach_be integer,
    ADD COLUMN IF NOT EXISTS belegung_hort integer,
    ADD COLUMN IF NOT EXISTS ueberregionale_hortplaetze integer,
    ADD COLUMN IF NOT EXISTS soll_belegung_hort_ke_plus integer,
    ADD COLUMN IF NOT EXISTS soll_belegung_hort integer,
    ADD COLUMN IF NOT EXISTS integrationsplaetze_hort_ke_plus integer,
    ADD COLUMN IF NOT EXISTS integrationsplaetze_hort integer,
    ADD COLUMN IF NOT EXISTS belegung_integrationsplaetze_hort integer;

--
-- Bereinigung Einrichtungsträger
--
ALTER TABLE IF EXISTS isidbuser.haus_fuer_kinder
    DROP CONSTRAINT IF EXISTS haus_fuer_kinder_einrichtungstraeger_check;

UPDATE isidbuser.haus_fuer_kinder
SET einrichtungstraeger = 'UNSPECIFIED'
WHERE einrichtungstraeger IN (
   'EINRICHTUNG_GESAMTSTAEDTISCH',
   'PRIVATE_TRAEGERSCHAFT',
   'STAATLICHE_EINRICHTUNG',
   'KIRCHLICHE_TRAEGERSCHAFT'
);

ALTER TABLE IF EXISTS isidbuser.haus_fuer_kinder
    ADD CONSTRAINT haus_fuer_kinder_einrichtungstraeger_check CHECK (einrichtungstraeger IN (
    'UNSPECIFIED',
    'STAEDTISCHE_EINRICHTUNG',
    'EINRICHTUNG_BETRIEBSTRAEGERSCHAFT',
    'FREIE_GEMEINNUETZIGE_SONSTIGE',
    'ELTERN_KIND_INITIATIVE'
));

COMMIT;
