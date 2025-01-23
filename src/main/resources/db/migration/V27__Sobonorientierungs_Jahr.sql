--
-- Hinzufügen der Spalte Jahr Bezeichnung
--

BEGIN;

ALTER TABLE IF EXISTS isidbuser.sobon_orientierungswert_soziale_infrastruktur
    ADD COLUMN jahr_bezeichnung character varying(200);

ALTER TABLE IF EXISTS isidbuser.sobon_orientierungswert_soziale_infrastruktur
    ALTER COLUMN einwohner_jahr1nach_ersterstellung DROP NOT NULL,
    ALTER COLUMN einwohner_jahr2nach_ersterstellung DROP NOT NULL, ALTER COLUMN einwohner_jahr3nach_ersterstellung DROP NOT NULL,
    ALTER COLUMN einwohner_jahr4nach_ersterstellung DROP NOT NULL, ALTER COLUMN einwohner_jahr5nach_ersterstellung DROP NOT NULL,
    ALTER COLUMN einwohner_jahr6nach_ersterstellung DROP NOT NULL, ALTER COLUMN einwohner_jahr7nach_ersterstellung DROP NOT NULL,
    ALTER COLUMN einwohner_jahr8nach_ersterstellung DROP NOT NULL, ALTER COLUMN einwohner_jahr9nach_ersterstellung DROP NOT NULL,
    ALTER COLUMN einwohner_jahr10nach_ersterstellung DROP NOT NULL;
END;