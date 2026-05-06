BEGIN;

---
-- Bauleitplanverfahren: Wesentliche Rechtsgrundlage -> Planart
---
ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn
    RENAME COLUMN wesentliche_rechtsgrundlage_freie_eingabe TO planart_freie_eingabe;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn_wesentliche_rechtsgrundlage
DROP CONSTRAINT abfragevariante_bauleitplanve_wesentliche_rechtsgrundlage_check;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn_wesentliche_rechtsgrundlage RENAME TO abfrgvar_bauleitplnvrfhrn_planart;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn_planart
    RENAME COLUMN wesentliche_rechtsgrundlage TO planart;

CREATE VIEW isidbuser.abfrgvar_bauleitplnvrfhrn_wesentliche_rechtsgrundlage AS
SELECT
    abfrgvar_bauleitplnvrfhrn_id,
    planart AS wesentliche_rechtsgrundlage
from isidbuser.abfrgvar_bauleitplnvrfhrn_planart;

DROP VIEW isidbuser.abfrgvar_bauleitplnvrfhrn_wesentliche_rechtsgrundlage;

UPDATE isidbuser.abfrgvar_bauleitplnvrfhrn_planart
SET planart = CASE
                  WHEN planart = 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30' THEN 'EINFACHER_BEBAUUNGSPLAN'
                  WHEN planart = 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9' THEN 'BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG'
                  ELSE planart
    END;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn_planart
    ADD CONSTRAINT abfragevariante_bauleitplanve_planart_check CHECK (planart::text = ANY (ARRAY['EINFACHER_BEBAUUNGSPLAN'::character varying::text, 'QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying::text, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying::text, 'BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG'::character varying::text, 'FREIE_EINGABE'::character varying::text]));

---
-- Ändern Stand Verfahren -> Verfahrensstand
---

--- Bauleitplanverfahren

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
DROP CONSTRAINT bauleitplanverfahren_stand_verfahren_check;

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    RENAME COLUMN stand_verfahren TO verfahrensstand;

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    RENAME COLUMN stand_verfahren_freie_eingabe TO verfahrensstand_freie_eingabe;

UPDATE isidbuser.bauleitplanverfahren
SET verfahrensstand = CASE
        WHEN verfahrensstand = 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS' THEN 'SIMULIERT_VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'
        WHEN verfahrensstand = 'VORBEREITUNG_WETTBEWERBAUSLOBUNG' THEN 'SIMULIERT_VORBEREITUNG_WETTBEWERBAUSLOBUNG'
        WHEN verfahrensstand = 'RECHTSVERBINDLICHKEIT_AMTSBLATT' THEN 'INKRAFTGETRETEN_VEROEFFENTLICHUNG_AMTSBLATT'
        WHEN verfahrensstand = 'AUFTEILUNGSPLAN' THEN 'INKRAFTGETRETEN_FOERDERMIXPLAN'
        WHEN verfahrensstand = 'VORLIEGENDER_SATZUNGSBESCHLUSS' THEN 'FREIE_EINGABE'
        WHEN verfahrensstand = 'VORBEREITUNG_ECKDATENBESCHLUSS' THEN 'FREIE_EINGABE'
        ELSE verfahrensstand
    END;

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    ADD CONSTRAINT bauleitplanverfahren_verfahrensstand_check CHECK (verfahrensstand::text = ANY (ARRAY['UNSPECIFIED'::character varying::text, 'SIMULIERT_VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying::text, 'SIMULIERT_VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying::text, 'VORBEREITUNG_FRUEHZEITIGE_BETEILIGUNG'::character varying::text, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying::text, 'VORBEREITUNG_SATZUNGSBESCHLUSS'::character varying::text, 'INKRAFTGETRETEN_VEROEFFENTLICHUNG_AMTSBLATT'::character varying::text, 'INKRAFTGETRETEN_FOERDERMIXPLAN'::character varying::text, 'FREIE_EINGABE'::character varying::text]));

--- Baugenehmigungsverfahren

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
DROP CONSTRAINT baugenehmigungsverfahren_stand_verfahren_check;

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
    RENAME COLUMN stand_verfahren TO verfahrensstand;

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
    RENAME COLUMN stand_verfahren_freie_eingabe TO verfahrensstand_freie_eingabe;

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
    ADD CONSTRAINT baugenehmigungsverfahren_verfahrensstand_check CHECK (verfahrensstand::text = ANY (ARRAY['UNSPECIFIED'::character varying::text, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying::text, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying::text, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying::text, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying::text, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying::text, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying::text, 'AUFTEILUNGSPLAN'::character varying::text, 'VORBEREITUNG_VORBESCHEID'::character varying::text, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying::text, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying::text, 'STRUKTURKONZEPT'::character varying::text, 'RAHMENPLANUNG'::character varying::text, 'POTENTIALUNTERSUCHUNG'::character varying::text, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying::text, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying::text, 'FREIE_EINGABE'::character varying::text, 'STANDORTABFRAGE'::character varying::text]));

--- Weiteres Verfahren

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
DROP CONSTRAINT weiteres_verfahren_stand_verfahren_check;

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    RENAME COLUMN stand_verfahren TO verfahrensstand;

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    RENAME COLUMN stand_verfahren_freie_eingabe TO verfahrensstand_freie_eingabe;

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    ADD CONSTRAINT weiteres_verfahren_verfahrensstand_check CHECK (verfahrensstand::text = ANY (ARRAY['UNSPECIFIED'::character varying::text, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying::text, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying::text, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying::text, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying::text, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying::text, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying::text, 'AUFTEILUNGSPLAN'::character varying::text, 'VORBEREITUNG_VORBESCHEID'::character varying::text, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying::text, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying::text, 'STRUKTURKONZEPT'::character varying::text, 'RAHMENPLANUNG'::character varying::text, 'POTENTIALUNTERSUCHUNG'::character varying::text, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying::text, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying::text, 'FREIE_EINGABE'::character varying::text, 'STANDORTABFRAGE'::character varying::text]));

--- Bauvorhaben

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
DROP CONSTRAINT bauvorhaben_stand_verfahren_check;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    RENAME COLUMN stand_verfahren TO verfahrensstand;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    RENAME COLUMN stand_verfahren_freie_eingabe TO verfahrensstand_freie_eingabe;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ADD CONSTRAINT bauvorhaben_verfahrensstand_check CHECK (verfahrensstand::text = ANY (ARRAY['UNSPECIFIED'::character varying::text, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying::text, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying::text, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying::text, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying::text, 'VORBEREITUNG_SATZUNGSBESCHLUSS'::character varying::text, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying::text, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying::text, 'AUFTEILUNGSPLAN'::character varying::text, 'VORBEREITUNG_VORBESCHEID'::character varying::text, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying::text, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying::text, 'STRUKTURKONZEPT'::character varying::text, 'RAHMENPLANUNG'::character varying::text, 'POTENTIALUNTERSUCHUNG'::character varying::text, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying::text, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying::text, 'FREIE_EINGABE'::character varying::text, 'STANDORTABFRAGE'::character varying::text]));

END;
