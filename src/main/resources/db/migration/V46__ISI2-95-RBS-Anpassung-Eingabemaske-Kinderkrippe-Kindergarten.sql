BEGIN;

-- Infrastruktureinrichtung
ALTER TABLE IF EXISTS isidbuser.infrastruktureinrichtung
DROP COLUMN IF EXISTS flaeche_gesamtgrundstueck,
    DROP COLUMN IF EXISTS flaeche_teilgrundstueck;

-- Kinderkrippe
ALTER TABLE IF EXISTS isidbuser.kinderkrippe
DROP COLUMN IF EXISTS anzahl_kinderkrippe_gruppen,
    DROP COLUMN IF EXISTS anzahl_kinderkrippe_plaetze,
    DROP COLUMN IF EXISTS wohnungsnahe_kinderkrippe_plaetze,
    ALTER COLUMN einrichtungstraeger SET NOT NULL,
    ADD COLUMN IF NOT EXISTS fertigstellungsmonat integer,
    ADD COLUMN IF NOT EXISTS sobon_relevant varchar(255),
    ADD COLUMN IF NOT EXISTS muenchner_kitafoerderung varchar(255),
    ADD COLUMN IF NOT EXISTS mini_kita boolean,
    ADD COLUMN IF NOT EXISTS integrationseinrichtung boolean,
    ADD COLUMN IF NOT EXISTS name_traeger varchar(255),
    ADD COLUMN IF NOT EXISTS gesamtplaetze_nach_be integer,
    ADD COLUMN IF NOT EXISTS kinderkrippenplaetze_nach_be integer,
    ADD COLUMN IF NOT EXISTS ueberregionale_kinderkrippenplaetze integer,
    ADD COLUMN IF NOT EXISTS belegung_kinderkrippenplaetze integer,
    ADD COLUMN IF NOT EXISTS soll_belegung_kinderkrippenplaetze_ke_plus integer,
    ADD COLUMN IF NOT EXISTS soll_belegung_kinderkrippenplaetze integer,
    ADD COLUMN IF NOT EXISTS integrationsplaetze_kinderkrippe_ke_plus integer,
    ADD COLUMN IF NOT EXISTS integrationsplaetze_kinderkrippe integer,
    ADD COLUMN IF NOT EXISTS belegung_integrationsplaetze_kinderkrippe integer,
    ADD COLUMN IF NOT EXISTS link_ke_plus varchar(8000);

-- Kindergarten
ALTER TABLE IF EXISTS isidbuser.kindergarten
DROP COLUMN IF EXISTS anzahl_kindergarten_gruppen,
    DROP COLUMN IF EXISTS anzahl_kindergarten_plaetze,
    DROP COLUMN IF EXISTS wohnungsnahe_kindergarten_plaetze,
    ALTER COLUMN einrichtungstraeger SET NOT NULL,
    ADD COLUMN IF NOT EXISTS sobon_relevant varchar(255),
    ADD COLUMN IF NOT EXISTS muenchner_kitafoerderung varchar(255),
    ADD COLUMN IF NOT EXISTS mini_kita boolean,
    ADD COLUMN IF NOT EXISTS integrationseinrichtung boolean,
    ADD COLUMN IF NOT EXISTS name_traeger varchar(255),
    ADD COLUMN IF NOT EXISTS gesamtplaetze_nach_be integer,
    ADD COLUMN IF NOT EXISTS kindergartenplaetze_nach_be integer,
    ADD COLUMN IF NOT EXISTS ueberregionale_kindergartenplaetze integer,
    ADD COLUMN IF NOT EXISTS belegung_kindergartenplaetze integer,
    ADD COLUMN IF NOT EXISTS soll_belegung_kindergartenplaetze_ke_plus integer,
    ADD COLUMN IF NOT EXISTS soll_belegung_kindergartenplaetze integer,
    ADD COLUMN IF NOT EXISTS integrationsplaetze_kindergarten_ke_plus integer,
    ADD COLUMN IF NOT EXISTS integrationsplaetze_kindergarten integer,
    ADD COLUMN IF NOT EXISTS belegung_integrationsplaetze_kindergarten integer,
    ADD COLUMN IF NOT EXISTS link_ke_plus varchar(8000);

COMMIT;
