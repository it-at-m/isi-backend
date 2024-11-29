--
-- Entfernen von "INFO_FEHLT" aus den Tabellen und den Constraints
--
BEGIN;

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
    DROP CONSTRAINT baugenehmigungsverfahren_stand_verfahren_check;

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    DROP CONSTRAINT bauleitplanverfahren_stand_verfahren_check;

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    DROP CONSTRAINT weiteres_verfahren_stand_verfahren_check;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    DROP CONSTRAINT bauvorhaben_stand_verfahren_check;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_baugnhmgsverfhrn_wesentliche_rechtsgrundlage
    DROP CONSTRAINT abfragevariante_baugenehmigun_wesentliche_rechtsgrundlage_check;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn_wesentliche_rechtsgrundlage
    DROP CONSTRAINT abfragevariante_bauleitplanve_wesentliche_rechtsgrundlage_check;

ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn_wesentliche_rechtsgrundlage
    DROP CONSTRAINT abfragevariante_weiteres_verf_wesentliche_rechtsgrundlage_check;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben_wesentliche_rechtsgrundlage
    DROP CONSTRAINT bauvorhaben_wesentliche_recht_wesentliche_rechtsgrundlage_check;

ALTER TABLE IF EXISTS isidbuser.baugebiet
    DROP CONSTRAINT baugebiet_art_bauliche_nutzung_check;

ALTER TABLE IF EXISTS isidbuser.bauvorhaben_art_fnp
    DROP CONSTRAINT bauvorhaben_art_fnp_art_fnp_check;

UPDATE isidbuser.baugenehmigungsverfahren SET stand_verfahren = 'FREIE_EINGABE' WHERE stand_verfahren = 'INFO_FEHLT';
UPDATE isidbuser.bauleitplanverfahren SET stand_verfahren = 'FREIE_EINGABE' WHERE stand_verfahren = 'INFO_FEHLT';
UPDATE isidbuser.weiteres_verfahren SET stand_verfahren = 'FREIE_EINGABE' WHERE stand_verfahren = 'INFO_FEHLT';
UPDATE isidbuser.bauvorhaben SET stand_verfahren = 'FREIE_EINGABE' WHERE stand_verfahren = 'INFO_FEHLT';
UPDATE isidbuser.abfrgvar_baugnhmgsverfhrn_wesentliche_rechtsgrundlage SET wesentliche_rechtsgrundlage = 'FREIE_EINGABE' WHERE wesentliche_rechtsgrundlage = 'INFO_FEHLT';
UPDATE isidbuser.abfrgvar_bauleitplnvrfhrn_wesentliche_rechtsgrundlage SET wesentliche_rechtsgrundlage = 'FREIE_EINGABE' WHERE wesentliche_rechtsgrundlage = 'INFO_FEHLT';
UPDATE isidbuser.abfrgvar_weitrs_vrfhrn_wesentliche_rechtsgrundlage SET wesentliche_rechtsgrundlage = 'FREIE_EINGABE' WHERE wesentliche_rechtsgrundlage = 'INFO_FEHLT';
UPDATE isidbuser.bauvorhaben_wesentliche_rechtsgrundlage SET wesentliche_rechtsgrundlage = 'FREIE_EINGABE' WHERE wesentliche_rechtsgrundlage = 'INFO_FEHLT';
UPDATE isidbuser.baugebiet SET art_bauliche_nutzung = 'FREIE_EINGABE' WHERE art_bauliche_nutzung = 'INFO_FEHLT';
UPDATE isidbuser.bauvorhaben_art_fnp SET art_fnp = 'FREIE_EINGABE' WHERE art_fnp = 'INFO_FEHLT';

ALTER TABLE IF EXISTS isidbuser.baugenehmigungsverfahren
    ADD CONSTRAINT baugenehmigungsverfahren_stand_verfahren_check CHECK (stand_verfahren::text = ANY (ARRAY['UNSPECIFIED'::character varying::text, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying::text, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying::text, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying::text, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying::text, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying::text, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying::text, 'AUFTEILUNGSPLAN'::character varying::text, 'VORBEREITUNG_VORBESCHEID'::character varying::text, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying::text, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying::text, 'STRUKTURKONZEPT'::character varying::text, 'RAHMENPLANUNG'::character varying::text, 'POTENTIALUNTERSUCHUNG'::character varying::text, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying::text, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying::text, 'FREIE_EINGABE'::character varying::text, 'STANDORTABFRAGE'::character varying::text]));

ALTER TABLE IF EXISTS isidbuser.bauleitplanverfahren
    ADD CONSTRAINT bauleitplanverfahren_stand_verfahren_check CHECK (stand_verfahren::text = ANY (ARRAY['UNSPECIFIED'::character varying::text, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying::text, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying::text, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying::text, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying::text, 'VORBEREITUNG_SATZUNGSBESCHLUSS'::character varying::text, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying::text, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying::text, 'AUFTEILUNGSPLAN'::character varying::text, 'VORBEREITUNG_VORBESCHEID'::character varying::text, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying::text, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying::text, 'STRUKTURKONZEPT'::character varying::text, 'RAHMENPLANUNG'::character varying::text, 'POTENTIALUNTERSUCHUNG'::character varying::text, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying::text, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying::text, 'FREIE_EINGABE'::character varying::text, 'STANDORTABFRAGE'::character varying::text]));

ALTER TABLE IF EXISTS isidbuser.weiteres_verfahren
    ADD CONSTRAINT weiteres_verfahren_stand_verfahren_check CHECK (stand_verfahren::text = ANY (ARRAY['UNSPECIFIED'::character varying::text, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying::text, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying::text, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying::text, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying::text, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying::text, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying::text, 'AUFTEILUNGSPLAN'::character varying::text, 'VORBEREITUNG_VORBESCHEID'::character varying::text, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying::text, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying::text, 'STRUKTURKONZEPT'::character varying::text, 'RAHMENPLANUNG'::character varying::text, 'POTENTIALUNTERSUCHUNG'::character varying::text, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying::text, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying::text, 'FREIE_EINGABE'::character varying::text, 'STANDORTABFRAGE'::character varying::text]));

ALTER TABLE IF EXISTS isidbuser.bauvorhaben
    ADD CONSTRAINT bauvorhaben_stand_verfahren_check CHECK (stand_verfahren::text = ANY (ARRAY['UNSPECIFIED'::character varying::text, 'VORBEREITUNG_ECKDATENBESCHLUSS'::character varying::text, 'VORBEREITUNG_WETTBEWERBAUSLOBUNG'::character varying::text, 'VORBEREITUNG_AUFSTELLUNGSBESCHLUSS'::character varying::text, 'VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG'::character varying::text, 'VORBEREITUNG_SATZUNGSBESCHLUSS'::character varying::text, 'VORLIEGENDER_SATZUNGSBESCHLUSS'::character varying::text, 'RECHTSVERBINDLICHKEIT_AMTSBLATT'::character varying::text, 'AUFTEILUNGSPLAN'::character varying::text, 'VORBEREITUNG_VORBESCHEID'::character varying::text, 'VORBEREITUNG_BAUGENEHMIGUNG'::character varying::text, 'VORABFRAGE_OHNE_KONKRETEN_STAND'::character varying::text, 'STRUKTURKONZEPT'::character varying::text, 'RAHMENPLANUNG'::character varying::text, 'POTENTIALUNTERSUCHUNG'::character varying::text, 'STAEDTEBAULICHE_SANIERUNGSMASSNAHME'::character varying::text, 'STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME'::character varying::text, 'FREIE_EINGABE'::character varying::text, 'STANDORTABFRAGE'::character varying::text]));

ALTER TABLE IF EXISTS isidbuser.abfrgvar_baugnhmgsverfhrn_wesentliche_rechtsgrundlage
    ADD CONSTRAINT abfragevariante_baugenehmigun_wesentliche_rechtsgrundlage_check CHECK (wesentliche_rechtsgrundlage::text = ANY (ARRAY['QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying::text, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying::text, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30'::character varying::text, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying::text, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9'::character varying::text, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying::text, 'INNENBEREICH'::character varying::text, 'AUSSENBEREICH'::character varying::text, 'BEFREIUNG'::character varying::text, 'FREIE_EINGABE'::character varying::text]));

ALTER TABLE IF EXISTS isidbuser.abfrgvar_bauleitplnvrfhrn_wesentliche_rechtsgrundlage
    ADD CONSTRAINT abfragevariante_bauleitplanve_wesentliche_rechtsgrundlage_check CHECK (wesentliche_rechtsgrundlage::text = ANY (ARRAY['QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying::text, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying::text, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30'::character varying::text, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying::text, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9'::character varying::text, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying::text, 'INNENBEREICH'::character varying::text, 'AUSSENBEREICH'::character varying::text, 'BEFREIUNG'::character varying::text, 'FREIE_EINGABE'::character varying::text]));

ALTER TABLE IF EXISTS isidbuser.abfrgvar_weitrs_vrfhrn_wesentliche_rechtsgrundlage
    ADD CONSTRAINT abfragevariante_weiteres_verf_wesentliche_rechtsgrundlage_check CHECK (wesentliche_rechtsgrundlage::text = ANY (ARRAY['QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying::text, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying::text, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30'::character varying::text, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying::text, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9'::character varying::text, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying::text, 'INNENBEREICH'::character varying::text, 'AUSSENBEREICH'::character varying::text, 'BEFREIUNG'::character varying::text 'FREIE_EINGABE'::character varying::text]));

ALTER TABLE IF EXISTS isidbuser.bauvorhaben_wesentliche_rechtsgrundlage
    ADD CONSTRAINT bauvorhaben_wesentliche_recht_wesentliche_rechtsgrundlage_check CHECK (wesentliche_rechtsgrundlage::text = ANY (ARRAY['QUALIFIZIERTER_BEBAUUNGSPLAN'::character varying::text, 'VORHABENSBEZOGENER_BEBAUUNGSPLAN'::character varying::text, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30'::character varying::text, 'EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying::text, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9'::character varying::text, 'SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35'::character varying::text, 'INNENBEREICH'::character varying::text, 'AUSSENBEREICH'::character varying::text, 'BEFREIUNG'::character varying::text, 'FREIE_EINGABE'::character varying::text]));

ALTER TABLE IF EXISTS isidbuser.baugebiet
    ADD CONSTRAINT baugebiet_art_bauliche_nutzung_check CHECK (art_bauliche_nutzung::text = ANY (ARRAY['WR'::character varying::text, 'WA'::character varying::text, 'MU'::character varying::text, 'MK'::character varying::text, 'MI'::character varying::text, 'GE'::character varying::text, 'FREIE_EINGABE'::character varying::text]));

ALTER TABLE IF EXISTS isidbuser.bauvorhaben_art_fnp
    ADD CONSTRAINT bauvorhaben_art_fnp_art_fnp_check CHECK (art_fnp::text = ANY (ARRAY['WR'::character varying::text, 'WA'::character varying::text, 'MU'::character varying::text, 'MK'::character varying::text, 'MI'::character varying::text, 'GE'::character varying::text, 'FREIE_EINGABE'::character varying::text]));

END;