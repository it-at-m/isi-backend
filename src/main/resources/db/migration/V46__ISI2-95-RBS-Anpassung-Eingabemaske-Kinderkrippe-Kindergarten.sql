--
--
-- Anpassung Kinderkrippe und Kindergarten
--
--
BEGIN;
--
-- Infrastruktureinrichtung
--
ALTER TABLE IF EXISTS isidbuser.infrastruktureinrichtung
    ADD COLUMN flaeche_gesamtgrundstueck numeric(10,2);
--
-- Kinderkrippe
--
ALTER TABLE IF EXISTS isidbuser.kinderkrippe
    DROP COLUMN IF EXISTS anzahl_kinderkrippe_gruppen;
    DROP COLUMN IF EXISTS anzahl_kinderkrippe_plaetze;
    DROP COLUMN IF EXISTS wohnungsnahe_kinderkrippe_plaetze
    ALTER COLUMN einrichtungstraeger SET NOT NULL,
    ADD COLUMN fertigstellungsmonat integer,
    ADD COLUMN sobon_relevant character varying(255) NOT NULL,
    ADD COLUMN muenchener_kitafoerderung character varying(255) NOT NULL,
    ADD COLUMN miniKita boolean,
    ADD COLUMN integrationseinrichtung boolean,
    ADD COLUMN name_traeger character varying(255),
    ADD COLUMN kinderkrippenplaetze_nach_be integer,
    ADD COLUMN ueberregionale_kinderkrippenplaetze integer,
    ADD COLUMN belegung_kinderkrippenplaetze integer,
    ADD COLUMN sollBelegung_kinderkrippenplaetze integer,
    ADD COLUMN integrationsplaetze_kinderkrippe integer,
    ADD COLUMN integrationsplaetze_kinderkrippe_laut_belegung integer,
    ADD COLUMN link_ke_plus character varying(8000);

END;
