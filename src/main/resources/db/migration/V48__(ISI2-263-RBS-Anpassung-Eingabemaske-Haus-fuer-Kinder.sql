BEGIN;

--
-- Haus für Kinder
--
ALTER TABLE IF EXISTS isidbuser.hauskinderkrippe
    DROP COLUMN IF EXISTS anzahl_hort_gruppen,
    DROP COLUMN IF EXISTS anzahl_hort_plaetze,
    DROP COLUMN IF EXISTS anzahl_kindergarten_gruppen,
    DROP COLUMN IF EXISTS anzahl_kindergarten_plaetze,
    DROP COLUMN IF EXISTS anzahl_kinderkrippe_gruppen,
    DROP COLUMN IF EXISTS anzahl_kinderkrippe_plaetze,
    DROP COLUMN IF EXISTS wohnungsnahe_hort_plaetze,
    DROP COLUMN IF EXISTS wohnungsnahe_kinderkrippe_plaetze,
    DROP COLUMN IF EXISTS wohnungsnahe_kindergarten_plaetze,

    ADD COLUMN sobon_relevant varchar(255) not null DEFAULT 'UNSPECIFIED',
    ADD COLUMN muenchner_kitafoerderung boolean,
    ADD COLUMN mini_kita boolean,
    ADD COLUMN kitz boolean,
    ADD COLUMN integrationseinrichtung boolean,
    ADD COLUMN name_traeger varchar(255),
    ADD COLUMN link_ke_plus varchar(8000);

    ADD COLUMN gesamtplaetze_nach_be integer,
    ADD COLUMN kinderkrippenplaetze_nach_be integer,
    ADD COLUMN ueberregionale_kinderkrippenplaetze integer,
    ADD COLUMN belegung_kinderkrippenplaetze integer,
    ADD COLUMN soll_belegung_kinderkrippenplaetze_ke_plus integer,
    ADD COLUMN soll_belegung_kinderkrippenplaetze integer,
    ADD COLUMN integrationsplaetze_kinderkrippe_ke_plus integer,
    ADD COLUMN integrationsplaetze_kinderkrippe integer,
    ADD COLUMN belegung_integrationsplaetze_kinderkrippe integer,

    ADD COLUMN hortplaetze_nach_be integer,
    ADD COLUMN belegung_hort integer,
    ADD COLUMN ueberregionale_hortplaetze integer,

    ADD COLUMN soll_belegung_hort_ke_plus integer,
    ADD COLUMN soll_belegung_hort integer,
    ADD COLUMN integrationsplaetze_hort_ke_plus integer,
    ADD COLUMN integrationsplaetze_hort integer,
    ADD COLUMN belegung_integrationsplaetze_hort integer,

--
-- Bereinigung Einrichtungsträger
--

ALTER TABLE IF EXISTS isidbuser.haus_für_kinder
DROP CONSTRAINT haus_für_kinder_einrichtungstraeger_check;

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

COMMIT;
