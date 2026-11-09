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
    DROP COLUMN IF EXISTS flaeche_gesamtgrundstueck;
    DROP COLUMN IF EXISTS flaeche_teilgrundstueck;
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
    ADD COLUMN gesamtplaetze_nach_be integer,
    ADD COLUMN kinderkrippenplaetze_nach_be integer,
    ADD COLUMN ueberregionale_kinderkrippenplaetze integer,
    ADD COLUMN belegung_kinderkrippenplaetze integer,
    ADD COLUMN sollBelegung_kinderkrippenplaetze_ke_plus integer,
    ADD COLUMN sollBelegung_kinderkrippenplaetze integer,
    ADD COLUMN integrationsplaetze_kinderkrippe_ke_plus integer,
    ADD COLUMN integrationsplaetze_kinderkrippe integer,
    ADD COLUMN belegungIntegrationsplaetzeKinderkrippe integer,
    ADD COLUMN link_ke_plus character varying(8000);
--
-- Kindergarten
--
ALTER TABLE IF EXISTS isidbuser.kindergarten
DROP COLUMN IF EXISTS anzahl_kindergarten_gruppen;
    DROP COLUMN IF EXISTS anzahl_kindergarten_plaetze;
    DROP COLUMN IF EXISTS wohnungsnahe_kindergarten_plaetze
    ALTER COLUMN einrichtungstraeger SET NOT NULL,
    ADD COLUMN sobon_relevant character varying(255) NOT NULL,
    ADD COLUMN muenchener_kitafoerderung character varying(255) NOT NULL,
    ADD COLUMN miniKita boolean,
    ADD COLUMN integrationseinrichtung boolean,
    ADD COLUMN name_traeger character varying(255),
    ADD COLUMN gesamtplaetze_nach_be integer,
    ADD COLUMN kindergartenplaetze_nach_be integer,
    ADD COLUMN ueberregionale_kindergartenplaetze integer,
    ADD COLUMN belegung_kindergartenplaetze integer,
    ADD COLUMN sollBelegung_kindergartenplaetze_ke_plus integer,
    ADD COLUMN sollBelegung_kindergartenplaetze integer,
    ADD COLUMN integrationsplaetze_kindergarten_ke_plus integer,
    ADD COLUMN integrationsplaetze_kindergarten integer,
    ADD COLUMN belegungIntegrationsplaetzeKindergarten integer,
    ADD COLUMN link_ke_plus character varying(8000);

END;
