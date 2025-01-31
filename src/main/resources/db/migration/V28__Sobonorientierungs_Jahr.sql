--
-- Hinzufügen der Spalte Jahr Bezeichnung
--

BEGIN;

ALTER TABLE IF EXISTS isidbuser.sobon_orientierungswert_soziale_infrastruktur
    DROP CONSTRAINT sobon_orientierungswert_sozia_gueltig_ab_einrichtungstyp_al_key;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn
    DROP CONSTRAINT abfragevariante_bauleitplanv_sobon_orientierungswert_jahr_check;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_baugnhmgsverfhrn
    DROP CONSTRAINT abfragevariante_baugenehmigu_sobon_orientierungswert_jahr_check;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn
    DROP CONSTRAINT abfragevariante_weiteres_ver_sobon_orientierungswert_jahr_check;

ALTER TABLE IF EXISTS isidbuser.sobon_orientierungswert_soziale_infrastruktur
    ADD COLUMN jahr_bezeichnung character varying(200);

ALTER TABLE IF EXISTS isidbuser.sobon_orientierungswert_soziale_infrastruktur
    ALTER COLUMN stammwert_arbeitsgruppe DROP NOT NULL, ALTER COLUMN einwohner_jahr1nach_ersterstellung DROP NOT NULL,
    ALTER COLUMN einwohner_jahr2nach_ersterstellung DROP NOT NULL, ALTER COLUMN einwohner_jahr3nach_ersterstellung DROP NOT NULL,
    ALTER COLUMN einwohner_jahr4nach_ersterstellung DROP NOT NULL, ALTER COLUMN einwohner_jahr5nach_ersterstellung DROP NOT NULL,
    ALTER COLUMN einwohner_jahr6nach_ersterstellung DROP NOT NULL, ALTER COLUMN einwohner_jahr7nach_ersterstellung DROP NOT NULL,
    ALTER COLUMN einwohner_jahr8nach_ersterstellung DROP NOT NULL, ALTER COLUMN einwohner_jahr9nach_ersterstellung DROP NOT NULL,
    ALTER COLUMN einwohner_jahr10nach_ersterstellung DROP NOT NULL;

ALTER TABLE IF EXISTS isidbuser.sobon_orientierungswert_soziale_infrastruktur
    ADD CONSTRAINT sobon_orientierungswert_sozia_gueltig_bez_einrichtungstyp_al_key UNIQUE (gueltig_ab,jahr_bezeichnung,einrichtungstyp,altersklasse,foerderart_bezeichnung);

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn
    ADD CONSTRAINT abfragevariante_bauleitplanv_sobon_orientierungswert_jahr_check CHECK (sobon_orientierungswert_jahr_planungsursaechlich::text = ANY (ARRAY['UNSPECIFIED'::character varying::text, 'JAHR_2014'::character varying::text, 'JAHR_2017'::character varying::text, 'JAHR_2022'::character varying::text, 'JAHR_2024'::character varying::text, 'JAHR_2024_34'::character varying::text, 'STANDORTABFRAGE'::character varying::text]));

ALTER TABLE IF EXISTS isidbuser.abfrgvar_baugnhmgsverfhrn
    ADD CONSTRAINT abfragevariante_baugenehmigu_sobon_orientierungswert_jahr_check CHECK (sobon_orientierungswert_jahr_planungsursaechlich::text = ANY (ARRAY['UNSPECIFIED'::character varying::text, 'JAHR_2014'::character varying::text, 'JAHR_2017'::character varying::text, 'JAHR_2022'::character varying::text, 'JAHR_2024'::character varying::text, 'JAHR_2024_34'::character varying::text, 'STANDORTABFRAGE'::character varying::text]));

ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn
    ADD CONSTRAINT abfragevariante_weiteres_ver_sobon_orientierungswert_jahr_check CHECK (sobon_orientierungswert_jahr_planungsursaechlich::text = ANY (ARRAY['UNSPECIFIED'::character varying::text, 'JAHR_2014'::character varying::text, 'JAHR_2017'::character varying::text, 'JAHR_2022'::character varying::text, 'JAHR_2024'::character varying::text, 'JAHR_2024_34'::character varying::text, 'STANDORTABFRAGE'::character varying::text]));


END;